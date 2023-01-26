package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

import static com.example.demo.common.Constant.*;

@Audited
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "USER")
public class User extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String birthday;

    @Column
    private String birthYear;

    @Column
    private Timestamp lastLogin;

    @Column(nullable = false)
    private LocalDate privacyExpiredAt = LocalDate.now().plusYears(1);

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType = LoginType.ORIGINAL;

    @Column(name="profile_text")
    private String profileText;

    @Column(name="profile_image")
    private String profileImage;

    @Column
    @Enumerated(EnumType.STRING)
    private UserState userState = UserState.ACTIVE;

    @Builder
    public User(Long id, String phone, String name, String loginId, String password, String birthday, String birthYear, LocalDate privacyExpiredAt, LoginType loginType) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.birthday = birthday;
        this.birthYear = birthYear;
        this.privacyExpiredAt = privacyExpiredAt;
        this.loginType = loginType;
    }
    public void encryptOAuthUser(String encryptName, String encryptBirthday) {
        this.name = encryptName;
        this.birthday = encryptBirthday;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateLogin() {
        Long datetime = System.currentTimeMillis();
        this.lastLogin = new Timestamp(datetime);
    }

    public void updateProfile(String profileImage, String profileText){
        this.profileImage = profileImage;
        this.profileText = profileText;
    }

    public void deleteUser() {
        this.state = State.INACTIVE;
        this.userState = UserState.DELETED;
    }

    public void banUser() {
        this.state = State.INACTIVE;
        this.userState = UserState.BANNED;
    }
}
