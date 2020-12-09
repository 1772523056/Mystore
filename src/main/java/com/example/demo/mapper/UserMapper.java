package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

//@repository
@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,time_create,time_modify) values(#{name},#{accountId},#{token},#{time_create},#{time_modify})")
//    @Insert("insert into user (name,account_id,token,time_create,time_modify) values (#{name},#{accountId},#{token},#{time_create},#{time_modify}")
    void insert(User user);
}
