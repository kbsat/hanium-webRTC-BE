package hanium.videoMeeting.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomDto {

    private String title;
    private String password;
}
