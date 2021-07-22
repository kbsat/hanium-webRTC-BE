package hanium.videoMeeting.DTO;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String current_password;
    private String new_password;
    private String check_new_password;

}
