package hanium.videoMeeting.controller;

import hanium.videoMeeting.Config.auth.PrincipalDetails;
import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api2")
public class AdminController {
    private final ResponseService responseService;
    @GetMapping(value = "/user")
    public Result findUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        User user = principalDetails.getUser();
//        System.out.println(user.getId());
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSuccessResult();
    }

}
