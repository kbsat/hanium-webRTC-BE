package hanium.videoMeeting.controller;

import hanium.videoMeeting.Config.jwt.JwtTokenProvider;
import hanium.videoMeeting.DTO.CreateUserDTO;
import hanium.videoMeeting.DTO.LoginRequestDto;
import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.advice.exception.PasswordWrongException;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.service.ResponseService;
import hanium.videoMeeting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class SignController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signin")
    public Result signIn(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.findUserByEmail(loginRequestDto.getId());
        if (!bCryptPasswordEncoder.matches(loginRequestDto.getPw(), user.getPassword()))
            throw new PasswordWrongException();

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRole()));

    }

    @PostMapping("/signup")
    public Result signUp(@RequestBody CreateUserDTO createUserDTO) {
        userService.join(createUserDTO);
        return responseService.getSuccessResult();
    }

    @GetMapping("/test")
    public String test() {
        return "good";
    }
}