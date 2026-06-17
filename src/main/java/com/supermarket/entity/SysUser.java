package com.supermarket.entity;

/**
 * 系统用户实体（收银员/管理员）
 */
public class SysUser {
    private Integer userId;
    private String username;      // 登录账号
    private String password;      // 登录密码
    private String realName;      // 真实姓名
    private Integer roleType;     // 1管理员 2收银员
    private String phone;
    private String createTime;
    private Integer status;       // 1正常 0禁用

    public SysUser() {}

    public SysUser(String username, String password, String realName, Integer roleType) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.roleType = roleType;
        this.status = 1;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public Integer getRoleType() { return roleType; }
    public void setRoleType(Integer roleType) { this.roleType = roleType; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
