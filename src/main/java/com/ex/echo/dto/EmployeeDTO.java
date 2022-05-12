package com.ex.echo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 员工信息DTO
 */
@Data
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = -9117450188317218730L;

    private Long id;

    private String username;

    private String password;

    private String employeeName;

    private String phone;

    private Integer sex;

    private String idCard;

    private Integer status;

    private LocalDateTime updateTime;

    private Long updateEmployeeId;

    private String updateEmployeeName;
}
