package hanium.videoMeeting.controller;

import hanium.videoMeeting.Config.auth.PrincipalDetails;
import hanium.videoMeeting.Config.jwt.JwtTokenProvider;
import hanium.videoMeeting.DTO.ResponseUserDto;
import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.advice.exception.NoSuchUserException;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.service.ResponseService;
import hanium.videoMeeting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
/*
    @GetMapping(value = "/user")
    public Result findUserById(@RequestHeader String token) {
        String userPk = jwtTokenProvider.getUserPk(token);
        User userById = userService.findUserById(Long.valueOf(userPk));
        System.out.println(userById.getRole());
        return responseService.getSingleResult(ResponseUserDto.convertUserToDto(userService.findUserById(Long.valueOf(userPk))));
    }

 */
/*
    @GetMapping(value = "/user")
    public Result findUser() {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        System.out.println(id);
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userService.findUserByEmail(id));
    }

 */
    @GetMapping(value = "/user")
    public Result findUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        User user = principalDetails.getUser();
        System.out.println(user.getId());
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSuccessResult();
    }

}