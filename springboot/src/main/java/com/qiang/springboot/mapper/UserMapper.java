package com.qiang.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiang.springboot.controller.dto.UserPasswordDTO;
import com.qiang.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author XuQiang
 * @creat 2022-06-26 8:51
 */
//@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);

//    @Select("select * from sys_user")
//    List<User> findAll();
//    @Insert("INSERT INTO sys_user(username, `password`,nickname,email,phone, address) VALUES(#{username}, #{password}, " +
//            "#{nickname}, #{email}, #{phone}, #{address})")
//    int insert(User user);
//
//    int update(User user);
//
//    @Delete("delete from sys_user where id = #{id}")
//    int deleteById(@Param("id") Integer id);  //@Param("id") 参数名和 #{id} 对应
//
//    @Select("select * from sys_user limit #{pageNum}, #{pageSize}")
//    List<User> selectPage(Integer pageNum, Integer pageSize);
//
//    @Select("select count(*) from sys_user")
//    int selectTotal();
}
