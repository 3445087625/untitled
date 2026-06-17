package com.supermarket.entity;

import java.math.BigDecimal;

/**
 * 销售明细实体
 */
public class SalesDetail {
    private Integer detailId;
    private Integer orderId;         // 订单ID
    private Integer productId;       // 商品ID
    private BigDecimal price;        // 销售单价
    private Integer quantity;        // 销售数量
    private BigDecimal subtotal;     // 小计金额

    // 联表查询用
    private String productName;
    private String unit;

    public SalesDetail() {}

    public Integer getDetailId() { return detailId; }
    public void setDetailId(Integer detailId) { this.detailId = detailId; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
