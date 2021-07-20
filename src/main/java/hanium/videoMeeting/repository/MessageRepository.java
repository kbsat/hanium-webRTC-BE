package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
