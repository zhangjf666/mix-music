package com.happycoding.music.dto;

import com.happycoding.music.common.base.BaseDto;
import com.happycoding.music.common.support.valid.Insert;
import com.happycoding.music.common.support.valid.Update;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/8 16:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDto extends BaseDto {
    @ApiModelProperty(value = "id")
    @NotNull(groups = {Update.class},message = "角色id不能为空")
    private String id;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(groups = Insert.class,message = "角色名称不能为空")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "菜单")
    private Set<MenuDto> menus;
}
