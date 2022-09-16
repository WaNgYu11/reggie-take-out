package com.wy.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.common.R;
import com.wy.entity.Orders;
import com.wy.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@RestController
@RequestMapping("/order")
public class OrdersController {


    @Autowired
    private OrdersService ordersService;


    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime, String endTime) {

        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isEmpty(number), Orders::getNumber, number)
                .gt(StringUtils.isEmpty(beginTime), Orders::getOrderTime, beginTime)
                .lt(StringUtils.isEmpty(endTime), Orders::getOrderTime, endTime);
        this.ordersService.page(ordersPage, queryWrapper);
        return R.success(ordersPage);
    }


}

