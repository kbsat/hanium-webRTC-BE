package hanium.videoMeeting.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Join_Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="join_room_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user_id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room_id;

    @Column(nullable = false)
    private String token;

    public Join_Room(User user, Room room, String token) {
        this.user_id = user;
        this.room_id = room;
        this.token = token;
    }
}
