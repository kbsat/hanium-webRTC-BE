package hanium.videoMeeting.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private String session;

    @Column(nullable = false)
    private long people_num;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Join_Room> joinRooms = new ArrayList<>();

    // 예약한 방인지 확인
    private Boolean isReserved;

    public Room(User host, String title, String password) {
        this.host = host;

        this.title = title;
        this.password = password;
        this.start_time = LocalDateTime.now();
        this.session = null;
        this.people_num = 0;
    }

    public Room(User host, String title, String password, LocalDateTime reservedDate) {
        this.host = host;

        this.title = title;
        this.password = password;
        this.start_time = reservedDate;
        this.session = null;
        this.people_num = 0;

        this.isReserved = true;
    }

    public void connectSession(String sessionId) {
        this.session = sessionId;
    }

    // TODO 동시성 문제가 발생하지 않을까?
    public void plusJoinPeople() {
        this.people_num += 1;
    }

    public void minusJoinPeople() {
        this.people_num -= 1;
    }

    public void reserveDone(){
        this.isReserved = null;
    }
}
