package hanium.videoMeeting.controller;

import hanium.videoMeeting.Config.jwt.JwtTokenProvider;
import hanium.videoMeeting.DTO.ResponseUserDto;
import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.service.ResponseService;
import hanium.videoMeeting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "/users")
    public Result findAllUser() {
        return responseService.getListResult(userService.findAllUser().stream().map(ResponseUserDto::convertUserToDto).collect(Collectors.toList()));
    }

    @GetMapping(value = "/OneUser")
    public Result findUserById(@RequestHeader String token) {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        String userPk = jwtTokenProvider.getUserPk(token);
        System.out.println(userPk);
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userService.findUserById(userPk));
    }

}