package com.supermarket.service;

import com.supermarket.entity.Product;
import java.util.List;
import java.util.Map;

/**
 * 商品Service接口
 */
public interface ProductService {

    /** 分页查询 */
    Map<String, Object> getPage(String keyword, Integer categoryId, Integer page, Integer limit);

    Product getById(Integer productId);

    boolean add(Product product);

    boolean update(Product product);

    boolean delete(Integer productId);

    /** 收银搜索上架商品 */
    List<Product> searchOnSale(String keyword);
}
