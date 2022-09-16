package com.wy.controller;


import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.common.R;
import com.wy.entity.Category;
import com.wy.entity.Setmeal;
import com.wy.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    @RequestMapping("/list")
    public R<List<Category>> list(Category category) {
        //
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        // 排序
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }


    @PutMapping
    public R<String> updateById(@RequestBody Category category) {

        categoryService.updateById(category);
        return R.success("修改分类成功");
    }


    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id) {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Category.class);
        categoryService.remove(id);
        return R.success("分类删除成功");
    }


    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize) {

        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(categoryPage, queryWrapper);
        return R.success(categoryPage);
    }


    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("{category}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }


}

