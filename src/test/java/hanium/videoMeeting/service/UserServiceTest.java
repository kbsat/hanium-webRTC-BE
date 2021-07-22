package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.CreateUserDTO;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Test
    public void 회원가입_테스트() throws Exception {
        //given
        CreateUserDTO createUserDTO=new CreateUserDTO("wnsqja@naver.com","12345","12345", "jb");
        //when
        Long join = userService.join(createUserDTO);

        //then
        User one = userRepository.getById(join);
        Assertions.assertThat("wnsqja@naver.com").isEqualTo(one.getEmail());


    }

}