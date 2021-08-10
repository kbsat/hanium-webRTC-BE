package hanium.videoMeeting.DTO;

import hanium.videoMeeting.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMyInfoDto {
    private String email;
    private String name;
    private String image;
    private LocalDateTime signUpDate;

    public static ResponseMyInfoDto convertUserToMyInfoDto(User user) {
        return ResponseMyInfoDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .signUpDate(user.getCreate_date())
                .image(user.getImage())
                .build();
    }
}
