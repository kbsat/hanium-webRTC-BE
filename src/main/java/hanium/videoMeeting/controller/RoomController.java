package hanium.videoMeeting.controller;

import hanium.videoMeeting.Config.auth.PrincipalDetails;
import hanium.videoMeeting.DTO.ResponseRoomDto;
import hanium.videoMeeting.DTO.RoomDto;
import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.advice.exception.AuthenticationEntryPointException;
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
@Api(tags={"Room"})
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
    public Result createRoom(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody RoomDto roomDto){
        Long userId = principalDetails.getUser().getId();
        String session = roomService.create(roomDto, userId);

        return responseService.getSingleResult(session);
    }
    @ApiOperation(value = "회의방 참가", notes = "회의방 참가 성공시 토큰값을 반환한다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @PostMapping("/join")
    public Result joinRoom(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody RoomDto roomDto){
        String token = roomService.join(roomDto, principalDetails.getUser().getId());

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
    public Result deleteRoom(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable String session){
        User user = principalDetails.getUser();

        Room room = roomService.findRoomBySession(session);

        log.info("room : {}, room_host = {}, room_host_id = {}",room,room.getHost(),room.getHost().getId());
        // 현재 로그인한 유저와 방의 호스트와 일치하는지 확인
        if (user.getId().equals(room.getHost().getId())) {
            roomService.delete(room);
            return responseService.getSuccessResult();
        }else{
            return responseService.getFailResult(-2000,"해당 방을 삭제할 권한이 없습니다.");
        }
    }

}
