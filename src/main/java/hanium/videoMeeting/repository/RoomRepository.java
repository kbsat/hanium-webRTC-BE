package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    Optional<Room> findByTitle(String title);

    Optional<Room> findBySession(String session);
}
