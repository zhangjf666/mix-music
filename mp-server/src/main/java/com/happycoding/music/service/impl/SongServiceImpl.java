package com.happycoding.music.service.impl;

import com.happycoding.music.entity.Song;
import com.happycoding.music.mapper.SongMapper;
import com.happycoding.music.service.SongService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌曲信息表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {

}
