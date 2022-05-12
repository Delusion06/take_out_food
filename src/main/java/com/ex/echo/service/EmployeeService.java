package com.ex.echo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.EmployeeDTO;
import com.ex.echo.entity.Employee;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface EmployeeService{

    /**
     * 员工登录
     *
     * @param employeeDTO .
     * @return .
     */
    public Employee employeeLogin(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     *
     * @param page .
     * @param pageSize .
     * @param employeeName .
     * @return .
     */
    public Page<EmployeeDTO> employeeList(int page, int pageSize, String employeeName);

    /**
     * 根据id查询员工信息
     *
     * @param employeeDTO .
     * @return .
     */
    public EmployeeDTO getEmployeeById(EmployeeDTO employeeDTO);

    /**
     * 添加员工信息
     *
     * @param employeeDTO .
     * @param employeeId .
     * @return .
     */
    public Boolean addEmployee(EmployeeDTO employeeDTO,Long employeeId);

    /**
     * 修改员工信息
     *
     * @param employeeDTO .
     * @param employeeId .
     * @return .
     */
    public Boolean editEmployee(EmployeeDTO employeeDTO,Long employeeId);
}
