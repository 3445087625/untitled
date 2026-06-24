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
 * 用户Service实现 — 负责登录认证、注册、会话管理
 * <p>
 * 使用 HttpSession 管理登录状态（替代初版 ThreadLocal），
 * 确保同一浏览器会话内多个HTTP请求共享登录信息。
 * </p>
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 获取当前请求的 HttpSession，用于存取登录用户
     */
    private HttpSession getSession() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attrs.getRequest().getSession();
    }

    /**
     * 登录：校验用户名密码，成功后将用户存入 Session
     *
     * @param username 用户名
     * @param password 明文密码（生产环境应使用 BCrypt 加密）
     * @return success=true 时包含 user 对象
     */
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

    /**
     * 注册：检查用户名唯一性后插入新用户
     *
     * @param user 新用户实体（含 username/password/realName/roleType）
     * @return true=注册成功，false=用户名已存在
     */
    @Override
    public boolean register(SysUser user) {
        SysUser exist = sysUserMapper.selectByUsername(user.getUsername());
        if (exist != null) {
            return false;
        }
        sysUserMapper.insert(user);
        return true;
    }

    /**
     * 获取当前登录用户（从 Session 中读取）
     *
     * @return 登录用户对象，未登录返回 null
     */
    @Override
    public SysUser getLoginUser() {
        try {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
            return (SysUser) session.getAttribute("loginUser");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 退出登录：清除 Session 中的用户信息
     */
    @Override
    public void logout() {
        getSession().removeAttribute("loginUser");
    }

    /**
     * 修改密码：校验原密码正确后更新
     */
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
