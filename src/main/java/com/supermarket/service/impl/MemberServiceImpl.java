package com.supermarket.service.impl;

import com.supermarket.entity.Member;
import com.supermarket.mapper.MemberMapper;
import com.supermarket.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 会员Service实现 — 会员查询、积分管理
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    /**
     * 根据会员账号查询会员信息
     *
     * @param accountNo 会员账号
     * @return 会员对象，未找到返回 null
     */
    @Override
    public Member lookupByAccountNo(String accountNo) {
        return memberMapper.selectByAccountNo(accountNo);
    }

    /**
     * 增加积分（消费1元=1积分）
     *
     * @param memberId 会员ID
     * @param points   增加的积分
     */
    @Override
    @Transactional
    public void addPoints(Integer memberId, Integer points) {
        memberMapper.addPoints(memberId, points);
    }

    /**
     * 注册会员（账号 + 默认昵称"会员用户" + 积分0）
     *
     * @param accountNo 会员账号
     * @param nickname  昵称
     * @param phone     手机号（可选）
     * @return 注册成功的会员对象，账号重复返回 null
     */
    @Override
    @Transactional
    public Member register(String accountNo, String nickname, String phone) {
        Member exist = memberMapper.selectByAccountNo(accountNo);
        if (exist != null) {
            return null;
        }
        Member member = new Member();
        member.setAccountNo(accountNo);
        member.setNickname(nickname != null && !nickname.isEmpty() ? nickname : "会员用户");
        member.setPoints(0);
        member.setPhone(phone);
        memberMapper.insert(member);
        return member;
    }
}
