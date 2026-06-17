package com.supermarket.service.impl;

import com.supermarket.entity.Product;
import com.supermarket.entity.SalesDetail;
import com.supermarket.entity.SalesOrder;
import com.supermarket.mapper.ProductMapper;
import com.supermarket.mapper.SalesDetailMapper;
import com.supermarket.mapper.SalesOrderMapper;
import com.supermarket.service.SalesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售Service实现
 */
@Service
public class SalesServiceImpl implements SalesService {

    @Resource
    private SalesOrderMapper salesOrderMapper;
    @Resource
    private SalesDetailMapper salesDetailMapper;
    @Resource
    private ProductMapper productMapper;

    @Override
    @Transactional  // 事务：保证订单和明细、库存要么全成功，要么全回滚
    public Map<String, Object> submitOrder(SalesOrder order, List<SalesDetail> details) {
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
            detail.setPrice(p.getPrice());
            detail.setSubtotal(subtotal);
            total = total.add(subtotal);
        }
        order.setTotalAmount(total);
        if (order.getDiscountAmount() == null) {
            order.setDiscountAmount(BigDecimal.ZERO);
        }

        // 3. 保存订单
        salesOrderMapper.insert(order);

        // 4. 保存明细
        for (SalesDetail detail : details) {
            detail.setOrderId(order.getOrderId());
        }
        salesDetailMapper.batchInsert(details);

        // 5. 扣减库存
        for (SalesDetail detail : details) {
            productMapper.updateStock(detail.getProductId(), -detail.getQuantity());
        }

        result.put("success", true);
        result.put("msg", "订单提交成功");
        result.put("orderId", order.getOrderId());
        result.put("orderNo", orderNo);
        return result;
    }

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
}
