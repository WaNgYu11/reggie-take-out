package com.wy.service.impl;

import com.wy.entity.Employee;
import com.wy.mapper.EmployeeMapper;
import com.wy.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
