package com.practise.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.practise.model.User;

@Mapper
public interface UserDao {
	String TABLE_NAME = "user";
	String INSERT_FILEDS = "name, password, salt, head_url";
	String SELECT_FIELDS = "id," + INSERT_FILEDS;
	
	@Insert({"Insert into ",TABLE_NAME," (",INSERT_FILEDS,") values (#{name}, #{password}, #{salt}, #{headUrl})"})
	int addUser(User user);
	
	@Update({"update ",TABLE_NAME," set password=#{password} where id=${id}"})
	void updatePassWord(User user);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
	User selectById(int id);
	 
	@Delete({"delete from ",TABLE_NAME," where id=#{id}"})
	void deleteById(int id);
	
	//根据用户名选择
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where name=#{name}"})
	User selectByName(String name);
}
