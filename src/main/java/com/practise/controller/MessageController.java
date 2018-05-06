package com.practise.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.MessageCodeFormatter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.practise.model.HostHolder;
import com.practise.model.Message;
import com.practise.model.User;
import com.practise.model.ViewObject;
import com.practise.service.MessageService;
import com.practise.service.UserService;
import com.practise.util.DemoUtil;


@Controller
public class MessageController {
	
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	HostHolder hostHolder;
	
	@Autowired
	UserService userService;
	
	/**
	 * 发送消息
	 * @param toName 收件人name
	 * @param content 发送内容
	 * @return JSON，与前端交互
	 */
	@RequestMapping(path={"/msg/addMessage"},method=RequestMethod.POST)
	@ResponseBody
	public String addMessage(@RequestParam("toName") String toName, @RequestParam("content") String content){
		try {
			//登录检查
			if(hostHolder.getUser() == null){
				return DemoUtil.getJSONString(999, "未登录");
			}
			//根据用户名称查询用户
			User user = userService.selectByName(toName);
			if(user == null){
				return DemoUtil.getJSONString(2, "该用户不存在");
			}
			Message msg = new Message();
			msg.setContent(content);
			msg.setCreatedDate(new Date());
			msg.setFromId(hostHolder.getUser().getId());
			msg.setToId(user.getId());
			messageService.addMessage(msg);
			//0表示没问题
			return DemoUtil.getJSONString(0);
			
		} catch (Exception e) {
			logger.error("发送私信失败" + e.getMessage());
			return DemoUtil.getJSONString(2, "发送私信失败");
		}
	}
	
	
	
	/**
	 * 根据conversationId查看信息
	 * @param model
	 * @param conversationId
	 * @return
	 */
	@RequestMapping(path={"/msg/detail"})
	public String coversationDetail(Model model, @RequestParam("conversationId") String conversationId){
		try {
			List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
			List<ViewObject> messages = new ArrayList<>();
			for(Message msg : conversationList){
				ViewObject vo = new ViewObject();
				vo.set("message", msg);
				User user = userService.getUser(msg.getFromId());
				if(user == null) continue;
				vo.set("userId", user.getId());
				vo.set("headUrl", user.getHeadUrl());
				messages.add(vo);
			}
			model.addAttribute("messages", messages);
		} catch (Exception e) {
			logger.error("获取消息失败"+e.getMessage());
		}
		return "letterDetail";
	}
	
	/**
	 * 和当前用户相关的message list, 
	 * @param model
	 * @return
	 */
	@RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<ViewObject>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("unread", messageService.getConvesationUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
                model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }
}
