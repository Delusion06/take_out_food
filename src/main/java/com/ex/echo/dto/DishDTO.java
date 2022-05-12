package com.ex.echo.dto;

import com.ex.echo.entity.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 菜品信息DTO
 */
@Data
public class DishDTO implements Serializable {
    private static final long serialVersionUID = -9057591732361123118L;

    private Long id;

    private String name;

    private Long categoryId;

    private BigDecimal price;

    private String code;

    private String image;

    private String description;

    private Integer status;

    private Integer sort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createEmployeeId;

    private Long updateEmployeeId;

    private Integer isDelete;

    private List<DishFlavor> flavorList;

    private String categoryName;
}
