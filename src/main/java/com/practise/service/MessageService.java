package com.practise.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practise.dao.MessageDao;
import com.practise.model.Message;

@Service
public class MessageService {
	@Autowired
	MessageDao messageDao;
	
	
	public int addMessage(Message message){
		return messageDao.addMeaasge(message);
	}
	
	public List<Message> getConversationDetail(String conversationId, int offset, int limit){
		return messageDao.getConversationDetail(conversationId, offset, limit);
	}
	
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDao.getConversationList(userId, offset, limit);
    }

    /**
     * 描述的是当前用户为收件人的未读信息
     * @param userId to_id,即收件人的id
     * @param conversationId 
     * @return
     */
    public int getConvesationUnreadCount(int userId, String conversationId) {
        return messageDao.getConversationUnreadCount(userId, conversationId);
    }
}
