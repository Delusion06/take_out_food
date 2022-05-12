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
        log.info("分页查询(用户),page:[{}],pageSize:[{}],userId:[{}]", page, pageSize, userId);

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
        log.info("将购物车信息生成订单信息,userId:[{}]", userId);

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.eq(ShoppingCart::getStatus, 1);

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(wrapper);

        log.info("将购物车信息生成订单信息,shoppingCartList:[{}]", shoppingCartList);

        // 将购物车信息的status字段设置为2
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setStatus(2);
        shoppingCartMapper.update(shoppingCart, wrapper);

        // 生成orders的主键id
        long orderId = IdWorker.getId();
        // 获取总金额
        AtomicInteger allPrice = new AtomicInteger(0);
        // OrdersInfo表
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

        log.info("将购物车信息生成订单信息,ordersInfoList:[{}]", ordersInfoList);
        log.info("将购物车信息生成订单信息,allPrice:[{}]", allPrice);

        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setOrdersStatus(1);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPrice(new BigDecimal(allPrice.get()));

        log.info("将购物车信息生成订单信息,orders:[{}]", orders);

        ordersInfoMapper.batchInsert(ordersInfoList);
        ordersMapper.insert(orders);

        log.info("将购物车信息生成订单信息,orders:[{}]", orders);

        return orders;
    }

    @Override
    public Boolean editOrdersOrdersStatus(OrdersDTO ordersDTO, Long employeeId) {
        log.info("修改订单信息ordersStatus,ordersDTO:[{}],employeeId:[{}]", ordersDTO, employeeId);

        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersDTO, orders);
        orders.setEmployeeId(employeeId);

        log.info("修改订单信息ordersStatus,orders:[{}]", orders);

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
        log.info("将订单中的数据返回到购物车中,ordersDTO:[{}],userId:[{}]", ordersDTO, userId);

        long ordersId = ordersDTO.getId();

        Orders orders = new Orders();
        orders.setId(ordersId);
        orders.setIsDeleted(1);
        ordersMapper.updateById(orders);

        LambdaQueryWrapper<OrdersInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrdersInfo::getOrdersId, ordersId);
        wrapper.eq(OrdersInfo::getIsDeleted, 0);

        List<OrdersInfo> ordersInfoList = ordersInfoMapper.selectList(wrapper);

        log.info("将订单中的数据返回到购物车中,ordersInfoList:[{}]", ordersInfoList);

        OrdersInfo ordersInfo = new OrdersInfo();
        ordersInfo.setIsDeleted(1);

        // 修改ordersInfo表
        ordersInfoMapper.update(ordersInfo, wrapper);

        List<ShoppingCart> shoppingCartList = ordersInfoList.stream().map(item -> {
            long shoppingCartId = item.getShoppingCartId();
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setId(shoppingCartId);
            return shoppingCart;
        }).collect(Collectors.toList());

        log.info("将订单中的数据返回到购物车中,shoppingCartList:[{}]", shoppingCartList);

        shoppingCartMapper.batchUpdate(shoppingCartList);
        return true;
    }

    @Override
    public OrdersDTO getOrdersById(OrdersDTO ordersDTO, Long userId) {
        log.info("根据id获取订单信息,ordersDTO:[{}],userId:[{}]", ordersDTO, userId);

        OrdersDTO result = new OrdersDTO();

        long ordersId = ordersDTO.getId();
        // 获取orders信息
        Orders orders = ordersMapper.selectById(ordersId);
        if (Objects.isNull(orders) || !orders.getUserId().equals(userId) || orders.getIsDeleted() == 1) {
            return new OrdersDTO();
        }
        BeanUtils.copyProperties(orders, result);

        log.info("根据id获取订单信息,orders:[{}]", orders);

        // 获取ordersInfo信息
        LambdaQueryWrapper<OrdersInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrdersInfo::getOrdersId, ordersId);
        wrapper.eq(OrdersInfo::getIsDeleted, 0);
        List<OrdersInfo> ordersInfoList = ordersInfoMapper.selectList(wrapper);

        log.info("根据id获取订单信息,ordersInfoList:[{}]", ordersInfoList);

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

        log.info("根据id获取订单信息,ordersInfoDTOList:[{}]", ordersInfoDTOList);

        result.setOrdersInfoDTOList(ordersInfoDTOList);

        // 获取user信息
        User user = userMapper.selectById(userId);
        result.setUsername(user.getUsername());

        log.info("根据id获取订单信息,user:[{}]", user);

        result.setId(orders.getId());

        return result;
    }

    @Override
    public OrdersDTO editOrdersAddressId(OrdersDTO ordersDTO, Long userId) {
        log.info("修改订单信息addressId,ordersDTO:[{}]", ordersDTO);
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
        log.info("修改订单信息payStatus,ordersDTO:[{}]", ordersDTO);

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
        // TODO:清空购物车数据
        long oldOrdersId = ordersDTO.getId();
        long newOrdersId = IdWorker.getId();
        // 获取orders信息
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
     * 获取ordersDTOList
     *
     * @param ordersList .
     * @return .
     */
    private List<OrdersDTO> getOrdersDTOList(List<Orders> ordersList) {
        log.info("获取ordersDTOList,ordersList:[{}]", ordersList);

        List<OrdersDTO> ordersDTOList = ordersList.stream().map(item -> {
            OrdersDTO ordersDto = new OrdersDTO();
            BeanUtils.copyProperties(item, ordersDto);

            // 获取ordersInfo表信息
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

            // 获取user表信息
            long userId = item.getUserId();
            User user = userMapper.selectById(userId);
            if (Objects.nonNull(user)) {
                ordersDto.setUsername(user.getUsername());
            }

            // 获取address表信息
            long addressId = item.getAddressId();
            Address address = addressMapper.selectById(addressId);
            if (Objects.nonNull(address)) {
                ordersDto.setPhone(address.getPhone());
                ordersDto.setDetail(address.getDetail());
                ordersDto.setConsignee(address.getConsignee());
            }

            return ordersDto;
        }).collect(Collectors.toList());

        log.info("获取ordersDTOList,ordersDTOList:[{}]", ordersDTOList);

        return ordersDTOList;
    }
}