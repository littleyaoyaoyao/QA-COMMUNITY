package com.practise.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.practise.model.Comment;
import com.practise.model.EntityType;
import com.practise.model.HostHolder;
import com.practise.model.Question;
import com.practise.model.ViewObject;
import com.practise.service.CommentService;
import com.practise.service.QuestionService;
import com.practise.service.UserService;
import com.practise.util.DemoUtil;



@Controller
public class QuestionController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	HostHolder hostHold;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CommentService commentService;
	
	//查看问题详细内容
	//关于问题的评论也在这部分中实现
	@RequestMapping(path={"/question/{qid}"},method={RequestMethod.GET})
	public String questionDetails(Model model, @PathVariable("qid") int qid){
		Question question = questionService.getQuestionById(qid);
		model.addAttribute("question",question);
		
		//获得问题的评论列表
		List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
		List<ViewObject> vos = new ArrayList<>();
		for(Comment comment : commentList){
			ViewObject vo = new ViewObject();
			vo.set("comment", comment);
			vo.set("user", userService.getUser(comment.getUserId()));
			vos.add(vo);
		}
		model.addAttribute("comments",vos);
		return "detail";
	}

	
	//在主页查看问题的添加概况
	//js在popupAdd
	@RequestMapping(path={"/question/add"},method={RequestMethod.POST})
	@ResponseBody
	public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
		try {
			Question question = new Question();
			question.setContent(content);
			question.setTitle(title);
			question.setCreatedDate(new Date());
			if(hostHold.getUser() == null){
				return DemoUtil.getJSONString(999);
			}else{
				question.setUserId(hostHold.getUser().getId());
			}
			if(questionService.addQuestion(question) > 0){
				return DemoUtil.getJSONString(0);
			}
		} catch (Exception e) {
			logger.error("增加问题失败",e.getMessage());
		}
		return DemoUtil.getJSONString(1, "增加问题失败");
	}
}
