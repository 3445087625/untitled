package com.supermarket.service.impl;

import com.supermarket.entity.SysUser;
import com.supermarket.mapper.SysUserMapper;
import com.supermarket.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户Service实现
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    /** 当前登录用户（简化：单机版使用ThreadLocal模拟Session） */
    private static final ThreadLocal<SysUser> LOGIN_USER = new ThreadLocal<>();

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        SysUser user = sysUserMapper.selectByUsernameAndPassword(username, password);
        if (user == null) {
            result.put("success", false);
            result.put("msg", "用户名或密码错误");
            return result;
        }
        LOGIN_USER.set(user);
        result.put("success", true);
        result.put("msg", "登录成功");
        result.put("user", user);
        return result;
    }

    @Override
    public boolean register(SysUser user) {
        SysUser exist = sysUserMapper.selectByUsername(user.getUsername());
        if (exist != null) {
            return false;
        }
        sysUserMapper.insert(user);
        return true;
    }

    @Override
    public SysUser getLoginUser() {
        return LOGIN_USER.get();
    }

    @Override
    public void logout() {
        LOGIN_USER.remove();
    }

    @Override
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || !user.getPassword().equals(oldPassword)) {
            return false;
        }
        sysUserMapper.updatePassword(userId, newPassword);
        return true;
    }
}
