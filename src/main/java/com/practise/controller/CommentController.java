package com.practise.controller;

import java.util.Date;

import javax.swing.text.html.parser.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.practise.model.Comment;
import com.practise.model.EntityType;
import com.practise.model.HostHolder;
import com.practise.service.CommentService;
import com.practise.service.QuestionService;
import com.practise.service.UserService;

@Controller
public class CommentController {

	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	HostHolder hostHolder;
	
	@Autowired
	UserService userService;
	
	@Autowired
	QuestionService questionService;
	
	@RequestMapping(path={"/addComment"},method={RequestMethod.POST})
	public String addComment(@RequestParam("questionId") int questionId, @RequestParam("content") String content){
		
		try {
		//过滤html内容
		content = HtmlUtils.htmlEscape(content);
		
		//set
		Comment comment = new Comment();
		if(hostHolder.getUser() != null){
			comment.setUserId(hostHolder.getUser().getId());
		}else{
			return "redirect:/reglogin";
		}
		comment.setContent(content);
		comment.setCreatedDate(new Date());
		comment.setEntityId(questionId);
		//表示对问题进行评论
		comment.setEntityType(EntityType.ENTITY_QUESTION);
		comment.setStatus(0);
		commentService.addComment(comment);
		
		//得到问题中的评论总数
		int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
		questionService.updateCommentCount(comment.getEntityId(), count);
		
		} catch (Exception e) {
			logger.error("增加评论失败"+e.getMessage());
		}
		return "redirect:/question/" + String.valueOf(questionId);
	}
	
}
