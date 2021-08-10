package hanium.videoMeeting.DTO;

import hanium.videoMeeting.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserDto {
    private Long id;
    private String Email;
    private String name;

    public static ResponseUserDto convertUserToDto(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .Email(user.getEmail())
                .name(user.getName())
                .build();
    }

}
