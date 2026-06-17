package com.supermarket.service;

import com.supermarket.entity.Category;
import java.util.List;

/**
 * 分类Service接口
 */
public interface CategoryService {

    List<Category> getAll();

    Category getById(Integer categoryId);

    boolean add(Category category);

    boolean update(Category category);

    boolean delete(Integer categoryId);
}
