package com.supermarket.controller;

import com.supermarket.common.ResultVo;
import com.supermarket.entity.SalesDetail;
import com.supermarket.entity.SalesOrder;
import com.supermarket.entity.SysUser;
import com.supermarket.service.SalesService;
import com.supermarket.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

/**
 * 销售Controller
 */
@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;
    @Autowired
    private SysUserService sysUserService;

    /** 提交订单 */
    @PostMapping("/submit")
    public ResultVo<?> submit(@RequestBody Map<String, Object> params) {
        SysUser user = sysUserService.getLoginUser();
        if (user == null) {
            return ResultVo.error(401, "未登录");
        }

        // 解析订单信息
        SalesOrder order = new SalesOrder();
        order.setUserId(user.getUserId());
        // 金额从请求中解析
        if (params.get("paidAmount") != null) {
            order.setPaidAmount(new java.math.BigDecimal(params.get("paidAmount").toString()));
        }
        if (params.get("discountAmount") != null) {
            order.setDiscountAmount(new java.math.BigDecimal(params.get("discountAmount").toString()));
        }
        if (params.get("paymentMethod") != null) {
            order.setPaymentMethod(Integer.parseInt(params.get("paymentMethod").toString()));
        } else {
            order.setPaymentMethod(1);
        }
        if (params.get("remark") != null) {
            order.setRemark((String) params.get("remark"));
        }

        // 解析明细列表
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) params.get("items");
        java.util.List<SalesDetail> details = new java.util.ArrayList<>();
        for (Map<String, Object> item : items) {
            SalesDetail detail = new SalesDetail();
            detail.setProductId(Integer.parseInt(item.get("productId").toString()));
            detail.setQuantity(Integer.parseInt(item.get("quantity").toString()));
            details.add(detail);
        }

        Map<String, Object> result = salesService.submitOrder(order, details);
        if ((Boolean) result.get("success")) {
            return ResultVo.success((String) result.get("msg"), result);
        }
        return ResultVo.error((String) result.get("msg"));
    }

    /** 订单分页 */
    @GetMapping("/page")
    public ResultVo<Map<String, Object>> page(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        return ResultVo.success(salesService.getOrderPage(keyword, page, limit));
    }

    /** 订单详情 */
    @GetMapping("/detail/{id}")
    public ResultVo<Map<String, Object>> detail(@PathVariable Integer id) {
        return ResultVo.success(salesService.getOrderDetail(id));
    }

    /** 今日销售汇总 */
    @GetMapping("/today-summary")
    public ResultVo<Map<String, Object>> todaySummary() {
        return ResultVo.success(salesService.getTodaySummary());
    }

    /** 销售日报（查询视图 v_sales_summary） */
    @GetMapping("/report")
    public ResultVo<List<Map<String, Object>>> report() {
        return ResultVo.success(salesService.getSalesReport());
    }

    /** 退款 */
    @PostMapping("/refund/{id}")
    public ResultVo<?> refund(@PathVariable Integer id) {
        boolean ok = salesService.refund(id);
        if (ok) {
            return ResultVo.success("退款成功", null);
        }
        return ResultVo.error("退款失败：订单不存在或已退款");
    }
}
