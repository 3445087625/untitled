package com.supermarket.service.impl;

import com.supermarket.entity.Product;
import com.supermarket.mapper.ProductMapper;
import com.supermarket.service.ProductService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品Service实现
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Map<String, Object> getPage(String keyword, Integer categoryId, Integer page, Integer limit) {
        Map<String, Object> result = new HashMap<>();
        int offset = (page - 1) * limit;
        result.put("list", productMapper.selectPage(keyword, categoryId, offset, limit));
        result.put("total", productMapper.count(keyword, categoryId));
        return result;
    }

    @Override
    public Product getById(Integer productId) {
        return productMapper.selectById(productId);
    }

    @Override
    public boolean add(Product product) {
        return productMapper.insert(product) > 0;
    }

    @Override
    public boolean update(Product product) {
        return productMapper.update(product) > 0;
    }

    @Override
    public boolean delete(Integer productId) {
        return productMapper.deleteById(productId) > 0;
    }

    @Override
    public List<Product> searchOnSale(String keyword) {
        return productMapper.selectOnSale(keyword);
    }
}
