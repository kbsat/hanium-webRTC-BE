package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.CreateUserDTO;
import hanium.videoMeeting.advice.exception.ExistedEmailException;
import hanium.videoMeeting.advice.exception.ExistedNameException;
import hanium.videoMeeting.advice.exception.PasswordDiffException;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        System.out.println(createUserDTO.getPassword());
        User save = userRepository.save(User.createUser(createUserDTO));
        return save.getId();
    }


}
