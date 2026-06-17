package com.supermarket.mapper;

import com.supermarket.entity.Category;

import java.util.List;

/**
 * 商品分类Mapper接口
 */
public interface CategoryMapper {

    List<Category> selectAll();

    Category selectById(Integer categoryId);

    int insert(Category category);

    int update(Category category);

    int deleteById(Integer categoryId);
}
