package hanium.videoMeeting.repository;

import hanium.videoMeeting.domain.Message;
import hanium.videoMeeting.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired UserRepository userRepository;
    @Autowired MessageRepository messageRepository;
    @Test
    public void 레포지토리_테스트() throws Exception {
        //given
        Message message=Message.CreateMessage("하이");
        System.out.println(message);
        messageRepository.save(message);

        //when


        //then

    }

}