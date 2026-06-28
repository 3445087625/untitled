package com.supermarket.service.impl;

import com.supermarket.entity.Product;
import com.supermarket.entity.SalesDetail;
import com.supermarket.entity.SalesOrder;
import com.supermarket.mapper.MemberMapper;
import com.supermarket.mapper.ProductMapper;
import com.supermarket.mapper.SalesDetailMapper;
import com.supermarket.mapper.SalesOrderMapper;
import com.supermarket.service.SalesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售Service实现 — 核心业务：订单提交（事务）、查询、退款
 * <p>
 * submitOrder 是整个系统最核心的业务方法，使用 @Transactional 保证
 * "订单保存 → 明细保存 → 库存扣减" 三个操作的原子性。
 * 订单编号格式：SO + yyyyMMddHHmmss（如 SO20260624143001）。
 * </p>
 */
@Service
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private SalesDetailMapper salesDetailMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MemberMapper memberMapper;

    /**
     * 提交销售订单（事务方法）
     * <p>
     * 五步流程：
     * 1. 生成订单编号 (SO + 时间戳)
     * 2. 遍历明细，校验商品存在 & 库存充足，计算小计和总金额
     * 3. 插入 sales_order 主记录
     * 4. 批量插入 sales_detail 明细
     * 5. 扣减 product 库存
     * </p>
     *
     * @param order   订单主信息（userId/paymentMethod 需预先设置）
     * @param details 明细列表（每个元素含 productId/quantity）
     * @return success=true 时包含 orderId 和 orderNo
     */
    @Override
    @Transactional  // 事务：保证订单和明细、库存、积分要么全成功，要么全回滚
    public Map<String, Object> submitOrder(SalesOrder order, List<SalesDetail> details, Integer redeemPoints) {
        Map<String, Object> result = new HashMap<>();

        // 1. 生成订单编号
        String orderNo = "SO" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        order.setOrderNo(orderNo);

        // 2. 计算总金额
        BigDecimal total = BigDecimal.ZERO;
        for (SalesDetail detail : details) {
            Product p = productMapper.selectById(detail.getProductId());
            if (p == null) {
                result.put("success", false);
                result.put("msg", "商品不存在");
                return result;
            }
            if (p.getStock() < detail.getQuantity()) {
                result.put("success", false);
                result.put("msg", "商品[" + p.getProductName() + "]库存不足，当前库存：" + p.getStock());
                return result;
            }
            BigDecimal subtotal = p.getPrice().multiply(new BigDecimal(detail.getQuantity()));
            detail.setProductName(p.getProductName());
            detail.setPrice(p.getPrice());
            detail.setSubtotal(subtotal);
            total = total.add(subtotal);
        }
        order.setTotalAmount(total);
        if (order.getDiscountAmount() == null) {
            order.setDiscountAmount(BigDecimal.ZERO);
        }

        // 2.5 积分兑换：100积分 = 1元（扣积分，独立记入 redeemed_amount）
        if (redeemPoints != null && redeemPoints > 0 && order.getMemberId() != null) {
            BigDecimal redeemAmount = new BigDecimal(redeemPoints)
                    .divide(new BigDecimal(100), 2, java.math.RoundingMode.DOWN);
            if (redeemAmount.compareTo(BigDecimal.ZERO) > 0) {
                int deducted = memberMapper.deductPoints(order.getMemberId(), redeemPoints);
                if (deducted == 0) {
                    result.put("success", false);
                    result.put("msg", "积分不足，兑换失败");
                    return result;
                }
                order.setRedeemedAmount(redeemAmount);
                result.put("redeemedPoints", redeemPoints);
                result.put("redeemedAmount", redeemAmount);
            }
        }
        if (order.getRedeemedAmount() == null) {
            order.setRedeemedAmount(BigDecimal.ZERO);
        }

        // 折扣不能超过商品总金额，否则截断（防止倒贴）
        if (order.getDiscountAmount().compareTo(total) > 0) {
            order.setDiscountAmount(total);
        }

        // 3. 校验实收金额不能小于应收（应收 = 总金额 - 折扣 - 积分抵扣）
        BigDecimal receivable = total.subtract(order.getDiscountAmount())
                .subtract(order.getRedeemedAmount());
        if (order.getPaidAmount() != null && order.getPaidAmount().compareTo(receivable) < 0) {
            result.put("success", false);
            result.put("msg", "实收金额不足，应收 ¥" + receivable + "，实收 ¥" + order.getPaidAmount());
            return result;
        }

        // 4. 计算找零（现金支付且实收 > 应收时）
        if (order.getPaymentMethod() != null && order.getPaymentMethod() == 1) {
            if (order.getPaidAmount() != null && order.getPaidAmount().compareTo(receivable) > 0) {
                order.setChangeAmount(order.getPaidAmount().subtract(receivable));
            } else {
                order.setChangeAmount(BigDecimal.ZERO);
            }
        } else {
            order.setChangeAmount(BigDecimal.ZERO);
        }

        // 5. 保存订单
        salesOrderMapper.insert(order);

        // 6. 保存明细
        for (SalesDetail detail : details) {
            detail.setOrderId(order.getOrderId());
        }
        salesDetailMapper.batchInsert(details);

        // 7. 扣减库存
        for (SalesDetail detail : details) {
            productMapper.updateStock(detail.getProductId(), -detail.getQuantity());
        }

        // 7. 会员积分（消费1元=1积分，取整）
        if (order.getMemberId() != null && order.getPaidAmount() != null) {
            int points = order.getPaidAmount().intValue();
            if (points > 0) {
                memberMapper.addPoints(order.getMemberId(), points);
            }
        }

        result.put("success", true);
        result.put("msg", "订单提交成功");
        result.put("orderId", order.getOrderId());
        result.put("orderNo", orderNo);
        return result;
    }

    /**
     * 订单分页查询（含收银员姓名、商品种类数）
     *
     * @param keyword 订单号搜索关键词（可选）
     * @param page    页码
     * @param limit   每页条数
     */
    @Override
    public Map<String, Object> getOrderPage(String keyword, Integer page, Integer limit) {
        Map<String, Object> result = new HashMap<>();
        int offset = (page - 1) * limit;
        result.put("list", salesOrderMapper.selectPage(keyword, offset, limit));
        result.put("total", salesOrderMapper.count(keyword));
        return result;
    }

    @Override
    public Map<String, Object> getOrderDetail(Integer orderId) {
        Map<String, Object> result = new HashMap<>();
        SalesOrder order = salesOrderMapper.selectById(orderId);
        List<SalesDetail> details = salesDetailMapper.selectByOrderId(orderId);
        result.put("order", order);
        result.put("details", details);
        return result;
    }

    @Override
    public Map<String, Object> getTodaySummary() {
        return salesOrderMapper.selectTodaySummary();
    }

    @Override
    public List<Map<String, Object>> getSalesReport() {
        return salesOrderMapper.selectSalesReport();
    }

    @Override
    public boolean refund(Integer orderId) {
        SalesOrder order = salesOrderMapper.selectById(orderId);
        if (order == null || order.getOrderStatus() != 1) {
            return false;
        }
        salesOrderMapper.updateOrderStatus(orderId, 2);
        return true;
    }
}
