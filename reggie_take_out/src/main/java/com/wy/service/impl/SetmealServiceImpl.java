package com.wy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.common.CustomException;
import com.wy.dto.SetmealDto;
import com.wy.entity.Setmeal;
import com.wy.entity.SetmealDish;
import com.wy.mapper.SetmealMapper;
import com.wy.service.SetmealDishService;
import com.wy.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal,执行insert
        this.save(setmealDto);
        log.info("log.info========" + setmealDto.toString()); //查看一下这个套餐的基本信息是什么
        //保存套餐和菜品的关联信息，操作setmeal_dish ,执行insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //注意上面拿到的setmealDishes是没有setmeanlId这个的值的，通过debug可以发现
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item; //这里返回的就是集合的泛型
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes); //批量保存
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //sql语句应该是这样的:select count(*) setmeal where id in () and status = 1;
        //查询套餐的状态，看是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = (int) this.count(queryWrapper);
        //如果不能删除，抛出一个业务异常
        if (count <= 0) {
            throw new CustomException("该套餐正在售卖，不能删除");
        }
        //如果可以删除，先删除套餐表中的数据--setmeal
        this.removeByIds(ids);
        //删除关系表中的数据--setmeal_dish
        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(SetmealDish::getSetmealId, ids);
        this.setmealDishService.remove(queryWrapper2);

    }

}
