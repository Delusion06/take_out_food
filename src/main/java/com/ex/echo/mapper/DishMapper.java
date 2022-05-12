package com.ex.echo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ex.echo.entity.Dish;
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
public interface DishMapper extends BaseMapper<Dish> {

    /**
     * 批量修改菜品信息表is_deleted字段
     *
     * @param dishIdList .
     * @param employeeId .
     */
    @Update("<script>" +
            "update dish set is_deleted = '1',update_time = now(),update_employee_id = #{employeeId} where id in " +
            "<foreach collection='dishIdList' item='id' index='index' open='(' separator=',' close=')' > " +
            "#{id} " +
            "</foreach>" +
            "</script>")
    void batchUpdate(@Param("dishIdList") List<Long> dishIdList,@Param("employeeId") Long employeeId);
}