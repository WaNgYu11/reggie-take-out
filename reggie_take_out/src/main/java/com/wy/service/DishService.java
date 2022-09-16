package com.wy.service;

import com.wy.dto.DishDto;
import com.wy.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
public interface DishService extends IService<Dish> {


    //新增菜品,同时插入菜品对应的口味数据,需要同时操作两张表:dish  dish_flavor
    void saveWithFlavor(DishDto dishDto);


    DishDto getByIdWithFlavor(Long id);

    void deleteByIds(List<Long> ids);

    void updateWithFlavor(DishDto dishDto);
}
