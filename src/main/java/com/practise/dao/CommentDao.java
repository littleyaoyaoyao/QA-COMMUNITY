package com.practise.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.practise.model.Comment;

@Mapper
public interface CommentDao {
	String TABLE_NAME = "comment";
	String INSERT_FILEDS = "user_id, content, created_date, entity_id, entity_type, status";
	String SELECT_FIELDS = "id," + INSERT_FILEDS;
	
	/**
	 * 添加问题
	 * @param comment
	 * @return
	 */
	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FILEDS,") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
	int addComment(Comment comment);
	
	
	/**
	 * 根据entityId和entityType得到评论集合
	 * @param entityId
	 * @param entityType
	 * @return 某一实体的所有评论
	 */
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME, 
			 "where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
	List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);
	
	
	/**
	 * 统计某一实体的评论总数
	 * @param entityId
	 * @param entityType
	 * @return
	 */
	@Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

	@Select({"select ",SELECT_FIELDS, "from " ,TABLE_NAME, "where id=#{id}"})
	Comment getCommentById(int id);
	
	@Select({"select count(user_id) from ",TABLE_NAME, " where user_id=#{userId}"})
	int getUserCommentCount(int userId);
}
