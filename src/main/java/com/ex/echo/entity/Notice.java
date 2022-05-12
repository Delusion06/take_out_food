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
 * @Description: 公告信息
 */
@Data
public class Notice implements Serializable {

    private static final long serialVersionUID = 5250094237517190330L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公告内容
     */
    private String notice;

    /**
     * 公告状态,1为使用中,0为禁用中
     */
    private Integer status;

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
}
