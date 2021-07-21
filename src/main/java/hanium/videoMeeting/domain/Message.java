package hanium.videoMeeting.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private long id;

    @Lob
    private String contents;

    private LocalDateTime send_date;
    public static Message CreateMessage(String contents) {
        Message message=new Message();
        message.contents=contents;
        message.send_date=LocalDateTime.now();
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", send_date=" + send_date +
                '}';
    }
}
