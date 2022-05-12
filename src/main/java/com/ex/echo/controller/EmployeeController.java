package com.ex.echo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.dto.EmployeeDTO;
import com.ex.echo.entity.Employee;
import com.ex.echo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 员工信息管理
 */
@Slf4j
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request      .
     * @param employeeDTO .
     * @return .
     */
    @RequestMapping(value = "/employee/login", method = RequestMethod.POST)
    public JsonModel<Employee> login(HttpServletRequest request, @RequestBody EmployeeDTO employeeDTO) {
        log.info("员工登录,employeeDTO:[{}]", employeeDTO);

        Employee result = employeeService.employeeLogin(employeeDTO);
        if (Objects.isNull(result)){
            return JsonModel.error("用户名或密码错误!");
        }
        if (result.getStatus() == 0){
            return JsonModel.error("用户已被禁用,详情请联系管理员!");
        }

        request.getSession().setAttribute(Constants.EMPLOYEE, result.getId());

        return JsonModel.success(result);
    }

    /**
     * 员工退出登录
     *
     * @param request .
     * @return .
     */
    @RequestMapping(value = "/employee/logout", method = RequestMethod.POST)
    public JsonModel<String> logout(HttpServletRequest request) {
        // 判断是否存在该会话
        if (Objects.isNull(request.getSession().getAttribute(Constants.EMPLOYEE))){
            return JsonModel.error("尚未登录!");
        }
        request.getSession().removeAttribute(Constants.EMPLOYEE);
        return JsonModel.success("退出成功!");
    }

    /**
     * 分页查询
     *
     * @param page .
     * @param pageSize .
     * @param employeeName .
     * @return .
     */
    @RequestMapping(value = "/employee/page", method = RequestMethod.GET)
    public JsonModel<Page> page(int page, int pageSize, String employeeName) {
        log.info("分页查询,page=[{}],pageSize=[{}],employeeName=[{}]",page,pageSize,employeeName);

        Page<EmployeeDTO> result = employeeService.employeeList(page,pageSize,employeeName);
        return JsonModel.success(result);
    }

    /**
     * 根据id查询员工信息
     *
     * @param employeeDTO .
     * @return .
     */
    @RequestMapping(value = "/employee/getById", method = RequestMethod.POST)
    public JsonModel<EmployeeDTO> getEmployeeById(HttpServletRequest request, @RequestBody EmployeeDTO employeeDTO) {
        log.info("根据id查询员工信息,employeeDTO:[{}]", employeeDTO);

        EmployeeDTO result = employeeService.getEmployeeById(employeeDTO);
        if (Objects.isNull(result)){
            return JsonModel.error("查询出错!");
        }
        return JsonModel.success(result);
    }

    /**
     * 添加员工信息
     *
     * @param request .
     * @param employeeDTO .
     * @return .
     */
    @RequestMapping(value = "/employee/add", method = RequestMethod.POST)
    public JsonModel<String> addEmployee(HttpServletRequest request, @RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工信息,employeeDTO:[{}]", employeeDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = employeeService.addEmployee(employeeDTO,employeeId);
        if (!result){
            return JsonModel.error("添加失败!");
        }
        return JsonModel.success("添加成功!");
    }

    /**
     * 修改员工账号
     *
     * @param employeeDTO .
     * @return .
     */
    @RequestMapping(value = "/employee/edit", method = RequestMethod.POST)
    public JsonModel<String> editEmployee(HttpServletRequest request, @RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工信息,employeeDTO:[{}]",employeeDTO);

        Long employeeId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);

        boolean result = employeeService.editEmployee(employeeDTO,employeeId);
        if (!result){
            return JsonModel.error("修改失败!");
        }
        return JsonModel.success("修改成功!");
    }
}