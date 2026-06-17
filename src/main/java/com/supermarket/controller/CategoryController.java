package com.supermarket.controller;

import com.supermarket.common.ResultVo;
import com.supermarket.entity.Category;
import com.supermarket.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品分类Controller
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /** 获取所有分类 */
    @GetMapping("/list")
    public ResultVo<List<Category>> list() {
        return ResultVo.success(categoryService.getAll());
    }

    /** 新增分类 */
    @PostMapping("/add")
    public ResultVo<?> add(@RequestBody Category category) {
        categoryService.add(category);
        return ResultVo.success("添加成功", null);
    }

    /** 修改分类 */
    @PostMapping("/update")
    public ResultVo<?> update(@RequestBody Category category) {
        categoryService.update(category);
        return ResultVo.success("修改成功", null);
    }

    /** 删除分类 */
    @PostMapping("/delete/{id}")
    public ResultVo<?> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResultVo.success("删除成功", null);
    }
}
