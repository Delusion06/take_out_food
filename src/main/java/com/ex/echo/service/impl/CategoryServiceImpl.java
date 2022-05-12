package com.ex.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.CategoryDTO;
import com.ex.echo.entity.Category;
import com.ex.echo.mapper.CategoryMapper;
import com.ex.echo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Page<Category> categoryList(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,1);
        queryWrapper.orderByAsc(Category::getSort);

        categoryMapper.selectPage(pageInfo, queryWrapper);

        return pageInfo;
    }

    @Override
    public Boolean addCategory(CategoryDTO categoryDTO,Long employeeId) {
        log.info("添加分类信息,categoryDTO:[{}],employeeId:[{}]",categoryDTO,employeeId);

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setCreateEmployeeId(employeeId);
        category.setUpdateEmployeeId(employeeId);

        log.info("添加分类信息,category:[{}]",category);

        try {
            categoryMapper.insert(category);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean editCategory(CategoryDTO categoryDTO,Long employeeId) {
        log.info("修改分类信息,categoryDTO:[{}],employeeId:[{}]",categoryDTO,employeeId);

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateEmployeeId(employeeId);

        log.info("修改分类信息,category:[{}]",category);

        try {
            categoryMapper.updateById(category);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteCategory(CategoryDTO categoryDTO,Long employeeId) {
        log.info("删除分类信息,categoryDTO:[{}],employeeId:[{}]",categoryDTO,employeeId);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getId,categoryDTO.getId());
        wrapper.eq(Category::getStatus,1);

        Category category = categoryMapper.selectOne(wrapper);
        if (Objects.isNull(category)){
            return false;
        }
        category.setStatus(0);

        log.info("删除分类信息,category:[{}]",category);

        try {
            categoryMapper.updateById(category);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public List<Category> categoryList(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        log.info("条件查询,category:[{}]",category);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getCategoryType() != null, Category::getCategoryType, category.getCategoryType());

        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryMapper.selectList(wrapper);
        if (list.size() <= 0){
            return new ArrayList<>();
        }
        return list;
    }


}