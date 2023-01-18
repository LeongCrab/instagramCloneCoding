package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "USER") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
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
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String birthday;

    @Column(nullable = false)
    private LocalDate privacyExpiredAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.ORIGINAL;

    @Builder
    public User(Long id, String phone, String name, String userId, String password, String birthday, LocalDate privacyExpiredAt, UserType userType) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.birthday = birthday;
        this.privacyExpiredAt = privacyExpiredAt;
        this.userType = userType;
    }
    public enum UserType {
            ORIGINAL, KAKAO, NAVER, GOOGLE, APPLE
    }


    public void updateName(String name) {
        this.name = name;
    }

    public void deleteUser() {
        this.state = State.DELETED;
    }
}
