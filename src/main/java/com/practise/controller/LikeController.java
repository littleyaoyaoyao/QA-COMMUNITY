package com.practise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.practise.async.EventModel;
import com.practise.async.EventProducer;
import com.practise.async.EventType;
import com.practise.async.handler.LikeHandler;
import com.practise.model.Comment;
import com.practise.model.EntityType;
import com.practise.model.HostHolder;
import com.practise.service.CommentService;
import com.practise.service.LikeService;
import com.practise.util.DemoUtil;

@Controller
public class LikeController {
	
	@Autowired
	HostHolder hostHolder;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	LikeService likeService;
	
	@Autowired
	EventProducer eventProducer;
	
	
	@RequestMapping(path={"/like"},method=RequestMethod.POST)
	@ResponseBody
	public String like(@RequestParam("commentId") int commentId){
		if(hostHolder.getUser() == null){
			return DemoUtil.getJSONString(999);
		}
		
		//当用户进行点赞后，需要得到用户堆对什么entityType（comment or others）进行了点赞
		//此处对comment进行了like操作
		/**
		 * 	private EventType type;
			private int actorId; //事件出发者id
			private int entityType;
			private int entityId;
			private int entityOwnerId; //该实体拥有者，比如actorid点赞，给entityOwnerId发站内信。
		 */
		Comment comment = commentService.getCommentById(commentId);
		
		eventProducer.fireEvent(new EventModel(EventType.LIKE).
								setActorId(hostHolder.getUser().getId()).
								setEntityType(EntityType.ENTITY_COMMENT).
								setEntityId(commentId).
								setEntityOwnerId(comment.getUserId()).
								setExt("questionId", String.valueOf(comment.getEntityId())));
		
		//Comment comment = commentService.getCommentById(commentId);
		long likeCount = likeService.like(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT, commentId);
		
		return DemoUtil.getJSONString(0, String.valueOf(likeCount));
	}
	
	@RequestMapping(path={"/dislike"},method=RequestMethod.POST)
	@ResponseBody
	public String dislike(@RequestParam("commentId") int commentId){
		if(hostHolder.getUser() == null){
			return DemoUtil.getJSONString(999);
		}
		//Comment comment = commentService.getCommentById(commentId);
		long likeCount = likeService.dislike(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT, commentId);
		
		return DemoUtil.getJSONString(0, String.valueOf(likeCount));
	}
	
	
	
	
	
	
	
	
	
	
}
