package com.happycoding.music.service.impl;

import com.happycoding.music.entity.Singer;
import com.happycoding.music.mapper.SingerMapper;
import com.happycoding.music.service.SingerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌手信息表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Service
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer> implements SingerService {

}
