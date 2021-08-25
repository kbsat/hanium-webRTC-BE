package hanium.videoMeeting.controller;

import hanium.videoMeeting.Config.auth.PrincipalDetails;
import hanium.videoMeeting.DTO.response.Result;
import hanium.videoMeeting.repository.UserRepository;
import hanium.videoMeeting.service.ResponseService;
import hanium.videoMeeting.service.S3Uploader;
import hanium.videoMeeting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ImageController {
    private final S3Uploader s3Uploader;
    private final ResponseService responseService;
    private final UserService userService;
    @PostMapping("/images")
    public Result upload(@RequestParam("images") MultipartFile multipartFile,@AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        String imageUrl = s3Uploader.upload(multipartFile, "static");
        userService.changeProfileImage(imageUrl,principalDetails.getUser().getId());
        return responseService.getSuccessResult();
    }
}
