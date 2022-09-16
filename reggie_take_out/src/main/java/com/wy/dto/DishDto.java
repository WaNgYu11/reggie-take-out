package com.wy.dto;

import com.wy.entity.Dish;
import com.wy.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangyu
 * @Date: 2022/09/15/15:02
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;  //后面要用的

    private Integer copies; //后面要用的

}
