package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.CreateUserDTO;
import hanium.videoMeeting.DTO.UpdateNameDto;
import hanium.videoMeeting.DTO.UpdatePasswordDTO;
import hanium.videoMeeting.advice.exception.ExistedEmailAndNameException;
import hanium.videoMeeting.advice.exception.NoSuchUserException;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void pre() {
        CreateUserDTO createUserDTO = new CreateUserDTO("test@naver.com", "12345", "12345", "test");

        try {
            userService.join(createUserDTO);
        }catch(ExistedEmailAndNameException e){
            System.out.println("이미 생성된 test@naver.com");
        }
    }

    @Test
    public void 회원가입_테스트() throws Exception {
        //given
        CreateUserDTO createUserDTO=new CreateUserDTO("wnsqja@naver.com","12345","12345", "jb");
        //when
        Long join = userService.join(createUserDTO);

        //then
        User one = userRepository.getById(join);
        assertThat("wnsqja@naver.com").isEqualTo(one.getEmail());

    }
    @Test
    public void 회원정보_업데이트_테스트() throws Exception {
        //given
        User user = userRepository.findByName("test").orElseThrow(NoSuchUserException::new);
        UpdateNameDto updateNameDto = new UpdateNameDto("change");
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("12345", "1234", "1234");
        User result=userService.findUserById(user.getId());
        System.out.println(result.getPassword());
        System.out.println(bCryptPasswordEncoder.encode("12345"));

        //when
        userService.updateName(updateNameDto,user.getId());
        userService.updatePassword(updatePasswordDTO, user.getId());

        //then
        assertThat(result.getName()).isEqualTo("change");
        assertThat(bCryptPasswordEncoder.matches("1234", result.getPassword())).isEqualTo(true);
        assertThat(bCryptPasswordEncoder.matches("12345", result.getPassword())).isEqualTo(false);

    }

}