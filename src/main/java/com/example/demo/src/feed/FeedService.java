package com.example.demo.src.feed;


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

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
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
        User user = userRepository.findByIdAndState(jwtId, ACTIVE)
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
            Page<Feed> feedPage = feedRepository.findAllByState(ACTIVE, pageRequest);
            Page<GetFeedRes> dtoPage = feedPage.map(this::makeGetFeedRes);

            return dtoPage.getContent();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    @Transactional(readOnly = true)
    public List<GetFeedRes> getFeedsByLoginId(int size, int pageIndex, String loginId) {
        User user = userRepository.findByLoginIdAndState(loginId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        try{
            PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Feed> feedPage = feedRepository.findByUserIdAndState(user.getId(), ACTIVE, pageRequest);
            Page<GetFeedRes> dtoPage = feedPage.map(this::makeGetFeedRes);

            return dtoPage.getContent();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    private List<String> getImageList(long feedId) {
        List<Image> imageList = imageRepository.findAllByFeedIdAndState(feedId, ACTIVE);
        return imageList.stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    private List<String> getVideoList(long feedId) {
        List<Video> videoList = videoRepository.findAllByFeedIdAndState(feedId, ACTIVE);
        return videoList.stream()
                .map(Video::getUrl)
                .collect(Collectors.toList());
    }

    public void modifyFeed(long jwtId, long feedId, PatchFeedReq patchFeedReq) throws BaseException{
        Feed feed = feedRepository.findByIdAndState(feedId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));
        if(feed.getUser().getId().equals(jwtId)){
            feed.modifyFeed(patchFeedReq.getContent());
        } else {
            throw new BaseException(INVALID_USER_JWT);
        }
    }

    public void deleteFeed(long jwtId, long feedId) throws BaseException{
        Feed feed = feedRepository.findByIdAndState(feedId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));
        if(feed.getUser().getId().equals(jwtId)){
            feed.deleteFeed();
        } else {
            throw new BaseException(INVALID_USER_JWT);
        }
    }


    public boolean existHeart(long jwtId, long feedId) {
        Optional<Heart> existHeart = heartRepository.findByUserIdAndFeedId(jwtId, feedId);

        return existHeart.isPresent();
    }

    public void createHeart(long jwtId, long feedId) {
        User user = userRepository.findByIdAndState(jwtId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        Feed feed = feedRepository.findByIdAndState(feedId, ACTIVE)
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
        User user = userRepository.findByIdAndState(jwtId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        Feed feed = feedRepository.findByIdAndState(feedId, ACTIVE)
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
        return heartRepository.countByFeedIdAndState(feedId, ACTIVE);
    }
    @Transactional(readOnly = true)
    private int getComments(long feedId) {
        return commentRepository.countByFeedIdAndState(feedId, ACTIVE);
    }

    private GetFeedRes makeGetFeedRes(Feed feed) {
        Long feedId = feed.getId();
        int hearts = getHearts(feedId);
        int comments = getComments(feedId);
        List<String> imageList = getImageList(feedId);
        List<String> videoList = getVideoList(feedId);

        return new GetFeedRes(feed, hearts, comments, imageList, videoList);
    }
    @Transactional(readOnly = true)
    public List<GetCommentRes> getCommentsByFeedId(long feedId) {
        List<Comment> commentList = commentRepository.findAllByIdAndState(feedId, ACTIVE);

        return  commentList.stream().map(comment -> new GetCommentRes(
                comment.getId(),
                comment.getUser().getLoginId(),
                comment.getContent()
        )).collect(Collectors.toList());
    }

    public void patchComment(long jwtId, long commentId, PostCommentReq postCommentReq) {
        Comment comment = commentRepository.findByUserIdAndIdAndState(jwtId, commentId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));

        comment.patchComment(postCommentReq.getContent());
    }

    public void deleteComment(long jwtId, long commentId) {
        Comment comment = commentRepository.findByUserIdAndIdAndState(jwtId, commentId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));

        comment.deleteComment();
    }

    public void createReport(long feedId, PostReportReq postReportReq) {
        Feed feed = feedRepository.findByIdAndState(feedId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));

        Report report = Report.builder()
                .feed(feed)
                .reportReason(postReportReq.getReportReason())
                .build();

        reportRepository.save(report);
    }
}
