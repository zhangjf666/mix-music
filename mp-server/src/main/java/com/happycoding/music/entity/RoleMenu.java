package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 角色-菜单表
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_menu")
@ApiModel(value="RoleMenu对象", description="角色-菜单表")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "菜单id")
    private String menuId;

    public RoleMenu(String roleId, String menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
