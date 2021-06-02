package com.happycoding.music.service;

import com.happycoding.music.common.base.BaseService;
import com.happycoding.music.common.model.Page;
import com.happycoding.music.dto.UserDto;
import com.happycoding.music.dto.UserQueryDto;
import com.happycoding.music.entity.User;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zjf
 * @since 2020-06-11
 */
public interface UserService extends BaseService<UserDto, User> {

    /**
     * 用户名查询用户Dto
     * @param username 用户名 
     * @return
     */
    UserDto findByUsername(String username);

    /**
     * 分页查询
     * @param queryDto 查询条件
     * @param page 分页
     * @return
     */
    Page queryPage(UserQueryDto queryDto, Page page);



    /**
     * 查询
     * @param queryDto 查询条件
     * @return
     */
    List<UserDto> query(UserQueryDto queryDto);

    @Override
    UserDto queryById(Serializable id);

    /**
     * 保存用户
     * @param dto 用户Dto
     * @return
     */
    @Override
    boolean create(UserDto dto);

    /**
     * 更新用户
     * @param dto 用户Dto
     * @return
     */
    @Override
    boolean update(UserDto dto);

    /**
     * 删除用户
     * @param id 用户id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 查询用户名是否已存在
     * @param userName 用户名
     * @return
     */
    boolean checkExist(String userName);
}
