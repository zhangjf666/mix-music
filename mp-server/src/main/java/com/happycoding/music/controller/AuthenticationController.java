package com.happycoding.music.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import com.happycoding.music.common.annotation.Anonymous;
import com.happycoding.music.common.exception.BusinessException;
import com.happycoding.music.common.model.Response;
import com.happycoding.music.common.support.valid.Insert;
import com.happycoding.music.common.utils.RedisUtil;
import com.happycoding.music.common.utils.SpringSecurityUtil;
import com.happycoding.music.config.properties.SystemProperties;
import com.happycoding.music.config.security.JwtProperties;
import com.happycoding.music.dto.*;
import com.happycoding.music.mapstruct.RoleMapstruct;
import com.happycoding.music.service.RoleService;
import com.happycoding.music.service.UserService;
import com.happycoding.music.service.impl.JwtService;
import com.happycoding.music.service.impl.OnlineUserService;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.happycoding.music.common.constants.Constants.CAPTCHA_KEY;


/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/8 15:00
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "??????????????????")
public class AuthenticationController {

    private final JwtProperties properties;
    private final OnlineUserService onlineUserService;
    private final JwtService jwtService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SystemProperties systemProperties;
    private final UserService userService;
    private final RoleService roleService;
    private final RoleMapstruct roleMapstruct;

    @ApiOperation("????????????")
    @Anonymous
    @PostMapping("/login")
    public Response login(@Validated @RequestBody LoginUserDto loginUserDto, HttpServletRequest request) {
        //???????????????
        checkCaptcha(loginUserDto);
        //????????????
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // ????????????
        String token = jwtService.createToken(authentication);
        final JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        // ??????????????????
        onlineUserService.save(jwtUserDetails, token, request);
        //??????????????????,??????????????????
        if(systemProperties.getSingleLogin()){
            onlineUserService.checkLoginOnUser(jwtUserDetails.getUsername(),token);
        }
        // ?????? token ??? ????????????
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", BeanUtil.copyProperties(jwtUserDetails.getUser(), UserDto.class));
        }};
        return Response.ok(authInfo);
    }

    private void checkCaptcha(@Validated LoginUserDto loginUserDto) {
        if(!systemProperties.getEnableCaptcha()){
            return;
        }
        //???????????????
        String captcha = (String) RedisUtil.get(CAPTCHA_KEY + loginUserDto.getUuid());
        if (StringUtils.isBlank(captcha)) {
            throw new BusinessException("??????????????????");
        }
        //???????????????
        RedisUtil.del(CAPTCHA_KEY + loginUserDto.getUuid());
        //???????????????
        if (StringUtils.isBlank(loginUserDto.getCaptcha()) && captcha.equalsIgnoreCase(loginUserDto.getCaptcha())) {
            throw new BusinessException("???????????????");
        }
    }

    @Anonymous
    @ApiOperation("???????????????")
    @GetMapping("/captcha")
    public Response getCaptcha() {
        //???????????????
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String captcha = specCaptcha.text().toLowerCase();
        String uuid = IdUtil.simpleUUID();
        //???????????????
        RedisUtil.set(CAPTCHA_KEY + uuid, captcha);
        Map<String, Object> res = new HashMap<>(2);
        res.put("img", specCaptcha.toBase64());
        res.put("uuid", uuid);
        return Response.ok(res);
    }

    @ApiOperation("????????????")
    @Anonymous
    @PostMapping("/register")
    public Response create(@Validated(Insert.class) @RequestBody RegisterUserDto dto){
        if(userService.checkExist(dto.getUsername())) {
            throw new BusinessException("??????????????????");
        }
        if(!dto.getPassword().equals(dto.getRepeatPassword())){
            throw new BusinessException("2??????????????????");
        }
        userService.registerUser(dto);
        return Response.ok();
    }

    @ApiOperation("??????????????????????????????")
    @Anonymous
    @PostMapping("/check-user")
    public Response checkUserExist(@RequestBody UserQueryDto dto){
        return Response.ok(userService.checkExist(dto.getUsername()));
    }

    @ApiOperation("????????????????????????")
    @PostMapping("/user-info")
    public Response getCacheUser() {
        CacheUser cacheUser = ((JwtUserDetails) SpringSecurityUtil.getCurrentUser()).getUser();
        List<String> userPermissions = cacheUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Response.ok(Dict.create().set("user",BeanUtil.copyProperties(cacheUser, UserDto.class))
                .set("permission",userPermissions));
    }

    @ApiOperation("????????????????????????")
    @PostMapping("/user-permission")
    public Response getUserPermission() {
        List<String> userPermissions = SpringSecurityUtil.getCurrentUser().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Response.ok(userPermissions);
    }

    @ApiOperation("????????????")
    @PostMapping("/logout")
    public Response logout(HttpServletRequest request) {
        onlineUserService.logout(jwtService.getToken(request));
        return Response.ok();
    }

    @ApiOperation("??????????????????")
    @GetMapping("/online")
    @PreAuthorize("@ph.check()")
    public Response getOnlineUser(String username, Pageable pageable){
        return Response.ok(onlineUserService.getAll(username, pageable));
    }

    @ApiOperation("????????????")
    @PostMapping("/online-kickout")
    @PreAuthorize("@ph.check()")
    public Response kickout(String userKeys){
        String[] uks = userKeys.split(",");
        for (String key : uks) {
            onlineUserService.kickOut(key);
        }
        return Response.ok();
    }
}
