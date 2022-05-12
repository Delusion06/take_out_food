package com.ex.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.ComboDTO;
import com.ex.echo.dto.ComboDishDTO;
import com.ex.echo.entity.Category;
import com.ex.echo.entity.Combo;
import com.ex.echo.entity.ComboDish;
import com.ex.echo.entity.Dish;
import com.ex.echo.mapper.CategoryMapper;
import com.ex.echo.mapper.ComboDishMapper;
import com.ex.echo.mapper.ComboMapper;
import com.ex.echo.mapper.DishMapper;
import com.ex.echo.service.ComboService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
public class ComboServiceImpl implements ComboService {

    @Autowired
    private ComboMapper comboMapper;

    @Autowired
    private ComboDishMapper comboDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public Page<ComboDTO> page(int page, int pageSize, String name) {
        Page<ComboDTO> comboDTOPage = new Page<>();
        Page<Combo> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Combo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Combo::getName, name);
        wrapper.eq(Combo::getIsDeleted, 0);
        comboMapper.selectPage(pageInfo, wrapper);

        List<Combo> records = pageInfo.getRecords();
        BeanUtils.copyProperties(pageInfo, comboDTOPage, "records");

        List<ComboDTO> comboDTOList = records
                .stream().map(item -> {
                    ComboDTO comboDTO = new ComboDTO();
                    BeanUtils.copyProperties(item, comboDTO);

                    // 获取categoryName
                    long categoryId = item.getCategoryId();
                    Category category = categoryMapper.selectById(categoryId);
                    if (category != null) {
                        comboDTO.setCategoryName(category.getCategoryName());
                    }

                    // 获取comboDishList
                    LambdaQueryWrapper<ComboDish> wrapper1 = new LambdaQueryWrapper<>();
                    wrapper1.eq(item.getId() != null,ComboDish::getComboId,item.getId());
                    wrapper1.eq(ComboDish::getIsDeleted,0);
                    List<ComboDish> comboDishList = comboDishMapper.selectList(wrapper1);
                    comboDTO.setComboDishList(comboDishList);

                    return comboDTO;
                }).collect(Collectors.toList());

