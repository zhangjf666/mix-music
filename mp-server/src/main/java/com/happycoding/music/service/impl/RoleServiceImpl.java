package com.happycoding.music.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.happycoding.music.common.base.BaseServiceImpl;
import com.happycoding.music.common.constants.Constants;
import com.happycoding.music.common.model.Page;
import com.happycoding.music.common.utils.QueryUtil;
import com.happycoding.music.config.security.PermissionGrantedAuthority;
import com.happycoding.music.dto.MenuDto;
import com.happycoding.music.dto.RoleDto;
import com.happycoding.music.dto.RoleQueryDto;
import com.happycoding.music.dto.UserDto;
import com.happycoding.music.entity.Role;
import com.happycoding.music.entity.RoleMenu;
import com.happycoding.music.mapper.RoleMapper;
import com.happycoding.music.mapper.RoleMenuMapper;
import com.happycoding.music.mapstruct.RoleMapstruct;
import com.happycoding.music.service.MenuService;
import com.happycoding.music.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2020-06-11
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends BaseServiceImpl<RoleMapstruct, RoleDto, RoleMapper, Role> implements RoleService {

    private final RoleMenuMapper roleMenuMapper;

    private final MenuService menuService;


    @Override
    public Set<Role> getUserRoles(Long userId) {
        return baseMapper.getUserRoles(userId);
    }

    @Override
    public List<GrantedAuthority> getUserGrantedAuthority(UserDto userDto) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if (Constants.BOOL_TRUE.equals(userDto.getType())) {
            permissions.add("admin");
            return permissions.stream().map(PermissionGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        Set<Long> roleIds = userDto.getRoles().stream().map(RoleDto::getId).collect(Collectors.toSet());
        Set<MenuDto> menus = menuService.getMenusByRoleId(roleIds);
        permissions = menus.stream().filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                .map(MenuDto::getPermission).collect(Collectors.toSet());
        return permissions.stream().map(PermissionGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RoleDto> queryPage(RoleQueryDto queryDto, Page page) {
        Page rpage = Page.fromMybatisPlusPage(baseMapper.selectPage(page.toMybatisPlusPage(),
                QueryUtil.bulid(queryDto)));
        baseMapstruct.toDto(rpage.getRecord());
        return rpage;
    }

    @Override
    public List<RoleDto> query(RoleQueryDto queryDto) {
        return baseMapstruct.toDto(baseMapper.selectList(QueryUtil.bulid(queryDto)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleDto queryById(Serializable id) {
        RoleDto dto = super.queryById(id);
        dto.setMenus(menuService.getMenusByRoleId(Collections.singleton(dto.getId())));
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean create(RoleDto dto) {
        Role role = baseMapstruct.toEntity(dto);
        baseMapper.insert(role);
        for (MenuDto menuDto : dto.getMenus()) {
            RoleMenu rm = new RoleMenu(role.getId(), menuDto.getId());
            roleMenuMapper.insert(rm);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(RoleDto dto) {
        Role role = baseMapstruct.toEntity(dto);
        baseMapper.updateById(role);
        roleMenuMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, dto.getId()));
        for (MenuDto menuDto : dto.getMenus()) {
            RoleMenu rm = new RoleMenu(role.getId(), menuDto.getId());
            roleMenuMapper.insert(rm);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteById(Long id) {
        baseMapper.deleteById(id);
        roleMenuMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, id));
        return true;
    }

    @Override
    public Set<RoleDto> getRoleByUserId(Long userId) {
        Set<Role> roles = baseMapper.getRoleByUserId(userId);
        return roles.stream().map(baseMapstruct::toDto).collect(Collectors.toSet());
    }
}
