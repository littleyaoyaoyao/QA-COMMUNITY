package com.practise.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.ObjectView;

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

import com.practise.async.EventModel;
import com.practise.async.EventProducer;
import com.practise.async.EventType;
import com.practise.model.EntityType;
import com.practise.model.HostHolder;
import com.practise.model.Question;
import com.practise.model.User;
import com.practise.model.ViewObject;
import com.practise.service.CommentService;
import com.practise.service.FollowService;
import com.practise.service.QuestionService;
import com.practise.service.UserService;
import com.practise.util.DemoUtil;

@Controller
public class FollowController {

	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	FollowService followService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	HostHolder hostHolder;
	
	@Autowired
	EventProducer eventProducer;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	CommentService CommentService;
	
	@RequestMapping(path={"/followUser"}, method={RequestMethod.POST})
	@ResponseBody
	public String followerUser(@RequestParam("userId") int userId){
		if(hostHolder.getUser() == null){
			return DemoUtil.getJSONString(999);
		}
		boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
								.setActorId(hostHolder.getUser().getId())
								.setEntityId(userId)
								.setEntityOwnerId(userId)
								.setEntityType(EntityType.ENTITY_USER));
		return DemoUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
	}
	
	
	@RequestMapping(path={"/unfollowUser"}, method={RequestMethod.POST})
	@ResponseBody
	public String unfollowerUser(@RequestParam("userId") int userId){
		if(hostHolder.getUser() == null){
			return DemoUtil.getJSONString(999);
		}
		boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
				.setActorId(hostHolder.getUser().getId())
				.setEntityId(userId)
				.setEntityOwnerId(userId)
				.setEntityType(EntityType.ENTITY_USER));
		//数量的作用是用于JS显示数量，即前端的需求，因此后端需要返回给前端这么一个信息
		return DemoUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
	}
	
	//关注问题
	/**
	 * 关注问题，在关注问题的时候需要关联到用户的信息，包括用户的头像、姓名、id等信息
	 * @param questionId
	 * @return
	 */
	@RequestMapping(path={"/followQuestion"}, method={RequestMethod.POST})
	@ResponseBody
	public String followerQuestion(@RequestParam("questionId") int questionId){
		if(hostHolder.getUser() == null){
			return DemoUtil.getJSONString(999);
		}
		
		Question question = questionService.getQuestionById(questionId);
		if(question == null){
			return DemoUtil.getJSONString(1,"问题不存现在");
		}
		
		boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
								.setActorId(hostHolder.getUser().getId())
								.setEntityId(questionId)
								.setEntityOwnerId(question.getUserId())
								.setEntityType(EntityType.ENTITY_QUESTION));
		
		Map<String,Object> info = new HashMap<>();
		info.put("headUrl", hostHolder.getUser().getHeadUrl());
		info.put("name", hostHolder.getUser().getName());
		info.put("id", hostHolder.getUser().getId());
		info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
		return DemoUtil.getJSONString(ret ? 0 : 1, info);
	}
	
	/**
	 * //取消关注问题
	 * @param questionId
	 * @return
	 */
	@RequestMapping(path={"/unfollowQuestion"}, method={RequestMethod.POST})
	@ResponseBody
	public String unfollowerQuestion(@RequestParam("questionId") int questionId){
		if(hostHolder.getUser() == null){
			return DemoUtil.getJSONString(999);
		}
		
		Question question = questionService.getQuestionById(questionId);
		if(question == null){
			return DemoUtil.getJSONString(1,"问题不存现在");
		}
		
		boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
								.setActorId(hostHolder.getUser().getId())
								.setEntityId(questionId)
								.setEntityOwnerId(question.getUserId())
								.setEntityType(EntityType.ENTITY_QUESTION));
		
		Map<String,Object> info = new HashMap<>();
		info.put("headUrl", hostHolder.getUser().getHeadUrl());
		info.put("name", hostHolder.getUser().getName());
		info.put("id", hostHolder.getUser().getId());
		info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
		return DemoUtil.getJSONString(ret ? 0 : 1, info);
	}
	
	/**
	 * 得到某个用户的粉丝列表
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
		List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
		if(hostHolder.getUser() != null){
			model.addAttribute("followers",getUserInfo(hostHolder.getUser().getId(), followerIds));
		}else{
			model.addAttribute("followers", getUserInfo(2, followerIds));
		}
		model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
	}
	
	
	/**
	 * 得到某个用户的关注列表
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
		List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 10);
		if(hostHolder.getUser() != null){
			model.addAttribute("followees",getUserInfo(hostHolder.getUser().getId(), followeeIds));
		}else{
			model.addAttribute("followees", getUserInfo(2, followeeIds));
		}
		model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
		return "followees";
	}
	
	
	
	/**
	 *粉丝列表和关注列表中，每个实体都有个人信息，评论数量，关注数量，粉丝数量 
	 * @param localUserId
	 * @param userIds
	 * @return
	 */
	public List<ViewObject> getUserInfo(int localUserId, List<Integer> userIds){
		List<ViewObject> userInfo = new ArrayList<>();
		for(Integer uid : userIds){
			User user = userService.getUser(uid);
			if(user == null) continue;
			ViewObject vo = new ViewObject();
			vo.set("user", user);
			vo.set("commentCount", CommentService.getUserCommentCount(uid));
			vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
			vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
			//判断是否为“已关注”
			if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
			userInfo.add(vo);
		}
		return userInfo;
	}
	
	
}
