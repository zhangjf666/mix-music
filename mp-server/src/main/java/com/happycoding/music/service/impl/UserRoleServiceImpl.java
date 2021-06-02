package com.happycoding.music.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happycoding.music.entity.UserRole;
import com.happycoding.music.mapper.UserRoleMapper;
import com.happycoding.music.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-角色表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2020-06-11
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
