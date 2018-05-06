package com.practise.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.practise.model.Question;

@Mapper
public interface QuestionDao {
	String TABLE_NAME = "question";
	String INSERT_FILEDS = "title, content, user_id, created_date, comment_count";
	String SELECT_FIELDS = "id," + INSERT_FILEDS;
	
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FILEDS,") values(#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
	int addQuestion(Question question);
	
	List<Question> selectLatestQuestions(@Param("userId") int userId, 
										 @Param("offset") int offset,
										 @Param("limit") int limit);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
	Question getQuestionById(int id);
	
	@Update({"update ",TABLE_NAME," set comment_count=#{commentCount} where id=#{id}"})
	int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);
}
