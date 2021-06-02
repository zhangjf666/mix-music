package com.happycoding.music.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happycoding.music.entity.User;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zjf
 * @since 2020-06-11
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    @Select("select * from sys_user u where u.username = #{username}")
    @Results({
            @Result(property = "id",column = "id",id = true),
            @Result(property = "deptId",column = "dept_id"),
            @Result(property = "roles", javaType = Set.class,column = "id",many = @Many(select = "com.zjf.assistant.system.mapper.RoleMapper.getUserRoles"))
    })
    User findByUsername(String username);
}
