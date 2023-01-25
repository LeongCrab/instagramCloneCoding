package com.example.demo.src.feed;


import com.example.demo.common.Constant.State;
import com.example.demo.common.exceptions.BaseException;

import com.example.demo.src.feed.model.*;
import com.example.demo.src.feed.entity.*;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.response.BaseResponseStatus.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FeedService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;
    private final HeartRepository heartRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;


    public PostFeedRes createFeed(Long jwtId, PostFeedReq postFeedReq){
        User user = userRepository.findByIdAndState(jwtId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        Feed saveFeed = feedRepository.save(postFeedReq.toEntity(user));

        createImage(saveFeed, postFeedReq);
        createVideo(saveFeed, postFeedReq);

        return new PostFeedRes(saveFeed.getId());
    }

    private void createImage(Feed feed, PostFeedReq postFeedReq) {
        List<String> imageList = postFeedReq.getImageList();
        for(String url : imageList){
            Image image = Image.builder()
                    .url(url)
                    .feed(feed)
                    .build();
            imageRepository.save(image);
            log.info("추가된 사진 id :" + image.getId());
        }
    }

    private void createVideo(Feed feed, PostFeedReq postFeedReq) {
        List<String> videoList = postFeedReq.getVideoList();
        for(String url : videoList){
            Video video = Video.builder()
                    .url(url)
                    .feed(feed)
                    .build();
            videoRepository.save(video);
            log.info("추가된 영상 id :" + video.getId());
        }
    }
    @Transactional(readOnly = true)
    public List<GetFeedRes> getFeeds(int size, int pageIndex) throws BaseException{
        try{
            PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
            Page<Feed> feedPage = feedRepository.findAllByState(State.ACTIVE, pageRequest);
            Page<GetFeedRes> dtoPage = feedPage.map(this::toDto);

            return dtoPage.getContent();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    @Transactional(readOnly = true)
    public List<GetFeedRes> getFeedsByLoginId(int size, int pageIndex, Long userId) {
        User user = userRepository.findByIdAndState(userId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        try{
            PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Feed> feedPage = feedRepository.findByUserIdAndState(user.getId(), State.ACTIVE, pageRequest);
            Page<GetFeedRes> dtoPage = feedPage.map(this::toDto);

            return dtoPage.getContent();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private GetFeedRes toDto(Feed feed) {
        Long feedId = feed.getId();
        int hearts = getHearts(feedId);
        int comments = getComments(feedId);
        List<String> imageList = getImageList(feedId);
        List<String> videoList = getVideoList(feedId);

        return new GetFeedRes(feed, hearts, comments, imageList, videoList);
    }

    @Transactional(readOnly = true)
    private List<String> getImageList(long feedId) {
        List<Image> imageList = imageRepository.findAllByFeedIdAndState(feedId, State.ACTIVE);
        return imageList.stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    private List<String> getVideoList(long feedId) {
        List<Video> videoList = videoRepository.findAllByFeedIdAndState(feedId, State.ACTIVE);
        return videoList.stream()
                .map(Video::getUrl)
                .collect(Collectors.toList());
    }

    public void modifyFeed(long jwtId, long feedId, PatchFeedReq patchFeedReq) throws BaseException{
        Feed feed = feedRepository.findByIdAndState(feedId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));
        if(feed.getUser().getId().equals(jwtId)){
            feed.modifyFeed(patchFeedReq.getContent());
        } else {
            throw new BaseException(INVALID_USER_JWT);
        }
    }

    public void deleteFeed(long jwtId, long feedId) throws BaseException{
        Feed feed = feedRepository.findByIdAndState(feedId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));
        if(feed.getUser().getId().equals(jwtId)){
            feed.deleteFeed();
        } else {
            throw new BaseException(INVALID_USER_JWT);
        }
    }

    @Transactional(readOnly = true)
    public boolean existHeart(long jwtId, long feedId) {
        Optional<Heart> existHeart = heartRepository.findByUserIdAndFeedId(jwtId, feedId);

        return existHeart.isPresent();
    }

    public void createHeart(long jwtId, long feedId) {
        User user = userRepository.findByIdAndState(jwtId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        Feed feed = feedRepository.findByIdAndState(feedId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));

        Heart heart = Heart.builder()
                .user(user)
                .feed(feed)
                .build();
        heartRepository.save(heart);
    }

    public void toggleHeart(long jwtId, long feedId) {
        Heart heart = heartRepository.findByUserIdAndFeedId(jwtId, feedId)
                .orElseThrow(() -> new BaseException(NOT_FIND_HEART));
        heart.toggle();
    }

    public void createComment(long jwtId, long feedId, PostCommentReq postCommentReq) {
        User user = userRepository.findByIdAndState(jwtId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        Feed feed = feedRepository.findByIdAndState(feedId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));

       Comment comment = Comment.builder()
               .user(user)
               .feed(feed)
               .content(postCommentReq.getContent())
               .build();
        commentRepository.save(comment);
    }
    @Transactional(readOnly = true)
    private int getHearts(long feedId) {
        return heartRepository.countByFeedIdAndState(feedId, State.ACTIVE);
    }
    @Transactional(readOnly = true)
    private int getComments(long feedId) {
        return commentRepository.countByFeedIdAndState(feedId, State.ACTIVE);
    }


    @Transactional(readOnly = true)
    public List<GetCommentRes> getCommentsByFeedId(long feedId) {
        List<Comment> commentList = commentRepository.findAllByIdAndState(feedId, State.ACTIVE);

        return  commentList.stream().
                map(GetCommentRes::new)
                .collect(Collectors.toList());
    }

    public void patchComment(long jwtId, long commentId, PostCommentReq postCommentReq) {
        Comment comment = commentRepository.findByUserIdAndIdAndState(jwtId, commentId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));

        comment.patchComment(postCommentReq.getContent());
    }

    public void deleteComment(long jwtId, long commentId) {
        Comment comment = commentRepository.findByUserIdAndIdAndState(jwtId, commentId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));

        comment.deleteComment();
    }

    public void createFeedReport(long feedId, PostReportReq postReportReq) {
        Feed feed = feedRepository.findByIdAndState(feedId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));

        try {
            Report report = Report.builder()
                    .feed(feed)
                    .reportReason(postReportReq.getReportReason())
                    .build();
            reportRepository.save(report);
        } catch (Exception exception) {
            throw new BaseException(ENUM_ERROR);
        }

        deleteReportedFeed(feed);
    }

    private void deleteReportedFeed(Feed feed) {
        final int cut = 10;
        long feedId = feed.getId();
        int reports = reportRepository.countByFeedIdAndState(feedId, State.ACTIVE);
        if(reports > cut) {
            feed.hideFeed();
        }
    }

    public void createCommentReport(long commentId, PostReportReq postReportReq) {
        Comment comment = commentRepository.findByIdAndState(commentId, State.ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));

        try {
            Report report = Report.builder()
                    .comment(comment)
                    .reportReason(postReportReq.getReportReason())
                    .build();
            reportRepository.save(report);
        } catch (Exception exception) {
            throw new BaseException(ENUM_ERROR);
        }

        deleteReportedComment(comment);
    }

    private void deleteReportedComment(Comment comment) {
        final int cut = 10;
        long commentId = comment.getId();
        int reports = reportRepository.countByCommentIdAndState(commentId, State.ACTIVE);
        if(reports > cut) {
            comment.hideComment();
        }
    }


}
