package com.practise.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.practise.async.EventHandler;
import com.practise.async.EventModel;
import com.practise.async.EventType;
import com.practise.model.Message;
import com.practise.model.User;
import com.practise.service.MessageService;
import com.practise.service.UserService;
import com.practise.util.DemoUtil;

@Component
public class LikeHandler implements EventHandler{

	@Autowired
	UserService userService;
	
	@Autowired
	MessageService messageService;
	
	@Override
	public void doHandle(EventModel eventModel) {
		Message message = new Message();
		//得到发起like事件的用户
		User user = userService.getUser(eventModel.getActorId());
		
		//管理员发送信息
		message.setFromId(DemoUtil.SYSTEM_ID);
		message.setToId(eventModel.getEntityOwnerId());
		message.setCreatedDate(new Date());
		message.setContent("用户" + user.getName() + "赞了你的评论,"
							+ "http://localhost:8080/question/" +
							eventModel.getExt("questionId"));
		
		messageService.addMessage(message);

	}
	
	/**
	 * 添加Handler处理的event类型
	 */
	@Override
	public List<EventType> getSupportEventType() {
		return Arrays.asList(EventType.LIKE);
	}

}
