package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.utils.SHA256;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static com.example.demo.common.Constant.*;


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

    @Column(nullable = false)
    private LocalDate privacyExpiredAt = LocalDate.now().plusYears(1);

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType = LoginType.ORIGINAL;

    @Builder
    public User(Long id, String phone, String name, String loginId, String password, String birthday, LocalDate privacyExpiredAt, LoginType loginType) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.birthday = birthday;
        this.privacyExpiredAt = privacyExpiredAt;
        this.loginType = loginType;
    }


    public void updatePassword(String password) {
        this.password = new SHA256().encrypt(password);
    }

    public void deleteUser() {
        this.state = State.DELETED;
    }
}
