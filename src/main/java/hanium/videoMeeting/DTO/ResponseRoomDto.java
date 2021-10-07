package hanium.videoMeeting.DTO;

import hanium.videoMeeting.domain.Room;
import hanium.videoMeeting.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ResponseRoomDto {
    private Long roomId;
    private String title;
    private String password;
    private LocalDateTime startTime;
    private String session;
    private Long host_id;

    public static ResponseRoomDto convertRoomToResponseRoomDto(Room room) {
        return ResponseRoomDto.builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .password(room.getPassword())
                .startTime(room.getStart_time())
                .session(room.getSession())
                .host_id(room.getHost().getId())
                .build();
    }
}
