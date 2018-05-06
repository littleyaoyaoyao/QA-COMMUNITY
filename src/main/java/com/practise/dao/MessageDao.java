package com.practise.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.practise.model.Message;

@Mapper
public interface MessageDao {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    
    /**
     * 添加消息,此处调用getter
     * @param message
     * @return
     */
    @Insert({"insert into ",TABLE_NAME, "(",INSERT_FIELDS,") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMeaasge(Message message);
    
    /**
     * 得到与某一用户的对话列表与内容,对应message和letterDetail,根据conversation_id得到所有的messgae
     * @param conversationId
     * @param offset 偏移量
     * @param limit 每次展示的条目数
     * @return
     */
    @Select({"select ",SELECT_FIELDS , " from ",TABLE_NAME, " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
										@Param("offset") int offset,
										@Param("limit") int limit);
    
    
    /**
     * 统计得到有关于该用户的信息（包括发信息和收信息）
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);
    
    
    /**
     * 未读信息数量统计
     * @param userId
     * @param conversationId
     * @return
     */
    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);
    
}