        comboDTOPage.setRecords(comboDTOList);
        return comboDTOPage;
    }

    @Override
    public ComboDTO getComboById(ComboDTO comboDTO) {
        ComboDTO result = new ComboDTO();
        Combo combo = comboMapper.selectById(comboDTO.getId());

        log.info("根据id查询套餐信息,combo:[{}]", combo);

        if (Objects.isNull(combo)) {
            return null;
        }
        BeanUtils.copyProperties(combo, result);

        Category category = categoryMapper.selectById(combo.getCategoryId());
        if (Objects.isNull(category)) {
            return null;
        }
        result.setCategoryName(category.getCategoryName());

        LambdaQueryWrapper<ComboDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(combo.getId() != null,ComboDish::getComboId,combo.getId());
        wrapper.eq(ComboDish::getIsDeleted,0);

        List<ComboDish> comboDishList = comboDishMapper.selectList(wrapper);

        log.info("根据id查询套餐信息,comboDishList:[{}]", comboDishList);

        result.setComboDishList(comboDishList);

        List<ComboDishDTO> comboDishDTOList = comboDishList.stream().map(item -> {
            ComboDishDTO comboDishDTO = new ComboDishDTO();
            BeanUtils.copyProperties(item, comboDishDTO);

            long id = comboDishDTO.getDishId();
            Dish dish = dishMapper.selectById(id);

            comboDishDTO.setName(dish.getName());
            comboDishDTO.setPrice(dish.getPrice());
            comboDishDTO.setImage(dish.getImage());
            comboDishDTO.setDescription(dish.getDescription());
            return comboDishDTO;
        }).collect(Collectors.toList());

        log.info("根据id查询套餐信息,comboDishDTOList:[{}]", comboDishDTOList);

        result.setComboDishDTOList(comboDishDTOList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addCombo(ComboDTO comboDTO, Long employeeId) {
        log.info("添加套餐信息,comboDTO:[{}],employeeId:[{}]", comboDTO, employeeId);

        Combo combo = new Combo();
        BeanUtils.copyProperties(comboDTO, combo);
        combo.setCreateEmployeeId(employeeId);
        combo.setUpdateEmployeeId(employeeId);

        log.info("添加套餐信息,combo:[{}]", combo);

        comboMapper.insert(combo);

        List<ComboDish> comboDishList = comboDTO.getComboDishList()
                .stream().map(item -> {
                    item.setComboId(combo.getId());
                    item.setCreateEmployeeId(employeeId);
                    item.setUpdateEmployeeId(employeeId);
                    return item;
                }).collect(Collectors.toList());

        log.info("添加套餐信息,comboDishList:[{}]", comboDishList);

        comboDishMapper.batchInsert(comboDishList);
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean editCombo(ComboDTO comboDTO, Long employeeId) {
        log.info("修改套餐信息,comboDTO:[{}],employeeId:[{}]", comboDTO, employeeId);

        Combo combo = new Combo();
        BeanUtils.copyProperties(comboDTO, combo);
        combo.setUpdateEmployeeId(employeeId);

        log.info("修改套餐信息,combo:[{}]", combo);

        comboMapper.updateById(combo);

        // 删除原来对应的套餐菜品表
        LambdaQueryWrapper<ComboDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(comboDTO.getId() != null, ComboDish::getComboId, comboDTO.getId());
        List<ComboDish> comboDishListByComboId = comboDishMapper.selectList(wrapper);

        List<Long> comboDishIdList = comboDishListByComboId
                .stream().map(ComboDish::getId).collect(Collectors.toList());

        log.info("修改套餐信息,comboDishIdList:[{}]", comboDishIdList);

        comboDishMapper.batchUpdate(comboDishIdList, employeeId);

        // 新增对应的套餐菜品关系表
        List<ComboDish> newComboDishList = comboDTO.getComboDishList()
                .stream().map(item -> {
                    ComboDish comboDish = new ComboDish();
                    BeanUtils.copyProperties(item, comboDish);
                    comboDish.setComboId(combo.getId());
                    comboDish.setId(null);
                    comboDish.setCreateEmployeeId(employeeId);
                    comboDish.setUpdateEmployeeId(employeeId);
                    return comboDish;
                }).collect(Collectors.toList());

        comboDishMapper.batchInsert(newComboDishList);
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean editComboStatus(List<ComboDTO> comboDTOList, Long employeeId) {
        log.info("修改套餐信息status,comboDTOList:[{}],employeeId:[{}]", comboDTOList, employeeId);

        List<Combo> comboList = comboDTOList
                .stream().map(item -> {
                    Combo combo = new Combo();
                    combo.setId(item.getId());
                    combo.setStatus(item.getStatus());
                    combo.setUpdateEmployeeId(employeeId);
                    return combo;
                }).collect(Collectors.toList());

        log.info("修改套餐信息status,comboList:[{}]", comboList);

        if (comboList.size() <= 0) {
            return false;
        }

        for (Combo combo : comboList) {
            comboMapper.updateById(combo);
        }
        return true;
    }

    @Override
    public Boolean deleteCombo(List<ComboDTO> comboDTOList, Long employeeId) {
        log.info("删除套餐信息,comboDTOList:[{}],employeeId:[{}]", comboDTOList, employeeId);

        List<Long> idList = comboDTOList
                .stream().map(ComboDTO::getId).collect(Collectors.toList());

        log.info("删除套餐信息,idList:[{}]", idList);

        comboMapper.batchUpdate(idList, employeeId);
        return true;
    }

    @Override
    public List<Combo> comboList(ComboDTO comboDTO) {
        LambdaQueryWrapper<Combo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(comboDTO.getCategoryId() != null, Combo::getCategoryId, comboDTO.getCategoryId());
        queryWrapper.eq(comboDTO.getStatus() != null, Combo::getStatus, comboDTO.getStatus());
        queryWrapper.orderByDesc(Combo::getUpdateTime);

        List<Combo> comboList = comboMapper.selectList(queryWrapper);
        if (comboList.size() <= 0) {
            return new ArrayList<>();
        }
        return comboList;
    }
}