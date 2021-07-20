package hanium.videoMeeting.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user_id;

    @OneToOne
    @JoinColumn(name="friend_id")
    private User friend_id;

    private boolean is_friend;
}
