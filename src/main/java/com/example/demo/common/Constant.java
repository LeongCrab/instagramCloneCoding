package com.example.demo.common;

public class Constant {

    public enum State {
        ACTIVE, INACTIVE
    }
    public enum UserState {
        ACTIVE, INACTIVE, DELETED, BANNED
    }

    public enum FeedState {
        ACTIVE, INACTIVE, DELETED, BANNED
    }

    public enum LoginType {
        ORIGINAL, GOOGLE, KAKAO, NAVER, APPLE
    }

    public enum ReportReason {
        SPAM, SEXUAL, HATRED, VIOLATION, ILLEGAL, BULLYING, INPRP_INFRN, SELF_INJURY, EATING_DISORDER, FRAUD, FAKE_NEWS, DO_NOT_LIKE
    }

    public enum DataType{
        USER, LOGIN, FEED, REPORT, FOLLOW, HERAT, COMMENT
    }

    public enum MethodType {
        CREATE, READ, UPDATE, DELETE
    }
}

