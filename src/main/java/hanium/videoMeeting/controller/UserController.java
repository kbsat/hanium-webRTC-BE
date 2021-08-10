package hanium.videoMeeting.controller;

import hanium.videoMeeting.Config.auth.PrincipalDetails;
import hanium.videoMeeting.Config.jwt.JwtTokenProvider;
import hanium.videoMeeting.DTO.ResponseMyInfoDto;
import hanium.videoMeeting.DTO.ResponseUserDto;
import hanium.videoMeeting.DTO.UpdateNameDto;
import hanium.videoMeeting.DTO.UpdatePasswordDTO;
import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.advice.exception.DuplicateNameException;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.service.ResponseService;
import hanium.videoMeeting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/user/all")
    public Result findAllUser() {
        return responseService.getListResult(userService.findAllUser().stream().map(ResponseUserDto::convertUserToDto).collect(Collectors.toList()));
    }

    @GetMapping("/user/{id}")
    public Result findUserById(@PathVariable Long id) {
        return responseService.getSingleResult(ResponseUserDto.convertUserToDto(userService.findUserById(Long.valueOf(id))));
    }

    @GetMapping("/user/me")
    public Result MyInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        ResponseMyInfoDto responseMyInfoDto = ResponseMyInfoDto.convertUserToMyInfoDto(user);
        return responseService.getSingleResult(responseMyInfoDto);
    }

    @PostMapping("/user/updatename")
    public Result updateName(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody UpdateNameDto updateNameDto) {
        Optional<User> userByName = userService.findUserByName(updateNameDto.getName());
        if (userByName.isEmpty()) {
            userService.updateName(updateNameDto, principalDetails.getUser().getId());
        } else {
            throw new DuplicateNameException();
        }
        return responseService.getSuccessResult();
    }
    @PostMapping("/user/updatepassword")
    public Result updatePassword(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User user = principalDetails.getUser();
        userService.updatePassword(updatePasswordDTO, user.getId());
        return responseService.getSuccessResult();
    }
}