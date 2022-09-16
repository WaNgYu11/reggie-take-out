package com.wy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.common.R;
import com.wy.dto.DishDto;
import com.wy.entity.Category;
import com.wy.entity.Dish;
import com.wy.service.CategoryService;
import com.wy.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {


    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;


    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) { //会自动映射的
        //这里可以传categoryId,但是为了代码通用性更强,这里直接使用dish类来接受（因为dish里面是有categoryId的）,以后传dish的其他属性这里也可以使用
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus, 1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);


    }


    /**
     * 商品的启售和停售
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        log.info("status:{}", status);
        log.info("ids:{}", ids);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids != null, Dish::getId, ids);
        List<Dish> dishList = this.dishService.list(queryWrapper);
        for (Dish dish : dishList) {
            if (dish != null) {
                dish.setStatus(status);
                dishService.updateById(dish);
            }
        }
        return R.success("售卖状态修改成功");
    }


    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        // 逻辑删除
        this.dishService.deleteByIds(ids);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        dishService.remove(queryWrapper);
        return R.success("删除菜品成功");
    }


    @GetMapping("/{id}")
    public R<DishDto> update(@PathVariable Long id) {
        DishDto dishDto = this.dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造一个分页构造器对象
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //上面对dish泛型的数据已经赋值了，这里对DishDto我们可以把之前的数据拷贝过来进行赋值
        //构造一个条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件 注意判断是否为空  使用对name的模糊查询
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件  根据更新时间降序排
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //去数据库处理分页 和 查询
        dishService.page(dishPage, queryWrapper);
        //获取到dish的所有数据 records属性是分页插件中表示分页中所有的数据的一个集合
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            //对实体类DishDto进行categoryName的设值
            DishDto dishDto = new DishDto();
            //这里的item相当于Dish  对dishDto进行除categoryName属性的拷贝
            BeanUtils.copyProperties(item, dishDto);
            //获取分类的id
            Long categoryId = item.getCategoryId();
            //通过分类id获取分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                System.out.println("");
                //设置实体类DishDto的categoryName属性值
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        //对象拷贝  使用框架自带的工具类，第三个参数是不拷贝到属性
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        dishDtoPage.setRecords(list);
        //因为上面处理的数据没有分类的id,这样直接返回R.success(dishPage)虽然不会报错，但是前端展示的时候这个菜品分类这一数据就为空
        //所以进行了上面的一系列操作
        return R.success(dishDtoPage);
    }


    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        this.dishService.saveWithFlavor(dishDto);
        return R.success("添加菜品成功");
    }


    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        this.dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

}

