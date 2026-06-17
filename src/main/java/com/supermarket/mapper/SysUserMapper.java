package com.supermarket.mapper;

import com.supermarket.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
public interface SysUserMapper {

    /** 根据用户名和密码查询用户（登录） */
    SysUser selectByUsernameAndPassword(@Param("username") String username,
                                         @Param("password") String password);

    /** 根据用户名查询（注册时检查重复） */
    SysUser selectByUsername(@Param("username") String username);

    /** 新增用户（注册） */
    int insert(SysUser user);

    /** 根据ID查询 */
    SysUser selectById(@Param("userId") Integer userId);

    /** 查询所有收银员 */
    List<SysUser> selectAll();

    /** 修改密码 */
    int updatePassword(@Param("userId") Integer userId, @Param("password") String password);

    /** 修改用户状态 */
    int updateStatus(@Param("userId") Integer userId, @Param("status") Integer status);
}
