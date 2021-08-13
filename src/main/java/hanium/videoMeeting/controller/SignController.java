package hanium.videoMeeting.controller;

import hanium.videoMeeting.Config.jwt.JwtTokenProvider;
import hanium.videoMeeting.DTO.CreateUserDTO;
import hanium.videoMeeting.DTO.LoginRequestDto;
import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.advice.exception.PasswordWrongException;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.service.ResponseService;
import hanium.videoMeeting.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@Api(tags={"Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class SignController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @ApiOperation(value = "로그인", notes = "로그인 성공시 jwt토큰을 받는다")
    @PostMapping("/signin")
    public Result signIn(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.findUserByEmail(loginRequestDto.getId());
        if (!bCryptPasswordEncoder.matches(loginRequestDto.getPw(), user.getPassword()))
            throw new PasswordWrongException();

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRole()));

    }
    @ApiOperation(value = "회원가입", notes = "회원가입 요청을 보낸다.")
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