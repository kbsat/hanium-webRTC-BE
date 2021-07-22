package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.CreateUserDTO;
import hanium.videoMeeting.DTO.UpdateNameDto;
import hanium.videoMeeting.DTO.UpdatePasswordDTO;
import hanium.videoMeeting.advice.exception.CurrentPasswordDiffException;
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
        if (!bCryptPasswordEncoder.encode(updatePasswordDTO.getCurrent_password()).
                equals(bCryptPasswordEncoder.encode(user.getPassword()))) {
            throw new CurrentPasswordDiffException();
        }
        if (!updatePasswordDTO.getNew_password().equals(updatePasswordDTO.getCheck_new_password())) {
            throw new PasswordDiffException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(updatePasswordDTO.getNew_password()));
    }




}
