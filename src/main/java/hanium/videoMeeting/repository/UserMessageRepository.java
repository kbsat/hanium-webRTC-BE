package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.User_Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMessageRepository extends JpaRepository<User_Message,Long> {
}
