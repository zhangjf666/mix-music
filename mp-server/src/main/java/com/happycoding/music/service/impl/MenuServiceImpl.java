package com.happycoding.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happycoding.music.common.base.BaseServiceImpl;
import com.happycoding.music.common.constants.Constants;
import com.happycoding.music.common.model.Page;
import com.happycoding.music.common.utils.QueryUtil;
import com.happycoding.music.dto.MenuDto;
import com.happycoding.music.dto.MenuQueryDto;
import com.happycoding.music.dto.vo.MenuVo;
import com.happycoding.music.entity.Menu;
import com.happycoding.music.mapper.MenuMapper;
import com.happycoding.music.mapstruct.MenuMapstruct;
import com.happycoding.music.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2020-06-11
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends BaseServiceImpl<MenuMapstruct, MenuDto, MenuMapper, Menu> implements MenuService {

    @Override
    public Page<MenuDto> queryPage(MenuQueryDto queryDto, Page page) {
        QueryUtil.queryTreeRootCheck(queryDto);
        Page rpage = Page.fromMybatisPlusPage(baseMapper.selectPage(page.toMybatisPlusPage(), QueryUtil.bulid(queryDto)));
        rpage.setRecord(baseMapstruct.toDto(rpage.getRecord()));
        return rpage;
    }

    @Override
    public List<MenuDto> query(MenuQueryDto queryDto) {
        return baseMapstruct.toDto(baseMapper.selectList(QueryUtil.bulid(queryDto)));
    }

    @Override
    public Set<MenuDto> getMenusByRoleId(Set<String> roleId) {
        LinkedHashSet<Menu> menus = baseMapper.getMenusByRoleId(roleId);
        return menus.stream().map(baseMapstruct::toDto).collect(Collectors.toSet());
    }

    @Override
    public List<MenuDto> getSuperior(MenuDto dto, List<Menu> menus) {
        if(dto.getPid().equals(Constants.TREE_ROOT)){
            menus.addAll(baseMapper.selectList(new QueryWrapper<Menu>().eq("pid", Constants.TREE_ROOT)));
            return baseMapstruct.toDto(menus);
        }
        menus.addAll(baseMapper.selectList(new QueryWrapper<Menu>().eq("pid",dto.getPid())));
        return getSuperior(queryById(dto.getPid()), menus);
    }

    @Override
    public void superiorCheckChildren(List<MenuVo> trees) {
        Queue<MenuVo> queue = new ArrayDeque<>(trees.size());
        queue.addAll(trees);

        while (!queue.isEmpty()){
            MenuVo tree = queue.remove();
            List<MenuVo> childrens = tree.getChildren();
            if(childrens == null || childrens.size() <= 0){
                tree.setHasChildren(baseMapper.selectCount(new QueryWrapper<Menu>().eq("pid",tree.getId())) > 0);
            } else {
                queue.addAll(childrens);
            }
        }
    }
}
