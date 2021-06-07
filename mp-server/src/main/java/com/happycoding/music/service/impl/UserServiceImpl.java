package com.happycoding.music.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.happycoding.music.common.base.BaseServiceImpl;
import com.happycoding.music.common.exception.DataNotExsitException;
import com.happycoding.music.common.model.Page;
import com.happycoding.music.common.utils.QueryUtil;
import com.happycoding.music.dto.RegisterUserDto;
import com.happycoding.music.dto.RoleDto;
import com.happycoding.music.dto.UserDto;
import com.happycoding.music.dto.UserQueryDto;
import com.happycoding.music.entity.Role;
import com.happycoding.music.entity.User;
import com.happycoding.music.entity.UserConfig;
import com.happycoding.music.entity.UserRole;
import com.happycoding.music.mapper.UserConfigMapper;
import com.happycoding.music.mapper.UserMapper;
import com.happycoding.music.mapper.UserRoleMapper;
import com.happycoding.music.mapstruct.UserMapstruct;
import com.happycoding.music.service.RoleService;
import com.happycoding.music.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2020-06-11
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<UserMapstruct, UserDto, UserMapper, User> implements UserService {
    private static String DEFAULT_PASSWORD = "123456";

    private final UserRoleMapper userRoleMapper;
    
    private final RoleService roleService;

    private final UserConfigMapper userConfigMapper;

    @Override
    public UserDto findByUsername(String username) {
        User user = baseMapper.findByUsername(username);
        if(user == null){
            throw new DataNotExsitException();
        }
        return baseMapstruct.toDto(user);
    }

    @Override
    public Page<UserDto> queryPage(UserQueryDto queryDto, Page page) {
        Page rpage = Page.fromMybatisPlusPage(baseMapper.selectPage(page.toMybatisPlusPage(), QueryUtil.bulid(queryDto)));
        rpage.setRecord(baseMapstruct.toDto(rpage.getRecord()));
        return rpage;
    }

    @Override
    public List<UserDto> query(UserQueryDto queryDto) {
        return baseMapstruct.toDto(baseMapper.selectList(QueryUtil.bulid(queryDto)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserDto queryById(Serializable id) {
        UserDto dto = super.queryById(id);
        dto.setRoles(roleService.getRoleByUserId((Long) id));
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserDto create(UserDto dto) {
        User user = baseMapstruct.toEntity(dto);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        baseMapper.insert(user);
        for (RoleDto roleDto:dto.getRoles()) {
            UserRole ur = new UserRole(user.getId(),roleDto.getId());
            userRoleMapper.insert(ur);
        }
        return baseMapstruct.toDto(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(UserDto dto) {
        baseMapper.updateById(baseMapstruct.toEntity(dto));
        if(dto.getRoles() != null){
            userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId,dto.getId()));
            for (RoleDto roleDto:dto.getRoles()) {
                UserRole ur = new UserRole(dto.getId(),roleDto.getId());
                userRoleMapper.insert(ur);
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteById(String id) {
        baseMapper.deleteById(id);
        userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, id));
        return true;
    }

    @Override
    public boolean checkExist(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUsername, userName.trim())) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean registerUser(RegisterUserDto dto) {
        Role normalRole = roleService.getOne(Wrappers.<Role>lambdaQuery().eq(Role::getName,"普通用户"));
        User user = new User();
        user.setUsername(dto.getUserName());
        user.setNickName(RandomUtil.randomString(12));
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        baseMapper.insert(user);

        UserRole userRole = new UserRole(user.getId(), normalRole.getId());
        userRoleMapper.insert(userRole);

        UserConfig userConfig = new UserConfig();
        userConfig.setId(user.getId());
        userConfig.setPlayMode("1");
        userConfigMapper.insert(userConfig);
        return true;
    }
}
