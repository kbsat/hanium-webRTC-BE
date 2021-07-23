package hanium.videoMeeting.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePasswordDTO {
    private String current_password;
    private String new_password;
    private String check_new_password;

}
