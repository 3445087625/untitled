package com.supermarket.service;

import com.supermarket.entity.SysUser;
import java.util.Map;

/**
 * 用户Service接口
 */
public interface SysUserService {

    /** 登录 */
    Map<String, Object> login(String username, String password);

    /** 注册 */
    boolean register(SysUser user);

    /** 获取当前登录用户 */
    SysUser getLoginUser();

    /** 退出 */
    void logout();

    /** 修改密码 */
    boolean updatePassword(Integer userId, String oldPassword, String newPassword);
}
