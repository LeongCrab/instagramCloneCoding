package com.example.demo.src.user;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.feed.FeedRepository;
import com.example.demo.src.feed.ImageRepository;
import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.feed.entity.Image;
import com.example.demo.src.user.entity.Chat;
import com.example.demo.src.user.entity.Follow;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FeedRepository feedRepository;
    private final JwtService jwtService;
    private final ImageRepository imageRepository;
    private final ChatRepository chatRepository;


    public PostUserRes createUser(PostUserReq postUserReq) {
        //중복 체크
        Optional<User> checkUser = userRepository.findByLoginIdAndState(postUserReq.getLoginId(), ACTIVE);
        if(checkUser.isPresent()){
            throw new BaseException(POST_USERS_EXISTS_LOGIN_ID);
        }

        try {
            String encryptPhone = SHA256.encrypt(postUserReq.getPhone());
            postUserReq.setPhone(encryptPhone);
        } catch (Exception exception) {
            throw new BaseException(PHONE_ENCRYPTION_ERROR);
        }

        try {
            String encryptName = SHA256.encrypt(postUserReq.getName());
            postUserReq.setName(encryptName);
        } catch (Exception exception) {
            throw new BaseException(NAME_ENCRYPTION_ERROR);
        }

        try {
            String encryptPwd = SHA256.encrypt(postUserReq.getPassword());
            postUserReq.setPassword(encryptPwd);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try {
            String encryptBirthday = SHA256.encrypt(postUserReq.getBirthday());
            postUserReq.setBirthday(encryptBirthday);
        } catch (Exception exception) {
            throw new BaseException(BIRTHDAY_ENCRYPTION_ERROR);
        }

        try {
            String encryptBirthYear = SHA256.encrypt(postUserReq.getBirthYear().toString());
            postUserReq.setBirthday(encryptBirthYear);
        } catch (Exception exception) {
            throw new BaseException(BIRTHDAY_ENCRYPTION_ERROR);
        }

        User saveUser = userRepository.save(postUserReq.toEntity());

        return new PostUserRes(saveUser.getId(), saveUser.getLoginId());

    }

    public PostUserRes createOAuthUser(User user) {
        //이름 암호화
        String encryptName;
        try {
            encryptName = SHA256.encrypt(user.getName());
        } catch (Exception exception) {
            throw new BaseException(NAME_ENCRYPTION_ERROR);
        }
        //생일 암호화
        String encryptBirthday;
        try {
            encryptBirthday = SHA256.encrypt(user.getBirthday());
        } catch (Exception exception) {
            throw new BaseException(BIRTHDAY_ENCRYPTION_ERROR);
        }

        //유저 엔티티 암호화
        user.encryptOAuthUser(encryptName, encryptBirthday);

        userRepository.save(user);

        String jwtToken = jwtService.createJwt(user.getId());
        return new PostUserRes(user.getId(), user.getLoginId(), jwtToken);
    }

    public void updatePassword(Long userId, PatchUserReq patchUserReq) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.updatePassword(patchUserReq.getPassword());
    }

    public void updateProfile(Long userId, PatchProfileReq patchProfileReq) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        String profileImage = patchProfileReq.getProfileImage();
        String profileText = patchProfileReq.getProfileText();

        user.updateProfile(profileImage, profileText);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.deleteUser();
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers() {
        return userRepository.findAllByState(ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsersByUserId(String userId) {
        return userRepository.findAllByLoginIdAndState(userId, ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        return new GetUserRes(user);
    }

    @Transactional(readOnly = true)
    public boolean checkUserByLoginId(String loginId) {
        Optional<User> result = userRepository.findByLoginIdAndState(loginId, ACTIVE);

        return result.isPresent();
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) {
        User user = userRepository.findByLoginId(postLoginReq.getLoginId())
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        // 계정 상태에 따라 로그인 성공 여부 처리
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
            encryptPwd = SHA256.encrypt(postLoginReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            Long id = user.getId();
            //개인정보 동의 기간 확인
            sendNotice(id);
            String jwt = jwtService.createJwt(id);
            return new PostLoginRes(postLoginReq.getLoginId(), jwt);
        } else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }
    @Transactional(readOnly = true)
    public GetUserRes getUserByLoginId(String loginId) {
        User user = userRepository.findByLoginIdAndState(loginId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
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
    @Transactional(readOnly = true)
    public boolean existFollow(long followerId, long followingId) {
        Optional<Follow> existFollow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);

        return existFollow.isPresent();
    }

    public void createFollow(Long followerId, Long followingId) {
        if(followerId.equals(followingId)) {
            throw new BaseException(SELF_FOLLOW);
        }
        User follower = userRepository.findByIdAndState(followerId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        User following = userRepository.findByIdAndState(followingId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        followRepository.save(follow);
    }
    public void toggleFollow(long followerId, long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new BaseException(NOT_FIND_FOLLOW));
        follow.toggle();
    }
    @Transactional(readOnly = true)
    public GetMyPageRes getMyPage(long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        String loginId = user.getLoginId();
        int feeds = feedRepository.countByUserIdAndState(userId, ACTIVE);
        int followers = followRepository.countByFollowingIdAndState(userId, ACTIVE);
        int followings = followRepository.countByFollowerIdAndState(userId, ACTIVE);
        String profileText = user.getProfileText();
        String profileImage = user.getProfileImage();

        return new GetMyPageRes(loginId, feeds, followers, followings, profileText, profileImage);

    }
    @Transactional(readOnly = true)
    public GetMyPageFeedsRes getMyPageFeeds(long userId, int pageIndex) {
        final int size = 9;

        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Feed> feedPage = feedRepository.findByUserIdAndState(userId, ACTIVE, pageRequest);

        boolean hasNext = feedPage.hasNext();
        List<String> thumbnailList = new ArrayList<>();

        List<Long> feedIdList = feedPage.stream()
                .map(Feed::getId)
                .collect(Collectors.toList());

        for(Long feedId : feedIdList){
            try {
                List<Image> imageList = imageRepository.findAllByFeedIdAndState(feedId, ACTIVE);

                if(!imageList.isEmpty()) {
                    String thumbnail = imageList.get(0).getUrl();
                    thumbnailList.add(thumbnail);
                }
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
        }
        return new GetMyPageFeedsRes(hasNext, thumbnailList);
    }

    public void createChat(Long senderId, Long receiverId, PostChatReq postChatReq){
        User sender = userRepository.findByIdAndState(senderId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        User receiver = userRepository.findByIdAndState(receiverId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        String text = postChatReq.getText();

        Chat chat = Chat.builder()
                .sender(sender)
                .receiver(receiver)
                .text(text)
                .build();
        chatRepository.save(chat);
    }

    @Transactional(readOnly = true)
    public List<GetChatRes> getChatList(int size, int pageIndex, Long jwtId, Long receiverId) {
        User sender = userRepository.findByIdAndState(jwtId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        User receiver = userRepository.findByIdAndState(receiverId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        try{
            Page<Chat> chatPage = chatRepository.findChat(jwtId, receiverId, ACTIVE, pageRequest);
            Page<GetChatRes> dtoPage = chatPage.map(this::toDto);
            System.out.println(dtoPage.getContent());
            return dtoPage.getContent();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    private GetChatRes toDto(Chat chat){
        Long chatId = chat.getId();
        Long senderId = chat.getSender().getId();
        Long receiverId = chat.getReceiver().getId();
        String text = chat.getText();
        LocalDateTime createdAt = chat.getCreatedAt();

        return new GetChatRes(chatId, senderId, receiverId, text, createdAt);
    }

}
