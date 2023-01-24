package com.example.demo.src.admin;

import com.example.demo.common.Constant;
import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.entity.Log;
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

import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final AES128 aes128;

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers(int pageIndex, GetUserReq getUserReq){
        final int size = 10;

        String userName = encryptName(getUserReq.getUserName());
        State state = null;
        if(getUserReq.getState() != null) {
             state = State.valueOf(getUserReq.getState().toUpperCase());
        }

        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage = userRepository.findAllUsers(userName, getUserReq.getUserId(), state, getUserReq.getCreatedAt(), pageRequest);
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

    public GetUserInfoRes getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        Constant.LoginType loginType = user.getLoginType();
        switch(loginType) {
            case ORIGINAL:
                return getOriginalUser(user);
            case KAKAO:
                return getKakaoUser(user);
            default:
                return new GetUserInfoRes();
        }
    }
    @Transactional(readOnly = true)
    public GetUserInfoRes getOriginalUser(User user){
        String lastLogin = getLastLogin(user);
        GetUserInfoRes getUserInfoRes = new GetUserInfoRes(user, lastLogin);

        try {
            getUserInfoRes.setName(aes128.decrypt(user.getName()));
            getUserInfoRes.setPhone(aes128.decrypt(user.getPhone()));
            getUserInfoRes.setBirthday(aes128.decrypt(user.getBirthday()));
            getUserInfoRes.setBirthYear(aes128.decrypt(user.getBirthYear()));
        } catch (Exception exception) {
            throw new BaseException(DECRYPTION_ERROR);
        }

        return getUserInfoRes;
    }

    @Transactional(readOnly = true)
    private String getLastLogin(User user){
        Log log = logRepository.findFirstByDataTypeAndMethodTypeAndUserIdOrderByCreatedAtDesc(Constant.DataType.LOGIN, Constant.MethodType.CREATE, user.getId())
                .orElseThrow(()-> new BaseException(NOT_FIND_LOG));
        return log.getCreatedAt().toString();
    }

    @Transactional(readOnly = true)
    private GetUserInfoRes getKakaoUser(User user){
        String lastLogin = getLastLogin(user);
        GetUserInfoRes getUserInfoRes = new GetUserInfoRes(user, lastLogin);
        try {
            getUserInfoRes.setName(aes128.decrypt(user.getName()));
            getUserInfoRes.setBirthday(aes128.decrypt(user.getBirthday()));
        } catch (Exception exception) {
            throw new BaseException(DECRYPTION_ERROR);
        }

        return getUserInfoRes;
    }
    public void banUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.banUser();
    }
}
