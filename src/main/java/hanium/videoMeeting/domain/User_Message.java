package hanium.videoMeeting.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class User_Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_message_id")
    private long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sender_id")
    private User sender;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="receiver_id")
    private User receiver;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="message_id")
    private Message message;

    @Column(nullable = false)
    private boolean is_view;

}
