package com.wy.service.impl;

import com.wy.entity.Orders;
import com.wy.mapper.OrdersMapper;
import com.wy.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
