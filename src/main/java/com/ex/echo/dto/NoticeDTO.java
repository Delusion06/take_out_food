package com.ex.echo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 公告信息DTO
 */
@Data
public class NoticeDTO implements Serializable {
    private static final long serialVersionUID = -3067217881454427390L;

    private Long id;

    private String notice;

    private Integer status;

    private LocalDateTime updateTime;

    private Long createEmployeeId;

    private Long updateEmployeeId;

    private String updateEmployeeName;
}
