package com.ex.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.DishDTO;
import com.ex.echo.entity.Category;
import com.ex.echo.entity.Dish;
import com.ex.echo.entity.DishFlavor;
import com.ex.echo.mapper.CategoryMapper;
import com.ex.echo.mapper.DishFlavorMapper;
import com.ex.echo.mapper.DishMapper;
import com.ex.echo.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Page<DishDTO> page(int page, int pageSize, String name) {
        Page<DishDTO> dishDtoPage = new Page<>();
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Dish::getName, name);
        wrapper.eq(Dish::getIsDeleted, 0);
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishMapper.selectPage(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDTO> dtoList = records.stream().map(item -> {
            DishDTO dishDto = new DishDTO();
            BeanUtils.copyProperties(item, dishDto);

            Long id = item.getCategoryId();
            if (id != null) {
                Category category = categoryMapper.selectById(id);
                dishDto.setCategoryName(category.getCategoryName());
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dtoList);
        return dishDtoPage;
    }

    @Override
    public DishDTO getDishById(DishDTO dishDTO) {
        DishDTO result = new DishDTO();
        Dish dish = dishMapper.selectById(dishDTO.getId());

        log.info("根据id查询菜品信息,dish:[{}]", dish);

        if (Objects.isNull(dish)) {
            return null;
        }

        BeanUtils.copyProperties(dish, result);

        Category category = categoryMapper.selectById(dish.getCategoryId());
        if (Objects.isNull(category)) {
            return null;
        }
        result.setCategoryName(category.getCategoryName());

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getId() != null, DishFlavor::getDishId, dish.getId());
        wrapper.eq(DishFlavor::getIsDeleted, 0);
        List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper);

        log.info("根据id查询菜品信息,dishFlavorList:[{}]", dishFlavorList);

        result.setFlavorList(dishFlavorList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addDish(DishDTO dishDTO, Long employeeId) {
        log.info("添加菜品信息,dishDTO:[{}],employeeId:[{}]", dishDTO, employeeId);

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setCreateEmployeeId(employeeId);
        dish.setUpdateEmployeeId(employeeId);

        log.info("添加菜品信息,dish:[{}]", dish);

        dishMapper.insert(dish);

        Long dishId = dish.getId();

        List<DishFlavor> dishFlavorList = dishDTO.getFlavorList()
                .stream().map(item -> {
                    item.setDishId(dishId);
                    item.setCreateEmployeeId(employeeId);
                    item.setUpdateEmployeeId(employeeId);
                    return item;
                }).collect(Collectors.toList());

        log.info("添加菜品信息,dishFlavorList:[{}]", dishFlavorList);

        dishFlavorMapper.batchInsert(dishFlavorList);
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean editDish(DishDTO dishDTO, Long employeeId) {
        log.info("修改菜品信息,dishDTO:[{}],employeeId:[{}]", dishDTO, employeeId);

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setUpdateEmployeeId(employeeId);

        log.info("修改菜品信息,dish:[{}]", dish);

        dishMapper.updateById(dish);

        // 删除原来对应的口味信息表
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getId() != null, DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavorListByDishId = dishFlavorMapper.selectList(wrapper);

        List<Long> dishFlavorIdList = dishFlavorListByDishId
                .stream().map(DishFlavor::getId).collect(Collectors.toList());

        log.info("修改菜品信息,dishFlavorIdList:[{}]", dishFlavorIdList);

        dishFlavorMapper.batchUpdate(dishFlavorIdList, employeeId);

        // 新增对应的口味信息表
        List<DishFlavor> newDishFlavorList = dishDTO.getFlavorList()
                .stream().map(item -> {
                    DishFlavor dishFlavor = new DishFlavor();
                    BeanUtils.copyProperties(item, dishFlavor);
                    dishFlavor.setDishId(dish.getId());
                    dishFlavor.setId(null);
                    dishFlavor.setCreateEmployeeId(employeeId);
                    dishFlavor.setUpdateEmployeeId(employeeId);
                    return dishFlavor;
                }).collect(Collectors.toList());

        dishFlavorMapper.batchInsert(newDishFlavorList);
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean editDishStatus(List<DishDTO> dishDTOList, Long employeeId) {
        log.info("修改菜品信息status,dishDTOList:[{}],employeeId:[{}]", dishDTOList, employeeId);

        List<Dish> dishList = dishDTOList
                .stream().map(item -> {
                    Dish dish = new Dish();
                    dish.setId(item.getId());
                    dish.setStatus(item.getStatus());
                    dish.setUpdateEmployeeId(employeeId);
                    return dish;
                }).collect(Collectors.toList());

        log.info("修改菜品信息status,dishList:[{}]", dishList);

        if (dishList.size() <= 0) {
            return false;
        }

        for (Dish dish : dishList) {
            dishMapper.updateById(dish);
        }
        return true;
    }

    @Override
    public Boolean deleteDish(List<DishDTO> dishDTOList, Long employeeId) {
        log.info("删除菜品信息,dishDTOList:[{}],employeeId:[{}]", dishDTOList, employeeId);

        List<Long> idList = dishDTOList
                .stream().map(DishDTO::getId).collect(Collectors.toList());

        log.info("删除菜品信息,idList:[{}]", idList);

        dishMapper.batchUpdate(idList, employeeId);
        return true;
    }

    @Override
    public List<DishDTO> dishList(DishDTO dishDTO) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dishDTO.getCategoryId() != null, Dish::getCategoryId, dishDTO.getCategoryId());
        wrapper.like(dishDTO.getName() != null, Dish::getName, dishDTO.getName());
        wrapper.eq(Dish::getStatus, 1);
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishMapper.selectList(wrapper);

        List<DishDTO> dishDTOList = list.stream().map((item) -> {
            DishDTO dishDto = new DishDTO();
            BeanUtils.copyProperties(item, dishDto);

            Category category = categoryMapper.selectById(item.getCategoryId());
            if (category != null) {
                String categoryName = category.getCategoryName();
                dishDto.setCategoryName(categoryName);
            }

            LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(item.getId() != null,DishFlavor::getDishId,item.getId());
            wrapper1.eq(DishFlavor::getIsDeleted,0);
            List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper1);
            dishDto.setFlavorList(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        if (dishDTOList.size() <= 0){
            return new ArrayList<>();
        }
        return dishDTOList;
    }
}