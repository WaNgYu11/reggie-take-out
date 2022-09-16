package com.wy.service;

import com.wy.dto.SetmealDto;
import com.wy.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);


    void removeWithDish(List<Long> ids);
}
