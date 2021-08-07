package hanium.videoMeeting.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private LocalDateTime start_time;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String session;

    @Column(nullable = false)
    private long people_num;

    /*
    @JoinColumn(name="host_id")
    private User host;
     */

    public Room(String title, String password) {
        this.title = title;
        this.password = password;
        this.start_time = LocalDateTime.now();
        this.session = null;
        this.people_num = 0;
    }

    public void connectSession(String sessionId){
        this.session = sessionId;
    }


}
