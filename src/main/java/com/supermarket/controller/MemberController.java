package com.supermarket.controller;

import com.supermarket.common.ResultVo;
import com.supermarket.entity.Member;
import com.supermarket.service.MemberService;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员 Controller — 会员查询 API
 */
@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 根据账号查询会员
     * GET /api/member/lookup?accountNo=M001
     *
     * @return 会员信息（nickname, accountNo, points）或错误提示
     */
    @GetMapping("/lookup")
    public ResultVo<Map<String, Object>> lookup(@RequestParam String accountNo) {
        Member member = memberService.lookupByAccountNo(accountNo);
        if (member == null) {
            return ResultVo.error("会员账号不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("memberId", member.getMemberId());
        data.put("accountNo", member.getAccountNo());
        data.put("nickname", member.getNickname());
        data.put("points", member.getPoints());
        data.put("phone", member.getPhone());
        return ResultVo.success(data);
    }

    /**
     * 注册会员
     * POST /api/member/register
     * Body: {accountNo, nickname?, phone?}
     */
    @PostMapping("/register")
    public ResultVo<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        String accountNo = params.get("accountNo");
        if (accountNo == null || accountNo.trim().isEmpty()) {
            return ResultVo.error("会员账号不能为空");
        }
        Member member = memberService.register(
            accountNo.trim(),
            params.getOrDefault("nickname", "会员用户"),
            params.get("phone")
        );
        if (member == null) {
            return ResultVo.error("会员账号已存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("memberId", member.getMemberId());
        data.put("accountNo", member.getAccountNo());
        data.put("nickname", member.getNickname());
        data.put("points", member.getPoints());
        return ResultVo.success(data);
    }
}
