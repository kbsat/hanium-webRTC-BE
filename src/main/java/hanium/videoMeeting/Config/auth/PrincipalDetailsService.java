package hanium.videoMeeting.Config.auth;

import hanium.videoMeeting.advice.exception.NoSuchUserException;
import hanium.videoMeeting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new PrincipalDetails(userRepository.findByEmail(s).orElseThrow(NoSuchUserException::new));
    }
}
