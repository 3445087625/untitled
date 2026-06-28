package com.supermarket.mapper;

import com.supermarket.entity.Member;
import org.apache.ibatis.annotations.Param;

/**
 * 会员Mapper接口
 */
public interface MemberMapper {

    /** 根据账号查询会员 */
    Member selectByAccountNo(@Param("accountNo") String accountNo);

    /** 根据ID查询 */
    Member selectById(@Param("memberId") Integer memberId);

    /** 增加积分 */
    int addPoints(@Param("memberId") Integer memberId, @Param("points") Integer points);

    /** 注册会员 */
    int insert(Member member);
}
