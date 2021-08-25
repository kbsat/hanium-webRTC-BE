package hanium.videoMeeting.domain;

import hanium.videoMeeting.DTO.CreateUserDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.time.LocalDateTime;

import static hanium.videoMeeting.domain.Role.ROLE_USER;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDateTime create_date;

    //@Column(nullable = false)
    private boolean state;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String image;

    public static User createUser(CreateUserDTO createUserDTO) {
        User user=new User();
        user.name=createUserDTO.getName();
        user.create_date= LocalDateTime.now();
        user.email= createUserDTO.getEmail();
        user.password= createUserDTO.getPassword();
        user.image="https://hanium-imagestorage.s3.ap-northeast-2.amazonaws.com/static/basic.png";
        user.role = ROLE_USER;
        return user;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImage(String url){
        this.image=url;

    }
}
