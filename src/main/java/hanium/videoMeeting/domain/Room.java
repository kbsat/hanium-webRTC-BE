package hanium.videoMeeting.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime start_time;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private long people_num;

    /*
    @JoinColumn(name="host_id")
    private User host;
     */

}
