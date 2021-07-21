package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    
}
