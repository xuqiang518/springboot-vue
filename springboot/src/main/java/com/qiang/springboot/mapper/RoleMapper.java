package com.qiang.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiang.springboot.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author XuQiang
 * @creat 2022-06-30 21:58
 */
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select id from sys_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);
}
