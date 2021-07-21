package hanium.videoMeeting.domain;

import hanium.videoMeeting.DTO.CreateUserDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    //@Column(nullable = false)
    private String password;

    //@Column(nullable = false,unique = true)
    private String email;

    //@Column(nullable = false)
    private LocalDateTime create_date;

    //@Column(nullable = false)
    private boolean state;

    public static User createUser(CreateUserDTO createUserDTO) {
        User user=new User();
        user.name=createUserDTO.getName();
        user.create_date= LocalDateTime.now();
        user.email= createUserDTO.getEmail();
        user.password= createUserDTO.getPassword();
        return user;
    }
}
