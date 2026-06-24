package com.supermarket.service.impl;

import com.supermarket.entity.SysUser;
import com.supermarket.mapper.SysUserMapper;
import com.supermarket.service.SysUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户Service实现 - 使用HttpSession管理登录状态
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    private HttpSession getSession() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attrs.getRequest().getSession();
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        SysUser user = sysUserMapper.selectByUsernameAndPassword(username, password);
        if (user == null) {
            result.put("success", false);
            result.put("msg", "用户名或密码错误");
            return result;
        }
        getSession().setAttribute("loginUser", user);
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
        try {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
            return (SysUser) session.getAttribute("loginUser");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void logout() {
        getSession().removeAttribute("loginUser");
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
