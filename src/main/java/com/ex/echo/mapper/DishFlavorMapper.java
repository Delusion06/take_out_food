package com.ex.echo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ex.echo.entity.DishFlavor;
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
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    /**
     * 批量添加菜品口味关系表记录
     *
     * @param dishFlavorList 。
     */
    @Insert("<script>" +
            "insert into dish_flavor(" +
            "dish_id,name,value,create_time,update_time,create_employee_id,update_employee_id) " + "VALUES" +
            "<foreach collection='dishFlavorList' item='item' index='index'  separator=','>" +
            "(#{item.dishId},#{item.name},#{item.value},now(),now()," +
            "#{item.createEmployeeId},#{item.updateEmployeeId})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("dishFlavorList") List<DishFlavor> dishFlavorList);

    /**
     * 批量修改菜品口味关系表is_deleted字段
     *
     * @param dishFlavorIdList .
     * @param employeeId       .
     */
    @Update("<script>" +
            "update dish_flavor set " +
            "is_deleted = '1'," +
            "update_time = now()," +
            "update_employee_id = #{employeeId} " +
            "where id in " +
            "<foreach collection='dishFlavorIdList' item='id' index='index' open='(' separator=',' close=')' > " +
            "#{id} " +
            "</foreach>" +
            "</script>")
    void batchUpdate(@Param("dishFlavorIdList") List<Long> dishFlavorIdList, @Param("employeeId") Long employeeId);
}