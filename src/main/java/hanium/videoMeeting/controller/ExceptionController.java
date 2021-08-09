package hanium.videoMeeting.controller;

import hanium.videoMeeting.advice.exception.AccessDeniedException;
import hanium.videoMeeting.advice.exception.AuthenticationEntryPointException;
import hanium.videoMeeting.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {
    @GetMapping(value = "/entrypoint")
    public void entrypointException() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping(value = "/accessdenied")
    public void accessDeniedException() {
        throw new AccessDeniedException();
    }

}