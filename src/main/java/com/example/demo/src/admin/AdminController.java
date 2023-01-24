package com.example.demo.src.admin;

import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.*;
import com.example.demo.src.user.model.PatchUserReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;


    /**
     * 회원 조회 API
     * [GET] /admin/users
     * 회원 번호 및 아이디 검색 조회 API
     * [GET] /admin/users?pageIndex=
     *
     * @return BaseResponse<List < GetUserRes>>
     */
    @ResponseBody
    @GetMapping("/users")
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam int pageIndex, @Valid @RequestBody GetUserReq getUserReq) {
        List<GetUserRes> getUserResList = adminService.getUsers(pageIndex, getUserReq);

        return new BaseResponse<>(getUserResList);
    }


    /**
     * 회원 1명 아이디 검색 조회 API
     * [GET] /admin/user/:userId
     *
     * @return BaseResponse<GetUserInfoRes>
     */
    @ResponseBody
    @GetMapping("/user/{userId}")
    public BaseResponse<GetUserInfoRes> getUser(@PathVariable("userId") Long userId) {
        GetUserInfoRes getUserInfoRes = adminService.getUser(userId);

        return new BaseResponse<>(getUserInfoRes);
    }


    /**
     * 회원 정지 API
     * [PATCH] /admin/user/ban/:userId
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/user/ban/{userId}")
    public BaseResponse<String> banUser(@PathVariable("userId") Long userId) {
        adminService.banUser(userId);

        String result = "회원 정지 완료";
        return new BaseResponse<>(result);
    }
}

