package com.happycoding.music.service.impl;

import com.happycoding.music.entity.UserConfig;
import com.happycoding.music.mapper.UserConfigMapper;
import com.happycoding.music.service.UserConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户配置表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig> implements UserConfigService {

}
