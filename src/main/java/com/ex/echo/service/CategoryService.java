package com.ex.echo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.CategoryDTO;
import com.ex.echo.entity.Category;

import java.util.List;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface CategoryService{

    /**
     * 分页查询
     *
     * @param page .
     * @param pageSize .
     * @return .
     */
    public Page<Category> categoryList(int page,int pageSize);

    /**
     * 添加分类信息
     *
     * @param categoryDTO .
     * @param employeeId .
     * @return .
     */
    public Boolean addCategory(CategoryDTO categoryDTO,Long employeeId);

    /**
     * 修改分类信息
     *
     * @param categoryDTO .
     * @param employeeId .
     * @return .
     */
    public Boolean editCategory(CategoryDTO categoryDTO,Long employeeId);

    /**
     * 删除分类信息
     *
     * @param categoryDTO .
     * @param employeeId .
     * @return .
     */
    public Boolean deleteCategory(CategoryDTO categoryDTO,Long employeeId);

    /**
     * 条件查询
     *
     * @param categoryDTO .
     * @return .
     */
    public List<Category> categoryList(CategoryDTO categoryDTO);

}