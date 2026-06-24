package com.supermarket.mapper;

import com.supermarket.entity.SalesOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售订单 Mapper — 订单 CRUD + 销售汇总 + 视图查询
 * <p>
 * 关键 SQL：
 * - selectPage：LEFT JOIN sys_user 联表查收银员姓名，子查询统计商品种类数
 * - selectTodaySummary：DATE(create_time)=CURDATE() 聚合当日数据
 * - selectSalesReport：查询视图 v_sales_summary 获取按天汇总的销售日报
 * </p>
 */
public interface SalesOrderMapper {

    /**
     * 创建订单，useGeneratedKeys=true 自动回填 orderId
     */
    int insert(SalesOrder order);

    /**
     * 分页查询订单（联表：sys_user 获取收银员姓名，子查询统计明细种类数）
     */
    List<SalesOrder> selectPage(@Param("keyword") String keyword,
                                @Param("offset") Integer offset,
                                @Param("limit") Integer limit);

    int count(@Param("keyword") String keyword);

    SalesOrder selectById(Integer orderId);

    /**
     * 今日销售汇总：COUNT(*) 订单数 + SUM(paid_amount) 今日销售额
     */
    java.util.Map<String, Object> selectTodaySummary();

    /**
     * 查询销售日报视图 v_sales_summary（sale_date, order_count, total_sales, total_quantity）
     */
    java.util.List<java.util.Map<String, Object>> selectSalesReport();

    /**
     * 退款：将订单状态改为 2（已退款）
     */
    int updateOrderStatus(@Param("orderId") Integer orderId,
                          @Param("orderStatus") Integer orderStatus);
}
