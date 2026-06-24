package com.supermarket.service;

import com.supermarket.entity.SalesDetail;
import com.supermarket.entity.SalesOrder;
import java.util.List;
import java.util.Map;

/**
 * 销售Service接口
 */
public interface SalesService {

    /** 提交销售订单 */
    Map<String, Object> submitOrder(SalesOrder order, List<SalesDetail> details);

    /** 分页查订单 */
    Map<String, Object> getOrderPage(String keyword, Integer page, Integer limit);

    /** 订单详情 */
    Map<String, Object> getOrderDetail(Integer orderId);

    /** 今日销售汇总 */
    Map<String, Object> getTodaySummary();

    /** 销售日报（视图） */
    List<Map<String, Object>> getSalesReport();

    /** 退款 */
    boolean refund(Integer orderId);
}
