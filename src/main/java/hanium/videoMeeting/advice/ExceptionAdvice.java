package hanium.videoMeeting.advice;

import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.advice.exception.AccessDeniedException;
import hanium.videoMeeting.advice.exception.AuthenticationEntryPointException;
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
        return responseService.getFailResult(-1010, "권한이 없습니다.");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result AuthenticationEntryPointException() {
        return responseService.getFailResult(-1011, "EntryPoint에러.");
    }
}
