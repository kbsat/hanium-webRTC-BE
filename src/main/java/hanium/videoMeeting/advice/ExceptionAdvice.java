package hanium.videoMeeting.advice;

import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.advice.exception.*;
import hanium.videoMeeting.service.ResponseService;
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
    }@ExceptionHandler(PasswordWrongException.class)
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


}
