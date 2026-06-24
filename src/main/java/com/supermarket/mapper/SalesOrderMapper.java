package com.supermarket.mapper;

import com.supermarket.entity.SalesOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售订单Mapper接口
 */
public interface SalesOrderMapper {

    /** 创建订单，返回订单ID */
    int insert(SalesOrder order);

    /** 分页查询订单 */
    List<SalesOrder> selectPage(@Param("keyword") String keyword,
                                @Param("offset") Integer offset,
                                @Param("limit") Integer limit);

    int count(@Param("keyword") String keyword);

    SalesOrder selectById(Integer orderId);

    /** 今日销售汇总：订单数 + 销售额 */
    java.util.Map<String, Object> selectTodaySummary();

    /** 查询视图：销售日报 */
    java.util.List<java.util.Map<String, Object>> selectSalesReport();

    /** 退款：更新订单状态 */
    int updateOrderStatus(@Param("orderId") Integer orderId,
                          @Param("orderStatus") Integer orderStatus);
}
