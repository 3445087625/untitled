package com.supermarket.service.impl;

import com.supermarket.entity.Category;
import com.supermarket.mapper.CategoryMapper;
import com.supermarket.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类Service实现
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public Category getById(Integer categoryId) {
        return categoryMapper.selectById(categoryId);
    }

    @Override
    public boolean add(Category category) {
        return categoryMapper.insert(category) > 0;
    }

    @Override
    public boolean update(Category category) {
        return categoryMapper.update(category) > 0;
    }

    @Override
    public boolean delete(Integer categoryId) {
        return categoryMapper.deleteById(categoryId) > 0;
    }
}
