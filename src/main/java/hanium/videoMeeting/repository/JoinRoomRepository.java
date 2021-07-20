package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.Join_Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinRoomRepository extends JpaRepository<Join_Room,Long> {
}
