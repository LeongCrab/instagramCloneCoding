package com.example.demo.src.history;


import com.example.demo.src.history.entity.UserHistory;
import com.example.demo.src.user.entity.User;
import com.example.demo.utils.BeanUtil;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class UserEntityListener {
    @PostPersist
    public void postPersist(Object o) {
        UserHistoryRepository userHistoryRepository = BeanUtil.getBean(UserHistoryRepository.class);

        User user = (User) o;

        UserHistory userHistory = UserHistory.builder()
                .userId(user.getId())
                .phone(user.getPhone())
                .name(user.getName())
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .birthday(user.getBirthday())
                .birthYear(user.getBirthYear())
                .privacyExpiredAt(user.getPrivacyExpiredAt())
                .loginType(user.getLoginType())
                .profileImage(user.getProfileImage())
                .profileText(user.getProfileText())
                .build();

        userHistoryRepository.save(userHistory);
    }
    @PostUpdate
    public void postUpdate(Object o) {
        UserHistoryRepository userHistoryRepository = BeanUtil.getBean(UserHistoryRepository.class);

        User user = (User) o;

        UserHistory userHistory = UserHistory.builder()
                .userId(user.getId())
                .phone(user.getPhone())
                .name(user.getName())
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .birthday(user.getBirthday())
                .birthYear(user.getBirthYear())
                .lastLogin(user.getLastLogin().toString())
                .privacyExpiredAt(user.getPrivacyExpiredAt())
                .loginType(user.getLoginType())
                .profileImage(user.getProfileImage())
                .profileText(user.getProfileText())
                .build();

        userHistoryRepository.save(userHistory);
    }
}
