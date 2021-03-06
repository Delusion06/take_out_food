package com.ex.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ex.echo.dto.OrdersDTO;
import com.ex.echo.dto.OrdersInfoDTO;
import com.ex.echo.entity.*;
import com.ex.echo.mapper.*;
import com.ex.echo.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrdersInfoMapper ordersInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private ComboMapper comboMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public Page<OrdersDTO> employeePage(int page, int pageSize, Long id, String beginTime, String endTime) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDTO> ordersDtoPage = new Page<>();

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, Orders::getId, id);
        wrapper.between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime);
        wrapper.eq(Orders::getIsDeleted, 0);
        ordersMapper.selectPage(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");

        List<Orders> ordersList = pageInfo.getRecords();
        List<OrdersDTO> ordersDTOList = getOrdersDTOList(ordersList);

        ordersDtoPage.setRecords(ordersDTOList);
        return ordersDtoPage;
    }

    @Override
    public Page<OrdersDTO> userPage(int page, int pageSize, Long userId) {
        log.info("????????????(??????),page:[{}],pageSize:[{}],userId:[{}]", page, pageSize, userId);

        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDTO> ordersDtoPage = new Page<>();

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, userId);
        wrapper.eq(Orders::getIsDeleted, 0);
        Page<Orders> ordersPage = ordersMapper.selectPage(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");

        List<Orders> ordersList = ordersPage.getRecords();

        List<OrdersDTO> ordersDTOList = getOrdersDTOList(ordersList);

        ordersDtoPage.setRecords(ordersDTOList);
        return ordersDtoPage;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Orders addShoppingCartToOrders(Long userId) {
        log.info("????????????????????????????????????,userId:[{}]", userId);

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.eq(ShoppingCart::getStatus, 1);

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(wrapper);

        log.info("????????????????????????????????????,shoppingCartList:[{}]", shoppingCartList);

        // ?????????????????????status???????????????2
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setStatus(2);
        shoppingCartMapper.update(shoppingCart, wrapper);

        // ??????orders?????????id
        long orderId = IdWorker.getId();
        // ???????????????
        AtomicInteger allPrice = new AtomicInteger(0);
        // OrdersInfo???
        List<OrdersInfo> ordersInfoList = shoppingCartList.stream().map(item -> {
            OrdersInfo ordersInfo = new OrdersInfo();
            BeanUtils.copyProperties(item, ordersInfo);
            ordersInfo.setId(null);
            ordersInfo.setShoppingCartId(item.getId());

            BigDecimal price;
            if (item.getDishId() != null) {
                long id = item.getDishId();
                Dish dish = dishMapper.selectById(id);
                price = dish.getPrice();
            } else {
                long id = item.getComboId();
                Combo combo = comboMapper.selectById(id);
                price = combo.getPrice();
            }
            ordersInfo.setPrice(price);
            ordersInfo.setOrdersId(orderId);

            allPrice.addAndGet(ordersInfo.getPrice().multiply(new BigDecimal(ordersInfo.getAmount())).intValue());

            return ordersInfo;
        }).collect(Collectors.toList());

        log.info("????????????????????????????????????,ordersInfoList:[{}]", ordersInfoList);
        log.info("????????????????????????????????????,allPrice:[{}]", allPrice);

        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setOrdersStatus(1);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPrice(new BigDecimal(allPrice.get()));

        log.info("????????????????????????????????????,orders:[{}]", orders);

        ordersInfoMapper.batchInsert(ordersInfoList);
        ordersMapper.insert(orders);

        log.info("????????????????????????????????????,orders:[{}]", orders);

        return orders;
    }

    @Override
    public Boolean editOrdersOrdersStatus(OrdersDTO ordersDTO, Long employeeId) {
        log.info("??????????????????ordersStatus,ordersDTO:[{}],employeeId:[{}]", ordersDTO, employeeId);

        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersDTO, orders);
        orders.setEmployeeId(employeeId);

        log.info("??????????????????ordersStatus,orders:[{}]", orders);

        try {
            ordersMapper.updateById(orders);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean deleteOrdersToShoppingCart(OrdersDTO ordersDTO, Long userId) {
        log.info("??????????????????????????????????????????,ordersDTO:[{}],userId:[{}]", ordersDTO, userId);

        long ordersId = ordersDTO.getId();

        Orders orders = new Orders();
        orders.setId(ordersId);
        orders.setIsDeleted(1);
        ordersMapper.updateById(orders);

        LambdaQueryWrapper<OrdersInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrdersInfo::getOrdersId, ordersId);
        wrapper.eq(OrdersInfo::getIsDeleted, 0);

        List<OrdersInfo> ordersInfoList = ordersInfoMapper.selectList(wrapper);

        log.info("??????????????????????????????????????????,ordersInfoList:[{}]", ordersInfoList);

        OrdersInfo ordersInfo = new OrdersInfo();
        ordersInfo.setIsDeleted(1);

        // ??????ordersInfo???
        ordersInfoMapper.update(ordersInfo, wrapper);

        List<ShoppingCart> shoppingCartList = ordersInfoList.stream().map(item -> {
            long shoppingCartId = item.getShoppingCartId();
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setId(shoppingCartId);
            return shoppingCart;
        }).collect(Collectors.toList());

        log.info("??????????????????????????????????????????,shoppingCartList:[{}]", shoppingCartList);

        shoppingCartMapper.batchUpdate(shoppingCartList);
        return true;
    }

    @Override
    public OrdersDTO getOrdersById(OrdersDTO ordersDTO, Long userId) {
        log.info("??????id??????????????????,ordersDTO:[{}],userId:[{}]", ordersDTO, userId);

        OrdersDTO result = new OrdersDTO();

        long ordersId = ordersDTO.getId();
        // ??????orders??????
        Orders orders = ordersMapper.selectById(ordersId);
        if (Objects.isNull(orders) || !orders.getUserId().equals(userId) || orders.getIsDeleted() == 1) {
            return new OrdersDTO();
        }
        BeanUtils.copyProperties(orders, result);

        log.info("??????id??????????????????,orders:[{}]", orders);

        // ??????ordersInfo??????
        LambdaQueryWrapper<OrdersInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrdersInfo::getOrdersId, ordersId);
        wrapper.eq(OrdersInfo::getIsDeleted, 0);
        List<OrdersInfo> ordersInfoList = ordersInfoMapper.selectList(wrapper);

        log.info("??????id??????????????????,ordersInfoList:[{}]", ordersInfoList);

        List<OrdersInfoDTO> ordersInfoDTOList = ordersInfoList.stream().map(item -> {
            OrdersInfoDTO ordersInfoDTO = new OrdersInfoDTO();
            BeanUtils.copyProperties(item, ordersInfoDTO);

            if (item.getDishId() != null) {
                Dish dish = dishMapper.selectById(item.getDishId());
                ordersInfoDTO.setName(dish.getName());
                ordersInfoDTO.setImage(dish.getImage());
            } else {
                Combo combo = comboMapper.selectById(item.getComboId());
                ordersInfoDTO.setName(combo.getName());
                ordersInfoDTO.setImage(combo.getImage());
            }
            return ordersInfoDTO;
        }).collect(Collectors.toList());

        log.info("??????id??????????????????,ordersInfoDTOList:[{}]", ordersInfoDTOList);

        result.setOrdersInfoDTOList(ordersInfoDTOList);

        // ??????user??????
        User user = userMapper.selectById(userId);
        result.setUsername(user.getUsername());

        log.info("??????id??????????????????,user:[{}]", user);

        result.setId(orders.getId());

        return result;
    }

    @Override
    public OrdersDTO editOrdersAddressId(OrdersDTO ordersDTO, Long userId) {
        log.info("??????????????????addressId,ordersDTO:[{}]", ordersDTO);
        long ordersId = ordersDTO.getId();

        Orders orders = new Orders();
        orders.setId(ordersId);
        orders.setAddressId(ordersDTO.getAddressId());
        ordersMapper.updateById(orders);

        OrdersDTO result = new OrdersDTO();

        orders = ordersMapper.selectById(ordersId);
        if (Objects.isNull(orders) || !orders.getUserId().equals(userId) || orders.getIsDeleted() == 1) {
            return new OrdersDTO();
        }
        BeanUtils.copyProperties(orders, result);

        LambdaQueryWrapper<OrdersInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrdersInfo::getOrdersId, ordersId);
        wrapper.eq(OrdersInfo::getIsDeleted, 0);
        List<OrdersInfo> ordersInfoList = ordersInfoMapper.selectList(wrapper);

        List<OrdersInfoDTO> ordersInfoDTOList = ordersInfoList.stream().map(item -> {
            OrdersInfoDTO ordersInfoDTO = new OrdersInfoDTO();
            BeanUtils.copyProperties(item, ordersInfoDTO);

            if (item.getDishId() != null) {
                Dish dish = dishMapper.selectById(item.getDishId());
                ordersInfoDTO.setName(dish.getName());
                ordersInfoDTO.setImage(dish.getImage());
            } else {
                Combo combo = comboMapper.selectById(item.getComboId());
                ordersInfoDTO.setName(combo.getName());
                ordersInfoDTO.setImage(combo.getImage());
            }
            return ordersInfoDTO;
        }).collect(Collectors.toList());
        result.setOrdersInfoDTOList(ordersInfoDTOList);

        User user = userMapper.selectById(userId);
        result.setUsername(user.getUsername());

        long addressId = orders.getAddressId();
        Address address = addressMapper.selectById(addressId);
        result.setPhone(address.getPhone());
        result.setDetail(address.getDetail());
        result.setConsignee(address.getConsignee());

        result.setId(orders.getId());

        return result;
    }

    @Override
    public Boolean editOrdersPayStatus(OrdersDTO ordersDTO) {
        log.info("??????????????????payStatus,ordersDTO:[{}]", ordersDTO);

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getId, ordersDTO.getId());
        wrapper.eq(Orders::getIsDeleted, 0);

        Orders orders = new Orders();
        orders.setPayStatus(ordersDTO.getPayStatus());
        if (ordersDTO.getPayStatus() == 2) {
            orders.setCheckoutTime(LocalDateTime.now());
            orders.setOrdersStatus(2);
        }
        ordersMapper.update(orders, wrapper);
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Orders addOrdersAgain(OrdersDTO ordersDTO) {
        // TODO:?????????????????????
        long oldOrdersId = ordersDTO.getId();
        long newOrdersId = IdWorker.getId();
        // ??????orders??????
        Orders oldOrders = ordersMapper.selectById(oldOrdersId);
        Orders newOrders = new Orders();
        BeanUtils.copyProperties(oldOrders, newOrders);
        newOrders.setId(newOrdersId);
        newOrders.setOrderTime(LocalDateTime.now());
        newOrders.setOrdersStatus(1);
        newOrders.setPayStatus(1);
        ordersMapper.insert(newOrders);

        LambdaQueryWrapper<OrdersInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrdersInfo::getOrdersId, oldOrdersId);
        wrapper.eq(OrdersInfo::getIsDeleted, 0);
        List<OrdersInfo> oldOrdersInfoList = ordersInfoMapper.selectList(wrapper);

        List<OrdersInfo> newOrdersInfoList = oldOrdersInfoList.stream().map(item -> {
            OrdersInfo ordersInfo = new OrdersInfo();
            BeanUtils.copyProperties(item, ordersInfo);
            ordersInfo.setId(null);
            ordersInfo.setOrdersId(newOrdersId);
            return ordersInfo;
        }).collect(Collectors.toList());

        ordersInfoMapper.batchInsert(newOrdersInfoList);
        return newOrders;
    }

    @Override
    public Boolean editOrdersStatus(OrdersDTO ordersDTO) {
        Orders orders = new Orders();
        orders.setId(ordersDTO.getId());
        orders.setOrdersStatus(5);

        ordersMapper.updateById(orders);
        return true;
    }

    /**
     * ??????ordersDTOList
     *
     * @param ordersList .
     * @return .
     */
    private List<OrdersDTO> getOrdersDTOList(List<Orders> ordersList) {
        log.info("??????ordersDTOList,ordersList:[{}]", ordersList);

        List<OrdersDTO> ordersDTOList = ordersList.stream().map(item -> {
            OrdersDTO ordersDto = new OrdersDTO();
            BeanUtils.copyProperties(item, ordersDto);

            // ??????ordersInfo?????????
            long orderId = item.getId();
            LambdaQueryWrapper<OrdersInfo> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(OrdersInfo::getOrdersId, orderId);
            wrapper1.eq(OrdersInfo::getIsDeleted, 0);
            List<OrdersInfo> ordersInfoList = ordersInfoMapper.selectList(wrapper1);

            List<OrdersInfoDTO> ordersInfoDTOList = ordersInfoList.stream().map(info -> {
                OrdersInfoDTO ordersInfoDTO = new OrdersInfoDTO();
                BeanUtils.copyProperties(info, ordersInfoDTO);

                if (info.getDishId() != null) {
                    Dish dish = dishMapper.selectById(info.getDishId());
                    ordersInfoDTO.setName(dish.getName());
                } else {
                    Combo combo = comboMapper.selectById(info.getComboId());
                    ordersInfoDTO.setName(combo.getName());
                }
                return ordersInfoDTO;
            }).collect(Collectors.toList());

            ordersDto.setOrdersInfoDTOList(ordersInfoDTOList);

            // ??????user?????????
            long userId = item.getUserId();
            User user = userMapper.selectById(userId);
            if (Objects.nonNull(user)) {
                ordersDto.setUsername(user.getUsername());
            }

            // ??????address?????????
            long addressId = item.getAddressId();
            Address address = addressMapper.selectById(addressId);
            if (Objects.nonNull(address)) {
                ordersDto.setPhone(address.getPhone());
                ordersDto.setDetail(address.getDetail());
                ordersDto.setConsignee(address.getConsignee());
            }

            return ordersDto;
        }).collect(Collectors.toList());

        log.info("??????ordersDTOList,ordersDTOList:[{}]", ordersDTOList);

        return ordersDTOList;
    }
}