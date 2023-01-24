package com.example.demo.src.admin;

import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.*;
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
     * 회원 관리 API
     * [GET] /admin/users
     * 회원 번호 및 아이디 검색 조회 API
     * [GET] /admin/users?pageIndex=
     * @return BaseResponse<List<GetLogRes>>
     */
    @ResponseBody
    @GetMapping("/users")
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam int pageIndex, @Valid @RequestBody GetUserReq getUserReq) {
        List<GetUserRes> getUserResList = adminService.getUserList(pageIndex, getUserReq);

        return new BaseResponse<>(getUserResList);
    }
}
