package com.ex.echo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ex.echo.entity.ComboDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Mapper
public interface ComboDishMapper extends BaseMapper<ComboDish> {

    /**
     * 批量添加套餐菜品关系表记录
     *
     * @param comboDishList .
     */
    @Insert("<script>" +
            "insert into combo_dish(" +
            "combo_id,dish_id,amount,create_time,update_time,create_employee_id,update_employee_id) " + "VALUES" +
            "<foreach collection='comboDishList' item='item' index='index'  separator=','>" +
            "(#{item.comboId},#{item.dishId},#{item.amount},now(),now()," +
            "#{item.createEmployeeId},#{item.updateEmployeeId})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("comboDishList") List<ComboDish> comboDishList);

    /**
     * 批量修改套餐菜品关系表is_deleted字段
     *
     * @param comboDishIdList .
     * @param employeeId .
     */
    @Update("<script>" +
            "update combo_dish set " +
            "is_deleted = '1'," +
            "update_time = now()," +
            "update_employee_id = #{employeeId} " +
            "where id in " +
            "<foreach collection='comboDishIdList' item='id' index='index' open='(' separator=',' close=')' > " +
            "#{id} " +
            "</foreach>" +
            "</script>")
    void batchUpdate(@Param("comboDishIdList") List<Long> comboDishIdList, @Param("employeeId") Long employeeId);
}