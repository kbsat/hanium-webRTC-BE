package hanium.videoMeeting.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReserveMailDto {
    String email;
    String roomName;
    String roomHostName;
    LocalDateTime reserveTime;

    @Builder
    public ReserveMailDto(String email, String roomName, String roomHostName, LocalDateTime reserveTime) {
        this.email = email;
        this.roomName = roomName;
        this.roomHostName = roomHostName;
        this.reserveTime = reserveTime;
    }
}
