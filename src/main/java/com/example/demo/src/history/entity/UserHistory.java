package com.example.demo.src.history.entity;

import com.example.demo.common.entity.BaseEntity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

import static com.example.demo.common.Constant.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "USER_HISTORY")
public class UserHistory extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

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
    private String lastLogin;

    @Column(nullable = false)
    private LocalDate privacyExpiredAt = LocalDate.now().plusYears(1);

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column
    private String profileText;

    @Column
    private String profileImage;

    @Column
    @Enumerated(EnumType.STRING)
    private UserState userState = UserState.ACTIVE;


    @Builder
    public UserHistory(Long id, Long userId, String phone, String name, String loginId, String password, String birthday, String lastLogin, String birthYear, LocalDate privacyExpiredAt, LoginType loginType, String profileImage, String profileText) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.birthday = birthday;
        this.birthYear = birthYear;
        this.lastLogin = lastLogin;
        this.privacyExpiredAt = privacyExpiredAt;
        this.loginType = loginType;
        this.profileImage = profileImage;
        this.profileText = profileText;
    }
}