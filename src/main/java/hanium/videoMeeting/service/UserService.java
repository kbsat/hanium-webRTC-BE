package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.CreateUserDTO;
import hanium.videoMeeting.DTO.UpdateNameDto;
import hanium.videoMeeting.DTO.UpdatePasswordDTO;
import hanium.videoMeeting.advice.exception.*;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long join(CreateUserDTO createUserDTO) {
        if (userRepository.findByEmail(createUserDTO.getEmail()).isPresent()) {
            throw new ExistedEmailException();
        }
        if (userRepository.findByName(createUserDTO.getName()).isPresent()) {
            throw new ExistedNameException();
        }
        if (!createUserDTO.getPassword().equals(createUserDTO.getCheckPassword())) {
            throw new PasswordDiffException();
        }
        createUserDTO.setPassword(bCryptPasswordEncoder.encode(createUserDTO.getPassword()));
        User save = userRepository.save(User.createUser(createUserDTO));
        return save.getId();
    }

    @Transactional
    public void updateName(UpdateNameDto updateNameDto,Long id) {
        if (userRepository.findByName(updateNameDto.getName()).isPresent()) {
            throw new ExistedNameException();
        } else {
            User user = userRepository.getById(id);
            user.setName(updateNameDto.getName());
        }
    }

    @Transactional
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO,Long id) {
        User user = userRepository.getById(id);
        if (!bCryptPasswordEncoder.matches(updatePasswordDTO.getCurrent_password(),user.getPassword())) {
            throw new CurrentPasswordDiffException();
        } else if (bCryptPasswordEncoder.matches(updatePasswordDTO.getNew_password(),user.getPassword())) {
            throw new PasswordNoChangeException();
        }
        if (!updatePasswordDTO.getNew_password().equals(updatePasswordDTO.getCheck_new_password())) {
            throw new PasswordDiffException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(updatePasswordDTO.getNew_password()));
        System.out.println(user.getPassword());
    }

    public Optional<User> findUserByName(String name) {
        return userRepository.findByName(name);
    }

    public User findUserByEmail(String email) {
        System.out.println(email);
        return userRepository.findByEmail(email).orElseThrow(NoSuchUserException::new);
    }


    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchUserException::new);
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }



}
