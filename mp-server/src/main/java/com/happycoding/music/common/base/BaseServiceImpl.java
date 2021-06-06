package com.happycoding.music.common.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/8/17 14:49
 */
public class BaseServiceImpl<S extends BaseMapstruct<D,E>,D,M extends BaseMapper<E>,E> extends ServiceImpl<M,E>
        implements BaseService<D,E> {

    @Autowired
    protected S baseMapstruct;

    @Override
    public BaseMapstruct<D, E> getBaseMapstruct() {
        return this.baseMapstruct;
    }

    @Override
    public List<D> queryList(Wrapper<E> wrapper) {
        List<E> list = list(wrapper);
        return baseMapstruct.toDto(list);
    }

    @Override
    public D queryOne(Wrapper<E> wrapper) {
        E entity = getOne(wrapper);
        return baseMapstruct.toDto(entity);
    }

    @Override
    public D queryById(Serializable id) {
        return baseMapstruct.toDto(getById(id));
    }

    @Override
    public D create(D dto) {
        E e = baseMapstruct.toEntity(dto);
        baseMapper.insert(e);
        return baseMapstruct.toDto(e);
    }

    @Override
    public boolean update(D dto) {
        return baseMapper.updateById(baseMapstruct.toEntity(dto)) >= 1;
    }
}
