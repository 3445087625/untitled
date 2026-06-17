package com.supermarket.mapper;

import com.supermarket.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品Mapper接口
 */
public interface ProductMapper {

    /** 分页查询商品列表（含分类名称） */
    List<Product> selectPage(@Param("keyword") String keyword,
                             @Param("categoryId") Integer categoryId,
                             @Param("offset") Integer offset,
                             @Param("limit") Integer limit);

    /** 统计总数 */
    int count(@Param("keyword") String keyword,
              @Param("categoryId") Integer categoryId);

    Product selectById(Integer productId);

    int insert(Product product);

    int update(Product product);

    int deleteById(Integer productId);

    /** 更新库存 */
    int updateStock(@Param("productId") Integer productId,
                    @Param("quantity") Integer quantity);

    /** 查询所有上架商品（收银用） */
    List<Product> selectOnSale(@Param("keyword") String keyword);
}
