package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
