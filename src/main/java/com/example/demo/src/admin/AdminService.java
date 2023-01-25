package com.example.demo.src.admin;

import com.example.demo.common.Constant.LoginType;
import com.example.demo.common.Constant.DataType;
import com.example.demo.common.Constant.MethodType;
import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.entity.Log;
import com.example.demo.src.admin.model.*;
import com.example.demo.src.feed.CommentRepository;
import com.example.demo.src.feed.FeedRepository;
import com.example.demo.src.feed.entity.Comment;
import com.example.demo.src.feed.entity.Feed;
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
import java.util.Optional;


import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final LogRepository logRepository;
    private final CommentRepository commentRepository;
    private final AES128 aes128;

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers(int pageIndex, GetUserReq getUserReq){
        final int size = 10;
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> userPage;
        if(getUserReq == null){
            userPage = userRepository.findAll(pageRequest);
        } else {
            String userName = encryptName(getUserReq.getUserName());
            Long userId = getUserReq.getUserId();
            State state = modifyState(getUserReq.getState());
            String createdAt = getUserReq.getCreatedAt();

            userPage = userRepository.findUsers(userName, userId, state, createdAt, pageRequest);
        }

        return userPage.map(GetUserRes::new).getContent();
    }

    private State modifyState(String state) {
        if(state == null) {
            return null;
        }
        try {
           return State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new BaseException(ENUM_ERROR);
        }
    }

    private String encryptName(String name) {
        try{
            return aes128.encrypt(name);
        } catch (Exception exception){
            throw new BaseException(NAME_ENCRYPTION_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public GetUserInfoRes getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        LoginType loginType = user.getLoginType();
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

    @Transactional(readOnly = true)
    private String getLastLogin(User user){
        return logRepository.findFirstByDataTypeAndUserIdOrderByCreatedAtDesc(DataType.LOGIN, user.getId())
                .map(log -> log.getCreatedAt().toString())
                .orElse(null);

    }

    public void banUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.banUser();
    }

    @Transactional(readOnly = true)
    public List<GetFeedRes> getFeeds(int pageIndex, GetFeedReq getFeedReq) {
        final int size = 10;
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Feed> feedPage;
        if(getFeedReq == null) {
            feedPage = feedRepository.findAll(pageRequest);
        } else {
            String loginId = getFeedReq.getLoginId();
            State state = modifyState(getFeedReq.getState());
            String createdAt = getFeedReq.getCreatedAt();

            feedPage = feedRepository.findFeeds(loginId, state, createdAt, pageRequest);
        }

        return feedPage.map(GetFeedRes::new).getContent();
    }

    @Transactional(readOnly = true)
    public GetFeedInfoRes getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));

        List<Comment> commentList = commentRepository.findAllByFeedId(feed.getId());

        return new GetFeedInfoRes(feed, commentList);
    }

    public String banFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));

        List<Comment> commentList = commentRepository.findAllByFeedId(feed.getId());
        for(Comment comment: commentList) {
            comment.deleteComment();
        }

        feed.banFeed();
        return feed.getId().toString() + "번 게시글과 관련 댓글 삭제 완료";
    }
}