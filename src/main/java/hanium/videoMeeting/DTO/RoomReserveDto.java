package hanium.videoMeeting.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReserveDto {

    private String title;
    private String password;
    private LocalDateTime reservationTime;
}
