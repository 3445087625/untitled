package com.supermarket.controller;

import com.supermarket.common.ResultVo;
import com.supermarket.entity.Product;
import com.supermarket.service.ProductService;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

/**
 * 商品Controller
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /** 分页查询 */
    @GetMapping("/page")
    public ResultVo<Map<String, Object>> page(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") Integer categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> data = productService.getPage(keyword, categoryId, page, limit);
        return ResultVo.success(data);
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public ResultVo<Product> getById(@PathVariable Integer id) {
        return ResultVo.success(productService.getById(id));
    }

    /** 新增 */
    @PostMapping("/add")
    public ResultVo<?> add(@RequestBody Product product) {
        productService.add(product);
        return ResultVo.success("添加成功", null);
    }

    /** 修改 */
    @PostMapping("/update")
    public ResultVo<?> update(@RequestBody Product product) {
        productService.update(product);
        return ResultVo.success("修改成功", null);
    }

    /** 删除 */
    @PostMapping("/delete/{id}")
    public ResultVo<?> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResultVo.success("删除成功", null);
    }

    /** 收银搜索上架商品 */
    @GetMapping("/search")
    public ResultVo<List<Product>> search(@RequestParam(defaultValue = "") String keyword) {
        return ResultVo.success(productService.searchOnSale(keyword));
    }
}
