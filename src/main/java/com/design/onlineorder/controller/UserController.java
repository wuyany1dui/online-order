package com.design.onlineorder.controller;

import com.design.onlineorder.entity.User;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.service.UserService;
import com.design.onlineorder.utils.UserUtils;
import com.design.onlineorder.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author Created by DrEAmSs on 2022-04-25 17:51
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @ApiParam("用户实体类") User user) {
        userService.register(user);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("普通用户登录")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @ApiParam("账号密码对象") UserLoginVo data) {
        return ResponseEntity.ok(userService.login(data));
    }

    @ApiOperation("商家用户登录")
    @PostMapping("/storeLogin")
    public ResponseEntity<?> storeLogin(@RequestBody @ApiParam("账号密码对象") UserLoginVo data) {
        return ResponseEntity.ok(userService.storeLogin(data));
    }

    @ApiOperation("当前用户名")
    @GetMapping("/username")
    public ResponseEntity<?> currentUsername() {
        return ResponseEntity.ok(UserUtils.getCurrentUser().getUsername());
    }

    @ApiOperation("当前用户信息")
    @GetMapping("/userInfo")
    public ResponseEntity<?> currentUserInfo() {
        return ResponseEntity.ok(UserUtils.getCurrentUser());
    }

    @ApiOperation("上传用户头像")
    @PostMapping("/uploadAvatar")
    public ResponseEntity<?> uploadAvatar(MultipartFile multipartFile) {
        userService.uploadAvatar(multipartFile);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/modify")
    public ResponseEntity<?> modifyUserInfo(@RequestBody @ApiParam("用户Vo类") UserVo userVo) {
        userService.modifyUserInfo(userVo);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("修改用户密码")
    @PostMapping("/modifyPwd")
    public ResponseEntity<?> modifyUserPassword(@RequestBody ModifyPasswordVo modifyPasswordVo) {
        userService.modifyUserPassword(modifyPasswordVo);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }

    @ApiOperation("查询用户列表")
    @PostMapping("/queryList")
    public ResponseEntity<?> queryList(@RequestBody UserQueryVo userQueryVo) {
        return ResponseEntity.ok(userService.queryList(userQueryVo));
    }

    @ApiOperation("修改用户权限")
    @PostMapping ("/modifyLevel")
    public ResponseEntity<?> modifyLevel(@RequestBody ModifyLevelVo modifyLevelVo) {
        userService.modifyLevel(modifyLevelVo);
        return ResponseEntity.ok(ResultEnum.SUCCESS.getLabel());
    }
}
