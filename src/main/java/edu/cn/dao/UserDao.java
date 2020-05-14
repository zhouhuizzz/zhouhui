package edu.cn.dao;

import edu.cn.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from user where id =#{id}")
    public User getByID(@Param("id") int id);

}
