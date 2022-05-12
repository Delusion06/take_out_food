package com.ex.echo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.DishDTO;

import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface DishService {

    /**
     * 分页查询
     *
     * @param page     .
     * @param pageSize .
     * @param name     .
     * @return .
     */
    public Page<DishDTO> page(int page, int pageSize, String name);

    /**
     * 根据id查询菜品信息
     *
     * @param dishDTO .
     * @return .
     */
    public DishDTO getDishById(DishDTO dishDTO);

    /**
     * 添加菜品信息
     *
     * @param dishDTO    .
     * @param employeeId .
     * @return .
     */
    public Boolean addDish(DishDTO dishDTO, Long employeeId);

    /**
     * 修改菜品信息
     *
     * @param dishDTO    .
     * @param employeeId .
     * @return .
     */
    public Boolean editDish(DishDTO dishDTO, Long employeeId);

    /**
     * 修改菜品信息status
     *
     * @param dishDTOList .
     * @param employeeId  .
     * @return .
     */
    public Boolean editDishStatus(List<DishDTO> dishDTOList, Long employeeId);

    /**
     * 删除菜品信息
     *
     * @param dishDTOList .
     * @param employeeId  .
     * @return .
     */
    public Boolean deleteDish(List<DishDTO> dishDTOList, Long employeeId);

    /**
     * 条件查询
     *
     * @param dishDTO .
     * @return .
     */
    public List<DishDTO> dishList(DishDTO dishDTO);

}