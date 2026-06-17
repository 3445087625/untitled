package com.supermarket.entity;

import java.math.BigDecimal;

/**
 * 商品实体
 */
public class Product {
    private Integer productId;
    private String productName;       // 商品名称
    private Integer categoryId;       // 所属分类ID
    private BigDecimal price;         // 销售单价
    private Integer stock;            // 库存数量
    private String unit;              // 单位（个/箱/瓶/袋）
    private String barcode;           // 条形码
    private String imageUrl;          // 商品图片
    private Integer status;           // 1上架 0下架
    private String createTime;
    private String updateTime;

    // 联表查询用
    private String categoryName;

    public Product() {}

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
