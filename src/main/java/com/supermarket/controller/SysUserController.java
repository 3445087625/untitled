package com.supermarket.controller;

import com.supermarket.common.ResultVo;
import com.supermarket.entity.SysUser;
import com.supermarket.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户Controller - 处理登录/注册/退出请求
 */
@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    /** 登录 */
    @PostMapping("/login")
    public ResultVo<?> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return ResultVo.error("用户名和密码不能为空");
        }
        Map<String, Object> result = sysUserService.login(username, password);
        if ((Boolean) result.get("success")) {
            return ResultVo.success("登录成功", result.get("user"));
        }
        return ResultVo.error((String) result.get("msg"));
    }

    /** 注册 */
    @PostMapping("/register")
    public ResultVo<?> register(@RequestBody SysUser user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResultVo.error("用户名和密码不能为空");
        }
        boolean success = sysUserService.register(user);
        if (success) {
            return ResultVo.success("注册成功", null);
        }
        return ResultVo.error("用户名已存在");
    }

    /** 获取当前登录用户 */
    @GetMapping("/current")
    public ResultVo<SysUser> current() {
        SysUser user = sysUserService.getLoginUser();
        if (user == null) {
            return ResultVo.error(401, "未登录");
        }
        return ResultVo.success(user);
    }

    /** 退出 */
    @PostMapping("/logout")
    public ResultVo<?> logout() {
        sysUserService.logout();
        return ResultVo.success("已退出", null);
    }

    /** 修改密码 */
    @PostMapping("/password")
    public ResultVo<?> updatePassword(@RequestBody Map<String, String> params) {
        SysUser user = sysUserService.getLoginUser();
        if (user == null) {
            return ResultVo.error(401, "未登录");
        }
        boolean success = sysUserService.updatePassword(
                user.getUserId(),
                params.get("oldPassword"),
                params.get("newPassword"));
        if (success) {
            return ResultVo.success("密码修改成功", null);
        }
        return ResultVo.error("原密码错误");
    }
}
