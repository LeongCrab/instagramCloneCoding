package com.example.demo.src.admin;

import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.model.*;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.utils.AES128;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.common.response.BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final AES128 aes128;

    @Transactional(readOnly = true)
    public List<GetUserRes> getUserList(int pageIndex, GetUserReq getUserReq){
        final int size = 10;

        String createdAt = dateFormat(getUserReq.getCreatedAt());
        String userName = encryptName(getUserReq.getUserName());
        State state = null;
        if(getUserReq.getState() != null) {
             state = State.valueOf(getUserReq.getState().toUpperCase());
        }

        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage = userRepository.findAllUsers(userName, getUserReq.getUserId(), state, createdAt, pageRequest);
        Page<GetUserRes> dtoPage = userPage.map(GetUserRes::new);

        return dtoPage.getContent();
    }

    private String encryptName(String name) {
        try{
            return aes128.encrypt(name);
        } catch (Exception exception){
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
    }

    private String dateFormat(String date) {
        if(date == null) {
            return null;
        }
        return LocalDate.parse(date).toString();
    }
}
