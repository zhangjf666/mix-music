package com.happycoding.music.config.security;

import cn.hutool.core.bean.BeanUtil;
import com.happycoding.music.common.constants.Constants;
import com.happycoding.music.dto.CacheUser;
import com.happycoding.music.dto.JwtUserDetails;
import com.happycoding.music.dto.UserDto;
import com.happycoding.music.service.RoleService;
import com.happycoding.music.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/8 16:50
 */
@RequiredArgsConstructor
@Component
public class UserDetailsImpl implements UserDetailsService {

    private final UserService userService;

    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDto userDto;
        try{
            userDto = userService.findByUsername(s);
        } catch (Exception e){
            throw new UsernameNotFoundException("",e);
        }
        if (!userDto.getEnabled().equals(Constants.BOOL_TRUE)) {
            throw new UsernameNotFoundException("账号未激活");
        }
        CacheUser cacheUser = BeanUtil.copyProperties(userDto,CacheUser.class);
        cacheUser.setAuthorities(roleService.getUserGrantedAuthority(userDto));

        return new JwtUserDetails(cacheUser,cacheUser.getAuthorities());
    }
}
