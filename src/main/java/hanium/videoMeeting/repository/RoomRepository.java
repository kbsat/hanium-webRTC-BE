package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    Optional<Room> findByTitle(String title);

    Optional<Room> findBySession(String session);

    //SELECT * FROM room AS r
    // WHERE r.start_time + interval 4 HOUR >= NOW() AND r.start_time <= NOW() AND r.password = '' AND r.session != null;
    @Query("select r from Room r" +
            " where r.session is not null" +
            "   and r.password = ''" +
            " order by r.id desc")
    List<Room> findByPage(Pageable pageable);

    @Query("select count(*) from Room r" +
            " where r.session is not null" +
            "   and r.password = ''" +
            " order by r.id desc")
    long countPublicRoom();

    @Query("select r from Room r" +
            " join fetch r.host h" +
            " where r.isReserved = true" +
            "   and r.start_time between :start and :end")
    List<Room> findReservedRoomsBetweenTime(@Param("start") LocalDateTime start,@Param("end") LocalDateTime end);

}
