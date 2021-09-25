package hanium.videoMeeting.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Join_Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="join_room_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    @Column(nullable = false)
    private String token;

    public Join_Room(User user, Room room, String token) {
        this.user = user;
        this.room = room;
        this.token = token;

        // TODO 동시성 문제가 발생하지 않을까?
        room.plusJoinPeople();
    }
}
