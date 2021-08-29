package hanium.videoMeeting.DTO;

import hanium.videoMeeting.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ResponseRoomReserveDto {
    private String title;
    private String password;
    private LocalDateTime startTime;

    public static ResponseRoomReserveDto convertRoomToResponseRoomReserveDto(Room room) {
        return ResponseRoomReserveDto.builder()
                .title(room.getTitle())
                .password(room.getPassword())
                .startTime(room.getStart_time())
                .build();
    }
}
