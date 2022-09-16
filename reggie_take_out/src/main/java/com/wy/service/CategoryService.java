package com.wy.service;

import com.wy.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
public interface CategoryService extends IService<Category> {


    void remove(Long id);

}
