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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;
@Api(tags = {"User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @ApiOperation(value = "모든 회원 조회", notes = "전체 회원의 정보를 가져온다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/user/all")
    public Result findAllUser() {
        return responseService.getListResult(userService.findAllUser().stream().map(ResponseUserDto::convertUserToDto).collect(Collectors.toList()));
    }
    @ApiOperation(value = "id(pk)로 회원 조회", notes = "PK를 통하여 회원을 검색한다")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/user/{id}")
    public Result findUserById(@PathVariable Long id) {
        return responseService.getSingleResult(ResponseUserDto.convertUserToDto(userService.findUserById(Long.valueOf(id))));
    }
    @ApiOperation(value = "내 정보 조회", notes = "토큰값을 기반으로 내 정보를 조회한다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/user/me")
    public Result MyInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        ResponseMyInfoDto responseMyInfoDto = ResponseMyInfoDto.convertUserToMyInfoDto(user);
        return responseService.getSingleResult(responseMyInfoDto);
    }
    @ApiOperation(value = "회원 이름 수정", notes = "회원 이름을 수정한다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
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
    @ApiOperation(value = "패스워드 변경", notes = "패스워드를 변경한다.")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    @PostMapping("/user/updatepassword")
    public Result updatePassword(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User user = principalDetails.getUser();
        userService.updatePassword(updatePasswordDTO, user.getId());
        return responseService.getSuccessResult();
    }
}