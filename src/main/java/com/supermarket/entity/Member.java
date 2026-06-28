package com.supermarket.entity;

/**
 * 会员实体 — 映射 member 表
 */
public class Member {
    private Integer memberId;
    private String accountNo;       // 会员账号
    private String nickname;        // 昵称
    private Integer points;         // 积分
    private String phone;           // 手机号
    private String createTime;      // 注册时间
    private Integer status;         // 状态: 1正常 0禁用

    public Member() {}

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
