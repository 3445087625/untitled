package com.supermarket.service;

import com.supermarket.entity.Member;

/**
 * 会员Service接口
 */
public interface MemberService {

    /** 根据账号查询会员 */
    Member lookupByAccountNo(String accountNo);

    /** 增加积分（消费获得积分） */
    void addPoints(Integer memberId, Integer points);

    /** 注册会员 */
    Member register(String accountNo, String nickname, String phone);
}
