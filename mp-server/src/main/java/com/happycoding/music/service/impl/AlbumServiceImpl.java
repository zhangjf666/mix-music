package com.happycoding.music.service.impl;

import com.happycoding.music.entity.Album;
import com.happycoding.music.mapper.AlbumMapper;
import com.happycoding.music.service.AlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专辑信息表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

}
