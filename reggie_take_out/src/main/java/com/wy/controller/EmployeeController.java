package com.wy.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.common.R;
import com.wy.entity.Employee;
import com.wy.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("未查询到员工信息");
    }


    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("修改成功");
    }


    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //这里之所以是返回page对象(mybatis-plus的page对象)，是因为前端需要这些分页的数据(比如当前页，总页数)
        //在编写前先测试一下前端传过来的分页数据有没有被我们接受到
        //log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询  这里不用封装了mybatis-plus帮我们做好了
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping()//因为请求就是 /employee 在类上已经写了，所以咱俩不用再写了
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        //对新增的员工设置初始化密码123456,需要进行md5加密处理，后续员工可以直接修改密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
        Long id = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(id); //创建人的id,就是当前用户的id（在进行添加操作的id）
        employee.setUpdateUser(id);//最后的更新人是谁
        //mybatis提供的新增方法
        employeeService.save(employee);

        return R.success("新增员工成功");
    }


    /**
     * 退出登录
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }



    /**
     * 用户登录
     *
     * @param request
     * @param employee
     * @return
     */
    @RequestMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {//接受JSON格式数据
        //这里为什么还有接收一个request对象的数据?
        //登陆成功后，我们需要从请求中获取员工的id，并且把这个id存到session中，这样我们想要获取登陆对象的时候就可以随时获取

        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();//从前端用户登录拿到的用户密码
        password = DigestUtils.md5DigestAsHex(password.getBytes());//对用户密码进行加密
        // 2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = this.employeeService.getOne(queryWrapper);
        // 3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("用户不存在");
        }
        // 4、密码不对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        // 5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() != 1) {
            return R.error("员工已被禁用");
        }
        // 6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }
}

