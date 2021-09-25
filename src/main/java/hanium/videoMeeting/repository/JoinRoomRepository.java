package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.Join_Room;
import hanium.videoMeeting.domain.Room;
import hanium.videoMeeting.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JoinRoomRepository extends JpaRepository<Join_Room, Long> {
    @Query("select jr from Join_Room jr" +
            " join fetch jr.room" +
            " where jr.user = :user and jr.room = :room")
    Optional<Join_Room> findByUserWithRoom(@Param("user") User user, @Param("room") Room room);
}
