package com.ex.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.EmployeeDTO;
import com.ex.echo.entity.Employee;
import com.ex.echo.entity.EmployeeInfo;
import com.ex.echo.mapper.EmployeeInfoMapper;
import com.ex.echo.mapper.EmployeeMapper;
import com.ex.echo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeInfoMapper employeeInfoMapper;

    @Override
    public Employee employeeLogin(EmployeeDTO employeeDTO) {
        if (StringUtils.isEmpty(employeeDTO.getPassword())){
            return null;
        }
        String password = DigestUtils.md5DigestAsHex(employeeDTO.getPassword().getBytes());

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(employeeDTO.getUsername()),Employee::getUsername,employeeDTO.getUsername());

        Employee result = employeeMapper.selectOne(wrapper);

        if (Objects.isNull(result) || !result.getPassword().equals(password)) {
            return null;
        }
        return result;
    }

    @Override
    public Page<EmployeeDTO> employeeList(int page, int pageSize, String employeeName) {
        Page<EmployeeDTO> employeeDTOPage = new Page<>();
        Page<EmployeeInfo> pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<EmployeeInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(employeeName),EmployeeInfo::getEmployeeName,employeeName);

        employeeInfoMapper.selectPage(pageInfo, wrapper);

        List<EmployeeInfo> records = pageInfo.getRecords();
        BeanUtils.copyProperties(pageInfo,employeeDTOPage,"records");

        List<EmployeeDTO> employeeDTOList = records
                .stream().map(item->{
                    EmployeeDTO employeeDTO = new EmployeeDTO();
                    BeanUtils.copyProperties(item,employeeDTO);

                    // 获取更新人姓名
                    LambdaQueryWrapper<EmployeeInfo> wrapper1 = new LambdaQueryWrapper<>();
                    wrapper1.eq(EmployeeInfo::getEmployeeId,item.getUpdateEmployeeId());
                    EmployeeInfo employeeInfo = employeeInfoMapper.selectOne(wrapper1);
                    if (StringUtils.isNotEmpty(employeeInfo.getEmployeeName())) {
                        employeeDTO.setUpdateEmployeeName(employeeInfo.getEmployeeName());
                    }

                    // 获取用户账号
                    Employee employee = employeeMapper.selectById(item.getEmployeeId());
                    if (StringUtils.isNotEmpty(employee.getUsername())){
                        employeeDTO.setUsername(employee.getUsername());
                    }

                    return employeeDTO;
                }).collect(Collectors.toList());

        employeeDTOPage.setRecords(employeeDTOList);
        return employeeDTOPage;
    }

    @Override
    public EmployeeDTO getEmployeeById(EmployeeDTO employeeDTO) {
        EmployeeInfo employeeInfo = employeeInfoMapper.selectById(employeeDTO.getId());

        if (Objects.isNull(employeeInfo)) {
            return null;
        }

        Employee employee = employeeMapper.selectById(employeeInfo.getEmployeeId());
        if (Objects.isNull(employee)) {
            return null;
        }

        EmployeeDTO result = new EmployeeDTO();
        BeanUtils.copyProperties(employee, result);
        BeanUtils.copyProperties(employeeInfo, result);

        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addEmployee(EmployeeDTO employeeDTO, Long employeeId) {
        log.info("添加员工信息,employeeDTO:[{}],employeeId:[{}]", employeeDTO, employeeId);

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setCreateEmployeeId(employeeId);
        employee.setUpdateEmployeeId(employeeId);
        String password = DigestUtils.md5DigestAsHex(employeeDTO.getPassword().getBytes());
        employee.setPassword(password);

        log.info("添加员工信息,employee:[{}]", employee);

        EmployeeInfo employeeInfo = new EmployeeInfo();
        BeanUtils.copyProperties(employeeDTO, employeeInfo);
        employeeInfo.setCreateEmployeeId(employeeId);
        employeeInfo.setUpdateEmployeeId(employeeId);

        employeeMapper.insert(employee);
        employeeInfo.setEmployeeId(employee.getId());

        log.info("添加员工信息,employeeInfo:[{}]", employeeInfo);

        employeeInfoMapper.insert(employeeInfo);
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean editEmployee(EmployeeDTO employeeDTO, Long employeeId) {
        EmployeeInfo employeeInfo = new EmployeeInfo();
        BeanUtils.copyProperties(employeeDTO, employeeInfo);
        employeeInfo.setUpdateEmployeeId(employeeId);
        employeeInfoMapper.updateById(employeeInfo);

        long newEmployeeId = employeeInfoMapper.selectById(employeeDTO.getId()).getEmployeeId();

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setId(newEmployeeId);
        employee.setUpdateEmployeeId(employeeId);

        if (StringUtils.isNotEmpty(employeeDTO.getPassword())) {
            String password = DigestUtils.md5DigestAsHex(employeeDTO.getPassword().getBytes());
            employee.setPassword(password);
        }

        employeeMapper.updateById(employee);
        return true;
    }

    @Override
    public Boolean getEmployeeByPhone(EmployeeDTO employeeDTO) {
        String phone = employeeDTO.getPhone();

        return null;
    }
}
