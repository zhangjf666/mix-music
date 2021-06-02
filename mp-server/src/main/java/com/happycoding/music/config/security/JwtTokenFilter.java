package com.happycoding.music.config.security;

import cn.hutool.core.util.StrUtil;
import com.happycoding.music.common.utils.SpringContextUtil;
import com.happycoding.music.dto.OnlineUserDto;
import com.happycoding.music.service.impl.JwtService;
import com.happycoding.music.service.impl.OnlineUserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.happycoding.music.common.constants.Constants.ONLINE_USER_KEY;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/9 9:37
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtService jwtService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if(StrUtil.isNotBlank(token)){
            OnlineUserDto onlineUserDto = null;
            try {
                OnlineUserService onlineUserService = SpringContextUtil.getBean(OnlineUserService.class);
                onlineUserDto = onlineUserService.getOne(ONLINE_USER_KEY + token);
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
            }
            if (onlineUserDto != null) {
                Authentication authentication = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Token 续期
                jwtService.checkRenewal(token);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        JwtProperties properties = SpringContextUtil.getBean(JwtProperties.class);
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(properties.getTokenStartWith(),"");
        }
        return null;
    }
}
