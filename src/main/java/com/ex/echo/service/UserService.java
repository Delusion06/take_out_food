package com.ex.echo.service;

import com.ex.echo.dto.UserDTO;
import com.ex.echo.entity.User;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param userDTO .
     * @return .
     */
    public User userLogin(UserDTO userDTO);

    /**
     * 添加用户信息
     *
     * @param userDTO .
     * @return .
     */
    public Boolean addUser(UserDTO userDTO);

    /**
     * 通过手机号查询用户信息
     *
     * @param userDTO .
     * @return .
     */
    public User getUserByPhone(UserDTO userDTO);

    /**
     * 根据id查询用户信息
     *
     * @param userId .
     * @return .
     */
    public UserDTO getUserById(Long userId);

    /**
     * 修改密码
     * password
     *
     * @param userDTO .
     * @param userId  .
     * @return .
     */
    public Boolean editPassword(UserDTO userDTO, Long userId);

    /**
     * 修改用户信息
     * username、sex、idCard
     *
     * @param userDTO .
     * @param userId  .
     * @return .
     */
    public Boolean editUser(UserDTO userDTO, Long userId);

}