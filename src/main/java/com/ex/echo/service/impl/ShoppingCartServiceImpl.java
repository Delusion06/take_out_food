package com.ex.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ex.echo.dto.ShoppingCartDTO;
import com.ex.echo.entity.Combo;
import com.ex.echo.entity.Dish;
import com.ex.echo.entity.ShoppingCart;
import com.ex.echo.mapper.ComboMapper;
import com.ex.echo.mapper.DishMapper;
import com.ex.echo.mapper.ShoppingCartMapper;
import com.ex.echo.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private ComboMapper comboMapper;

    @Override
    public ShoppingCartDTO addShoppingCart(ShoppingCartDTO shoppingCartDTO, Long userId) {
        log.info("添加购物车信息,shoppingCartDTO:[{}],userId:[{}]", shoppingCartDTO, userId);

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.eq(shoppingCartDTO.getDishId() != null, ShoppingCart::getDishId, shoppingCartDTO.getDishId());
        wrapper.eq(shoppingCartDTO.getComboId() != null, ShoppingCart::getComboId, shoppingCartDTO.getComboId());
        wrapper.eq(ShoppingCart::getStatus, 1);

        ShoppingCart shoppingCart = shoppingCartMapper.selectOne(wrapper);

        log.info("添加购物车信息,shoppingCart:[{}]", shoppingCart);

        if (Objects.nonNull(shoppingCart)) {
            // 如果之前的数量为0,则此时加1需要更改is_deleted字段
            shoppingCart.setIsDeleted(0);
            shoppingCart.setAmount(shoppingCart.getAmount() + 1);
            shoppingCartMapper.updateById(shoppingCart);
        } else {
            ShoppingCart newShoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO, newShoppingCart);
            newShoppingCart.setUserId(userId);
            shoppingCartMapper.insert(newShoppingCart);
        }
        ShoppingCartDTO result = getShoppingCartDTOByDishIdOrComboId(shoppingCartDTO, userId);

        log.info("添加购物车信息,result:[{}]", result);

        return result;
    }

    @Override
    public ShoppingCartDTO editShoppingCartAmount(ShoppingCartDTO shoppingCartDTO, Long userId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.eq(shoppingCartDTO.getDishId() != null, ShoppingCart::getDishId, shoppingCartDTO.getDishId());
        wrapper.eq(shoppingCartDTO.getComboId() != null, ShoppingCart::getComboId, shoppingCartDTO.getComboId());
        wrapper.eq(ShoppingCart::getStatus, 1);
        ShoppingCart shoppingCart = shoppingCartMapper.selectOne(wrapper);

        if (Objects.isNull(shoppingCart)) {
            return null;
        }

        int oldAmount = shoppingCart.getAmount();
        int newAmount = oldAmount - 1;
        shoppingCart.setAmount(newAmount);
        if (newAmount == 0){
            shoppingCart.setIsDeleted(1);
        }
        shoppingCartMapper.updateById(shoppingCart);

        return getShoppingCartDTOByDishIdOrComboId(shoppingCartDTO, userId);
    }

    @Override
    public Boolean editShoppingCartStatus(Long userId) {
        log.info("修改购物车信息status,userId:[{}]", userId);

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.eq(ShoppingCart::getStatus, 1);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setStatus(2);

        try {
            shoppingCartMapper.update(shoppingCart, wrapper);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<ShoppingCartDTO> shoppingCartList(Long userId) {
        log.info("条件查询,userId:[{}]", userId);

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.eq(ShoppingCart::getStatus, 1);
        wrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(wrapper);

        log.info("条件查询,shoppingCartList:[{}]", shoppingCartList);

        if (shoppingCartList.size() <= 0) {
            return new ArrayList<>();
        }

        List<ShoppingCartDTO> shoppingCartDTOList = shoppingCartList
                .stream().map(item -> {
                    ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
                    BeanUtils.copyProperties(item, shoppingCartDTO);

                    BigDecimal price;
                    String name;
                    String image;

                    if (shoppingCartDTO.getDishId() != null) {
                        // 根据dishId查找dish的price
                        long id = shoppingCartDTO.getDishId();
                        Dish dish = dishMapper.selectById(id);
                        price = dish.getPrice();
                        name = dish.getName();
                        image = dish.getImage();
                    } else {
                        long id = shoppingCartDTO.getComboId();
                        Combo combo = comboMapper.selectById(id);
                        price = combo.getPrice();
                        name = combo.getName();
                        image = combo.getImage();
                    }

                    shoppingCartDTO.setPrice(price);
                    shoppingCartDTO.setName(name);
                    shoppingCartDTO.setImage(image);

                    return shoppingCartDTO;
                }).collect(Collectors.toList());

        log.info("条件查询,shoppingCartDTOList:[{}]", shoppingCartDTOList);

        return shoppingCartDTOList;
    }

    /**
     * 通过dishId或comboId获取购物车信息
     *
     * @param shoppingCartDTO .
     * @param userId          .
     * @return .
     */
    private ShoppingCartDTO getShoppingCartDTOByDishIdOrComboId(ShoppingCartDTO shoppingCartDTO, Long userId) {
        ShoppingCartDTO result = new ShoppingCartDTO();

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.eq(shoppingCartDTO.getDishId() != null, ShoppingCart::getDishId, shoppingCartDTO.getDishId());
        wrapper.eq(shoppingCartDTO.getComboId() != null, ShoppingCart::getComboId, shoppingCartDTO.getComboId());
        wrapper.eq(ShoppingCart::getStatus, 1);
        wrapper.eq(ShoppingCart::getIsDeleted, 0);

        try {
            ShoppingCart shoppingCart = shoppingCartMapper.selectOne(wrapper);

            long id = shoppingCart.getId();
            ShoppingCart newShoppingCart = shoppingCartMapper.selectById(id);
            BeanUtils.copyProperties(newShoppingCart, result);

            BigDecimal price;
            String name;
            String image;

            if (newShoppingCart.getDishId() != null) {
                Dish dish = dishMapper.selectById(newShoppingCart.getDishId());
                price = dish.getPrice();
                name = dish.getName();
                image = dish.getImage();
            } else {
                Combo combo = comboMapper.selectById(newShoppingCart.getComboId());
                price = combo.getPrice();
                name = combo.getName();
                image = combo.getImage();
            }

            result.setImage(image);
            result.setPrice(price);
            result.setName(name);
        } catch (Exception e) {
            return new ShoppingCartDTO();
        }
        return result;
    }
}