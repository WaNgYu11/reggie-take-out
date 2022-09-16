package com.wy.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.wy.common.CustomException;
import com.wy.common.R;
import com.wy.entity.Category;
import com.wy.entity.Dish;
import com.wy.entity.Setmeal;
import com.wy.mapper.CategoryMapper;
import com.wy.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wy.service.DishService;
import com.wy.service.SetmealService;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;


    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Dish.class) ;
        int count = (int) dishService.count(dishLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("当前分类关联菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Setmeal.class) ;
        int count1 = (int) setmealService.count(setmealLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类关联了套餐，不能删除");
        }

        super.removeById(id);

    }
}
