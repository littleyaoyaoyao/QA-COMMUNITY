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
import com.practise.service.LikeService;
import com.practise.service.QuestionService;
import com.practise.service.UserService;
import com.practise.util.DemoUtil;
import com.practise.util.JedisAdapter;
import com.practise.util.RedisKeyUtil;



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
	
	@Autowired
	LikeService likeService;
	
	@Autowired
	JedisAdapter jedisAdapter;
	
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
			
			//若用户没有登录，like置为0，前端处理like=0时的按键样式
			if(hostHold.getUser() == null){
				vo.set("liked", 0);
			}else{
				vo.set("liked", likeService.getLikeStatus(hostHold.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
			}
			
			vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
			/*
			//这部分的作用是为了调试like功能
			System.out.println(RedisKeyUtil.getLikeKey(EntityType.ENTITY_COMMENT,comment.getId()));
			System.out.println("当前user id: " + hostHold.getUser().getId());
			//对应getlikestatus
			System.out.println("是否在结合中："+jedisAdapter.sismember(RedisKeyUtil.getLikeKey(EntityType.ENTITY_COMMENT,comment.getId()), String.valueOf(hostHold.getUser().getId())));
			//个数对应getlikecount
			System.out.println("个数："+jedisAdapter.scard(RedisKeyUtil.getLikeKey(EntityType.ENTITY_COMMENT,comment.getId())));
			System.out.println("comment id: "+comment.getId());
			
			//这部分有
			System.out.println(likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
			System.out.println(likeService.getLikeStatus(hostHold.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
			*/
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
