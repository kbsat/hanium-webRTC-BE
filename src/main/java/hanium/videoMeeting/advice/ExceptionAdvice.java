package hanium.videoMeeting.advice;

import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.advice.exception.*;
import hanium.videoMeeting.service.ResponseService;
import io.openvidu.java.client.OpenViduException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result defaultException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        return responseService.getFailResult(-1000, "오류가 발생하였습니다.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result AccessDeniedException() {
        return responseService.getFailResult(-1001, "해당 작업에 권한이 없습니다.");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result AuthenticationEntryPointException() {
        return responseService.getFailResult(-1002, "인증정보가 유효하지 않습니다.");
    }

    @ExceptionHandler(ExistedEmailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result ExistedEmailException() {
        return responseService.getFailResult(-1003, "이미 존재하는 이메일입니다");
    }

    @ExceptionHandler(ExistedNameException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result ExistedNameException() {
        return responseService.getFailResult(-1004, "이미 존재하는 이름입니다.");
    }

    @ExceptionHandler(NoSuchUserException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result NoSuchUserException() {
        return responseService.getFailResult(-1005, "없는 회원 입니다.");
    }

    @ExceptionHandler(DuplicateNameException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result DuplicateNameException() {
        return responseService.getFailResult(-1006, "이미 존재하는 이름입니다.");
    }

    @ExceptionHandler(CurrentPasswordDiffException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result CurrentPasswordDiffException() {
        return responseService.getFailResult(-1007, "현재 비밀번호가 틀렸습니다");
    }

    @ExceptionHandler(PasswordWrongException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result PasswordWrongException() {
        return responseService.getFailResult(-1008, "비밀번호가 틀렸습니다.");
    }

    @ExceptionHandler(PasswordNoChangeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result PasswordNoChangeException() {
        return responseService.getFailResult(-1009, "이전과 같은 비밀번호입니다");
    }

    @ExceptionHandler(PasswordDiffException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result PasswordDiffException() {
        return responseService.getFailResult(-1010, "비밀번호와 확인 비밀번호가 다릅니다");
    }

    @ExceptionHandler(OpenViduException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result OpenviduException() {
        return responseService.getFailResult(-2000, "오픈비두 서버 오류가 발생했습니다.");
    }

    @ExceptionHandler(ExistedRoomTitleException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result ExistedRoomTitleException() {
        return responseService.getFailResult(-2001, "중복된 방 제목이 존재합니다.");
    }

    @ExceptionHandler(NoRoomSessionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result NoRoomSessionException() {
        return responseService.getFailResult(-2002, "방에 세션이 존재하지 않습니다.");
    }

    @ExceptionHandler(NoSuchSessionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result NoSuchSessionException() {
        return responseService.getFailResult(-2003, "오픈비두 클라이언트에서 해당 세션을 찾을 수 없습니다.(오픈비두 서버를 확인하세요)");
    }

    @ExceptionHandler(NoSuchRoomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result NoSuchRoomException() {
        return responseService.getFailResult(-2004, "해당 정보와 일치하는 방을 찾을 수 없습니다.");
    }

    @ExceptionHandler(NoSuchJoinRoomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result NoSuchJoinRoomException() {
        return responseService.getFailResult(-2005, "해당 방에 접속 되어있는지 확인하세요.");
    }

    @ExceptionHandler(NotWorkingExitException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result NotWorkingExitException() {
        return responseService.getFailResult(-2006, "방 나가기에 실패했습니다.");
    }

    @ExceptionHandler(JoinDuplException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result JoinDuplException() {
        return responseService.getFailResult(-2007, "중복된 방 참여는 불가능합니다.");
    }

    @ExceptionHandler(NoRoomPasswordException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result NoRoomPasswordException() {
        return responseService.getFailResult(-2008, "방예약은 비밀번호가 필수입니다.");
    }

    @ExceptionHandler(ExistedEmailAndNameException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result ExistedEmailAndNameException() {
        return responseService.getFailResult(-1011, "닉네임과 이메일 모두 존재합니다 ");
    }
}
