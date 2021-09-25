package hanium.videoMeeting.DTO;

import hanium.videoMeeting.domain.Room;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomReadDto {

    private long id;

    private String title;

    private String hostName;

    private long peopleNum;

    public RoomReadDto(Room room){
        this.id = room.getId();
        this.title = room.getTitle();
        this.hostName = room.getHost().getName();
        this.peopleNum = room.getPeople_num();
    }
}
