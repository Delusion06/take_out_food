package com.ex.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ex.echo.dto.UserDTO;
import com.ex.echo.entity.User;
import com.ex.echo.mapper.UserMapper;
import com.ex.echo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Objects;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User userLogin(UserDTO userDTO) {
        if (StringUtils.isEmpty(userDTO.getPassword())) {
            return null;
        }
        String password = DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes());

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(userDTO.getPhone()), User::getPhone, userDTO.getPhone());
        User result = userMapper.selectOne(wrapper);

        if (Objects.isNull(result) || !result.getPassword().equals(password)) {
            return null;
        }
        return result;
    }

    @Override
    public Boolean addUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        String password = DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes());
        user.setPassword(password);
        try {
            userMapper.insert(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public User getUserByPhone(UserDTO userDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(userDTO.getPhone()), User::getPhone, userDTO.getPhone());
        wrapper.eq(User::getStatus, 1);
        try {
            User user = userMapper.selectOne(wrapper);
            if (Objects.isNull(user)) {
                return null;
            }
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserDTO getUserById(Long userId) {
        log.info("根据id查询用户信息,userId:[{}]", userId);

        UserDTO result = new UserDTO();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        wrapper.eq(User::getStatus, 1);

        User user = userMapper.selectOne(wrapper);
        if (Objects.isNull(user)) {
            return result;
        }

        log.info("根据id查询用户信息,user:[{}]", user);

        BeanUtils.copyProperties(user, result);
        return result;
    }

    @Override
    public Boolean editPassword(UserDTO userDTO, Long userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(userDTO.getPhone()), User::getPhone, userDTO.getPhone());
        wrapper.eq(User::getStatus, 1);
        try {
            User user = userMapper.selectOne(wrapper);
            // 查询不到user数据或者userId不一致
            if (Objects.isNull(user) || !user.getId().equals(userId)) {
                return false;
            }
            String password = DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes());
            user.setPassword(password);
            userMapper.updateById(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Boolean editUser(UserDTO userDTO, Long userId) {
        log.info("修改用户信息,userDTO:[{}],userId:[{}]",userDTO,userId);
        User user = userMapper.selectById(userId);
        user.setUsername(userDTO.getUsername());
        user.setSex(userDTO.getSex());
        if (StringUtils.isNotEmpty(userDTO.getIdCard())) {
            user.setIdCard(userDTO.getIdCard());
        }
        try {
            userMapper.updateById(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}