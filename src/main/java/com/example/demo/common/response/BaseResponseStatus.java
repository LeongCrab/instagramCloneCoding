package com.example.demo.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    /**
     * 400 : Request, Response 오류
     */

    POST_USERS_EXISTS_LOGIN_ID(false,HttpStatus.BAD_REQUEST.value(),"중복된 아이디입니다."),
    VALIDATION_ERROR(false, HttpStatus.BAD_REQUEST.value(), null),
    TYPE_MISMATCH_ERROR(false, HttpStatus.BAD_REQUEST.value(), "쿼리에 올바른 타입의 값을 입력해주세요"),
    JSON_PARSE_ERROR(false, HttpStatus.BAD_REQUEST.value(), "JSON에 올바른 타입의 값을 입력해주세요"),
    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),
    FAILED_TO_LOGIN(false,HttpStatus.NOT_FOUND.value(),"없는 아이디거나 비밀번호가 틀렸습니다."),
    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,HttpStatus.FORBIDDEN.value(),"권한이 없는 유저의 접근입니다."),
    NOT_FIND_USER(false,HttpStatus.NOT_FOUND.value(),"일치하는 유저가 없습니다."),
    NOT_FIND_FOLLOW(false,HttpStatus.NOT_FOUND.value(),"일치하는 팔로우가 없습니다."),
    NOT_FIND_FEED(false,HttpStatus.NOT_FOUND.value(),"일치하는 게시물이 없습니다."),
    NOT_FIND_CHAT(false,HttpStatus.NOT_FOUND.value(),"일치하는 채팅이 없습니다."),
    SELF_FOLLOW(false, HttpStatus.BAD_REQUEST.value(), "자기 자신을 팔로우 할 수 없습니다."),
    NOT_FIND_HEART(false,HttpStatus.NOT_FOUND.value(),"일치하는 좋아요가 없습니다."),
    NOT_FIND_COMMENT(false,HttpStatus.NOT_FOUND.value(),"일치하는 댓글이 없습니다."),
    INACTIVE_USER(false,HttpStatus.BAD_REQUEST.value(), "휴면 계정입니다"),
    BANNED_USER(false,HttpStatus.BAD_REQUEST.value(), "차단된 계정입니다"),
    DELETED_USER(false,HttpStatus.BAD_REQUEST.value(), "탈퇴한 계정입니다"),

    INVALID_OAUTH_TYPE(false, HttpStatus.BAD_REQUEST.value(), "알 수 없는 소셜 로그인 형식입니다."),



    /**
     * 500 :  Database, Server 오류
     */
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다."),
    NAME_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "이름 암호화에 실패하였습니다."),
    PHONE_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "휴대폰 번호 암호화에 실패하였습니다."),
    BIRTHDAY_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "생일 암호화에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 복호화에 실패하였습니다."),
    MODIFY_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저네임 수정 실패"),
    DELETE_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저 삭제 실패"),
    MODIFY_FAIL_MEMO(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"메모 수정 실패"),

    UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러가 발생했습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
