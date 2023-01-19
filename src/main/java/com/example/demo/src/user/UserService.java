package com.example.demo.src.user;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) {
        //중복 체크
        Optional<User> checkUser = userRepository.findByUserIdAndState(postUserReq.getUserId(), ACTIVE);
        if(checkUser.isPresent()){
            throw new BaseException(POST_USERS_EXISTS_USERID);
        }
        //휴대폰 번호 암호화
        String encryptPhone;
        try {
            encryptPhone = new SHA256().encrypt(postUserReq.getPhone());
            postUserReq.setPhone(encryptPhone);
        } catch (Exception exception) {
            throw new BaseException(PHONE_ENCRYPTION_ERROR);
        }
        //이름 암호화
        String encryptName;
        try {
            encryptName = new SHA256().encrypt(postUserReq.getName());
            postUserReq.setName(encryptName);
        } catch (Exception exception) {
            throw new BaseException(NAME_ENCRYPTION_ERROR);
        }
        //비밀번호 암호화
        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(encryptPwd);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        //생일 암호화
        String encryptBirthday;
        try {
            encryptBirthday = new SHA256().encrypt(postUserReq.getBirthday());
            postUserReq.setBirthday(encryptBirthday);
        } catch (Exception exception) {
            throw new BaseException(BIRTHDAY_ENCRYPTION_ERROR);
        }

        User saveUser = userRepository.save(postUserReq.toEntity());

        //String jwtToken = jwtService.createJwt(saveUser.getId());
        //return new PostUserRes(saveUser.getId(), jwtToken);
        return new PostUserRes(saveUser.getId());

    }

    public PostUserRes createOAuthUser(User user) {
        //이름 암호화
        String encryptName;
        try {
            encryptName = new SHA256().encrypt(user.getName());
        } catch (Exception exception) {
            throw new BaseException(NAME_ENCRYPTION_ERROR);
        }
        //생일 암호화
        String encryptBirthday;
        try {
            encryptBirthday = new SHA256().encrypt(user.getBirthday());
        } catch (Exception exception) {
            throw new BaseException(BIRTHDAY_ENCRYPTION_ERROR);
        }
        //암호화
        User encryptUser = user.builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .phone(user.getPhone())
                .name(encryptName)
                .userType(user.getUserType())
                .birthday(encryptBirthday)
                .privacyExpiredAt(user.getPrivacyExpiredAt())
                .build();

        User saveUser = userRepository.save(encryptUser);

        // JWT 발급
        String jwtToken = jwtService.createJwt(saveUser.getId());
        return new PostUserRes(saveUser.getId(), jwtToken);

    }

    public void modifyUserName(Long userId, PatchUserReq patchUserReq) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.updateName(patchUserReq.getName());
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.deleteUser();
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers() {
        List<GetUserRes> getUserResList = userRepository.findAllByState(ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsersByUserId(String userId) {
        List<GetUserRes> getUserResList = userRepository.findAllByUserIdAndState(userId, ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }


    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    @Transactional(readOnly = true)
    public boolean checkUserByUserId(String userId) {
        Optional<User> result = userRepository.findByUserIdAndState(userId, ACTIVE);
        if (result.isPresent()) return true;
        return false;
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) {
        User user = userRepository.findByUserId(postLoginReq.getUserId())
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        // 유저의 계정 상태에 따라 로그인 성공 여부 처리
        switch(user.getState()) {
            case ACTIVE:
                break;
            case INACTIVE:
                throw new BaseException(INACTIVE_USER);
            case BANNED:
                throw new BaseException(BANNED_USER);
            case DELETED:
                throw new BaseException(DELETED_USER);
        }

        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            Long id = user.getId();
            //개인정보 동의 기간 확인
            sendNotice(id);
            String jwt = jwtService.createJwt(id);
            return new PostLoginRes(id,jwt);
        } else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public GetUserRes getUserByEmail(String userId) {
        User user = userRepository.findByUserIdAndState(userId, ACTIVE).orElseThrow(() -> new BaseException(NOT_FIND_USER));
        sendNotice(user.getId());
        return new GetUserRes(user);
    }

    private void sendNotice(Long id){
        User user = userRepository.findByIdAndState(id, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        if(LocalDate.now().isAfter(user.getPrivacyExpiredAt())) {
            log.warn("개인정보 동의 필요함");
        } else {
            log.info("개인정보 동의 유효");
        }
    }
}
