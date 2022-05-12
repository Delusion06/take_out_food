package com.ex.echo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Echo
 * @Date: 2022/04/29/ 13:41
 * @Description: 套餐菜品关系
 */
@Data
public class ComboDish implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 套餐id
     */
    private Long comboId;

    /**
     * 菜品id
     */
    private Long dishId;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人id
     */
    private Long createEmployeeId;

    /**
     * 更新人id
     */
    private Long updateEmployeeId;

    /**
     * 是否删除
     */
    private Integer isDeleted;
}