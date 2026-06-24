package com.supermarket.entity;

import java.math.BigDecimal;

/**
 * 销售订单实体 — 映射 sales_order 表
 * <p>
 * - orderNo: SO+yyyyMMddHHmmss
 * - paymentMethod: 1=现金 2=微信 3=支付宝
 * - orderStatus: 1=已完成 2=已退款
 * - realName/detailCount: 联表填充，不存库
 * </p>
 */
public class SalesOrder {
    private Integer orderId;
    private String orderNo;          // 订单编号（自动生成）
    private Integer userId;          // 收银员ID
    private BigDecimal totalAmount;  // 订单总金额
    private BigDecimal discountAmount; // 折扣金额
    private BigDecimal paidAmount;   // 实收金额
    private BigDecimal changeAmount; // 找零金额
    private Integer paymentMethod;   // 支付方式：1现金 2微信 3支付宝
    private Integer orderStatus;     // 订单状态：1已完成 2已退货
    private String remark;
    private String createTime;

    // 联表查询用
    private String realName;         // 收银员姓名
    private Integer detailCount;     // 商品种类数

    public SalesOrder() {}

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    public BigDecimal getChangeAmount() { return changeAmount; }
    public void setChangeAmount(BigDecimal changeAmount) { this.changeAmount = changeAmount; }
    public Integer getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(Integer paymentMethod) { this.paymentMethod = paymentMethod; }
    public Integer getOrderStatus() { return orderStatus; }
    public void setOrderStatus(Integer orderStatus) { this.orderStatus = orderStatus; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public Integer getDetailCount() { return detailCount; }
    public void setDetailCount(Integer detailCount) { this.detailCount = detailCount; }
}
