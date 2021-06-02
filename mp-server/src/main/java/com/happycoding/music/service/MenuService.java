package com.happycoding.music.service;

import com.happycoding.music.common.base.BaseService;
import com.happycoding.music.common.model.Page;
import com.happycoding.music.dto.MenuDto;
import com.happycoding.music.dto.MenuQueryDto;
import com.happycoding.music.dto.vo.MenuVo;
import com.happycoding.music.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zjf
 * @since 2020-06-11
 */
public interface MenuService extends BaseService<MenuDto, Menu> {

    /**
     * 分页查询
     * @param queryDto 查询条件
     * @param page 分页
     * @return
     */
    Page queryPage(MenuQueryDto queryDto, Page page);

    /**
     * 查询
     * @param queryDto 查询条件
     * @return
     */
    List<MenuDto> query(MenuQueryDto queryDto);

    /**
     * 根据角色id获取,角色对应的菜单
     * @param roleId 角色id
     * @return
     */
    Set<MenuDto> getMenusByRoleId(@Param("roleId") Set<Long> roleId);

    /**
     * 获取指定节点的所有上级节点
     * @param dto 指定节点
     * @param menus 缓存节点列表
     * @return
     */
    List<MenuDto> getSuperior(MenuDto dto, List<Menu> menus);

    /**
     * 判断是否有子节点
     * @param trees
     */
    void superiorCheckChildren(List<MenuVo> trees);
}
