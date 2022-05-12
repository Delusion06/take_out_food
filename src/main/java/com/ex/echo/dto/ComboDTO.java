package com.ex.echo.dto;

import com.ex.echo.entity.ComboDish;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 套餐信息DTO
 */
@Data
public class ComboDTO implements Serializable {
    private static final long serialVersionUID = 6465223475624007371L;

    private Long id;

    private String name;

    private Long categoryId;

    private BigDecimal price;

    private String code;

    private String image;

    private String description;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createEmployeeId;

    private Long updateEmployeeId;

    private Integer isDeleted;

    private List<ComboDish> comboDishList;

    private List<ComboDishDTO> comboDishDTOList;

    private String categoryName;
}
