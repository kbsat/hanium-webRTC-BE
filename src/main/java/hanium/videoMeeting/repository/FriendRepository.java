package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
