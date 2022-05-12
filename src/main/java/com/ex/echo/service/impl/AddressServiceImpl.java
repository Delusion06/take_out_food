package com.ex.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ex.echo.dto.AddressDTO;
import com.ex.echo.entity.Address;
import com.ex.echo.mapper.AddressMapper;
import com.ex.echo.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public AddressDTO getAddressById(AddressDTO addressDTO) {
        log.info("根据id查询地址信息,addressDTO:[{}]", addressDTO);

        if (addressDTO.getId() == null) {
            return new AddressDTO();
        }

        long id = addressDTO.getId();
        Address address = addressMapper.selectById(id);
        AddressDTO result = new AddressDTO();
        BeanUtils.copyProperties(address, result);

        log.info("根据id查询地址信息,result:[{}]", result);

        return result;
    }

    @Override
    public AddressDTO getAddressIsDefault(Long userId) {
        log.info("查询默认地址,userId:[{}]", userId);

        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.eq(Address::getIsDefault, 1);
        wrapper.eq(Address::getIsDeleted, 0);

        Address address = addressMapper.selectOne(wrapper);

        log.info("查询默认地址,address:[{}]", address);

        AddressDTO result = new AddressDTO();
        BeanUtils.copyProperties(address, result);

        return result;
    }

    @Override
    public Boolean addAddress(AddressDTO addressDTO, Long userId) {
        log.info("添加地址信息,addressDTO:[{}],userId:[{}]", addressDTO, userId);

        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        address.setUserId(userId);

        log.info("添加地址信息,address:[{}]", address);
        try {
            addressMapper.insert(address);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean editAddress(AddressDTO addressDTO, Long userId) {
        log.info("修改地址信息,addressDTO:[{}],userId:[{}]", addressDTO, userId);

        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        if (address.getId() == null) {
            return false;
        }

        log.info("修改地址信息,address:[{}]", address);

        try {
            addressMapper.updateById(address);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean editAddressIsDefault(AddressDTO addressDTO, Long userId) {
        log.info("修改地址信息is_default,adddressDTO:[{}],userId:[{}]", addressDTO, userId);

        if (addressDTO.getId() == null) {
            return false;
        }

        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.eq(Address::getIsDefault, 1);
        wrapper.eq(Address::getIsDeleted, 0);

        Address oldAddress = new Address();
        oldAddress.setIsDefault(0);

        Address newAddress = new Address();
        newAddress.setId(addressDTO.getId());
        newAddress.setIsDefault(1);
        newAddress.setUpdateTime(LocalDateTime.now());

        addressMapper.update(oldAddress, wrapper);
        addressMapper.updateById(newAddress);
        return true;
    }

    @Override
    public Boolean editAddressIsDeleted(AddressDTO addressDTO, Long userId) {
        log.info("修改地址信息is_deleted,addressDTO:[{}],userId:[{}]",addressDTO,userId);

        if (addressDTO.getId() == null){
            return false;
        }

        Address address = new Address();
        BeanUtils.copyProperties(addressDTO,address);
        address.setIsDeleted(1);

        addressMapper.updateById(address);
        return true;
    }

    @Override
    public List<AddressDTO> addressList(Long userId) {
        log.info("条件查询,userId:[{}]", userId);

        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != userId, Address::getUserId, userId);
        wrapper.eq(Address::getIsDeleted, 0);
        wrapper.orderByDesc(Address::getUpdateTime);

        List<Address> addressList = addressMapper.selectList(wrapper);

        log.info("条件查询,addressList:[{}]", addressList);
        if (addressList.size() <= 0) {
            return new ArrayList<>();
        }

        List<AddressDTO> addressDTOList = addressList.stream().map(item -> {
            AddressDTO addressDTO = new AddressDTO();
            BeanUtils.copyProperties(item, addressDTO);
            return addressDTO;
        }).collect(Collectors.toList());

        log.info("条件查询,addressDTOList:[{}]", addressDTOList);

        return addressDTOList;
    }
}