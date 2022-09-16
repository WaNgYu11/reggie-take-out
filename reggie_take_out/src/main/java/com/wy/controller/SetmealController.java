package com.wy.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.common.R;
import com.wy.dto.SetmealDto;
import com.wy.entity.Category;
import com.wy.entity.Dish;
import com.wy.entity.Setmeal;
import com.wy.service.CategoryService;
import com.wy.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {


    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;


    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        this.setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }


    @PostMapping
    public R<String> save(SetmealDto setmealDto) {
        this.setmealService.saveWithDish(setmealDto);
        return R.success("添加套餐成功");
    }


    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, queryWrapper);
        // 拷贝对象
        BeanUtils.copyProperties(setmealPage, dtoPage, "records");
        List<Setmeal> setmealList = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList = setmealList.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对setmealDto进行除categoryName的属性进行拷贝(因为item里面没有categoryName)
            BeanUtils.copyProperties(item, setmealDto);
            //获取分类id  通过分类id获取分类对象  然后再通过分类对象获取分类名
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            //根据分类id获取分类对象  判断是否为null
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(setmealDtoList);
        return R.success(dtoPage);
    }

}

