package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
