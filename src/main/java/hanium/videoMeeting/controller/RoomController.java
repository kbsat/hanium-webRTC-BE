package hanium.videoMeeting.controller;

import hanium.videoMeeting.Config.auth.PrincipalDetails;
import hanium.videoMeeting.DTO.*;
import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.advice.exception.NoSuchRoomException;
import hanium.videoMeeting.advice.exception.NotWorkingExitException;
import hanium.videoMeeting.domain.Room;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.service.ResponseService;
import hanium.videoMeeting.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Room"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
@Slf4j
public class RoomController {
    private final RoomService roomService;
    private final ResponseService responseService;

    @ApiOperation(value = "회의방 생성", notes = "회의방 생성 성공시 세션값을 반환한다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @PostMapping
    public Result createRoom(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody RoomDto roomDto) {
        Long userId = principalDetails.getUser().getId();
        String session = roomService.create(roomDto, userId);

        return responseService.getSingleResult(session);
    }

    @ApiOperation(value = "회의방 참가", notes = "회의방 참가 성공시 토큰값을 반환한다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @PostMapping("/join")
    public Result joinRoom(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody RoomDto roomDto) {
        String token = roomService.join(roomDto, principalDetails.getUser().getId());

        // 방이 세션 타임이 지나서 사라졌을 경우
        if (token == null) {
            throw new NoSuchRoomException();
        }
        return responseService.getSingleResult(token);
    }

    @ApiOperation(value = "회의방 정보찾기", notes = "세션으로 회의방을 찾아 방 정보를 제공한다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{session}")
    public Result roomInfo(@PathVariable String session) {
        Room room = roomService.findRoomBySession(session);

        ResponseRoomDto responseRoomDto = ResponseRoomDto.convertRoomToResponseRoomDto(room);
        return responseService.getSingleResult(responseRoomDto);
    }

    @DeleteMapping("/{session}")
    @ApiOperation(value = "회의방 삭제", notes = "회의방 호스트일시 방삭제에 성공하고 아니면 실패한다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    public Result deleteRoom(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable String session) {
        User user = principalDetails.getUser();

        Room room = roomService.findRoomBySession(session);

        log.info("room : {}, room_host = {}, room_host_id = {}", room, room.getHost(), room.getHost().getId());
        // 현재 로그인한 유저와 방의 호스트와 일치하는지 확인
        if (user.getId().equals(room.getHost().getId())) {
            roomService.delete(room);
            return responseService.getSuccessResult();
        } else {
            return responseService.getFailResult(-2000, "해당 방을 삭제할 권한이 없습니다.");
        }
    }

    @ApiOperation(value = "회의방 예약", notes = "회의방 예약 성공시 예약된 회의방 이름을 반환한다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @PostMapping("/reserve")
    public Result reserveRoom(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody RoomReserveDto roomReserveDto) {
        Long userId = principalDetails.getUser().getId();
        String roomTitle = roomService.reserve(roomReserveDto, userId);

        //front의 예약완료 메시지 대로 결과 출력
        Room room = roomService.findRoomByTitle(roomTitle);
        ResponseRoomReserveDto responseRoomReserveDto = ResponseRoomReserveDto.convertRoomToResponseRoomReserveDto(room);
        return responseService.getSingleResult(responseRoomReserveDto);
    }

    @ApiOperation(value = "회의 공개방 목록", notes = "회의방 중 공개방의 목록을 보여준다. ( 비밀번호가 ''인 방 )")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = false, dataType = "String", paramType = "header")
    @GetMapping
    public Result listRoom(@RequestParam(value = "page", defaultValue = "0") long page,
                           @RequestParam(value = "size", defaultValue = "10") long size) {
        // 방 중 비밀번호가 공란이고 세션만료가 되지않은 방이 공개방.
        List<RoomReadDto> roomPageDto = roomService.findRoomByPage((int) page, (int) size);
        long count = roomPageDto.size();

        return responseService.getPageResult(roomPageDto, count, page);

    }

    @ApiOperation(value = "방 나가기", notes = "방을 나간다. (join_room 삭제) 호스트가 나갈 경우 방이 삭제됨")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @PostMapping("/exit/{session}")
    public Result exitRoom(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable String session) {
        Long userId = principalDetails.getUser().getId();
        boolean isExit = roomService.exit(userId, session);

        if (isExit) {
            return responseService.getSuccessResult();
        } else {
            throw new NotWorkingExitException();
        }
    }


}
