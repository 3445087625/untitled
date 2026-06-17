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
}
