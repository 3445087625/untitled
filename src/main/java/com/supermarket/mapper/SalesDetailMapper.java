package com.supermarket.mapper;

import com.supermarket.entity.SalesDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售明细Mapper接口
 */
public interface SalesDetailMapper {

    /** 批量插入明细 */
    int batchInsert(@Param("list") List<SalesDetail> list);

    /** 根据订单ID查询明细 */
    List<SalesDetail> selectByOrderId(Integer orderId);
}
