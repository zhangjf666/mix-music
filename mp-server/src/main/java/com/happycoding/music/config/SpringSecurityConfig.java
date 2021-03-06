package com.happycoding.music.config;

import com.happycoding.music.common.annotation.Anonymous;
import com.happycoding.music.config.security.JwtAccessDeniedHandler;
import com.happycoding.music.config.security.JwtAuthenticationEntryPoint;
import com.happycoding.music.config.security.JwtTokenFilter;
import com.happycoding.music.service.impl.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/9 16:08
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtService jwtService;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ??????????????????
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@Anonymous?????????url
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            Anonymous anonymousAccess = handlerMethod.getMethodAnnotation(Anonymous.class);
            if (null != anonymousAccess) {
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }
        //??????X-Frame-Options
        http.headers().frameOptions().disable();
        //????????????
        http.csrf().disable().addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
        //??????jwt,?????????????????????
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //???????????????????????????
        http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler);
        //jwtFilter
        http.addFilterBefore(new JwtTokenFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        //??????url
        http.authorizeRequests()
                // ??????????????????
                .antMatchers(
                        HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/webSocket/**"
                ).permitAll()
                // swagger ??????
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/*/api-docs").permitAll()
                // ??????
                .antMatchers("/avatar/**").permitAll()
                .antMatchers("/file/**").permitAll()
                // ???????????? druid
                .antMatchers("/druid/**").permitAll()
                // ??????OPTIONS??????
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // ???????????????????????????url?????? ??? ????????????????????????????????????????????????
                .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                // ???????????????????????????
                .anyRequest().authenticated();
    }
}
