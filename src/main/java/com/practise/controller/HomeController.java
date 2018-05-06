package com.practise.controller;

import java.util.ArrayList;
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

import com.practise.model.Question;
import com.practise.model.ViewObject;
import com.practise.service.QuestionService;
import com.practise.service.UserService;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	QuestionService questionService;
	
	private List<ViewObject> getQuestion(int userId, int offset, int limit){
		List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
		List<ViewObject> vos = new ArrayList<>();
		for(Question question : questionList){
			ViewObject vo = new ViewObject();
			vo.set("user", userService.getUser(question.getUserId()));
			vo.set("question", question);
			vos.add(vo);
		}
		return vos;
	}
	
	@RequestMapping(path={"/index","/"}, method={RequestMethod.GET, RequestMethod.POST})
	public String some(Model model){
		model.addAttribute("vos",getQuestion(0, 0, 10));
		//model.addAttribute("78","43");
		return "index";
	}
	
	//-----------------------------------------------------------
	//test用例
	@RequestMapping(path={"/test"})
	@ResponseBody
	public String show(Model model){
		model.addAttribute("value","211");
		return "test1";
	}
	
	
	//--------------------------------------------------------------
	@RequestMapping(path={"/user/{userId}"},method={RequestMethod.GET, RequestMethod.POST})
		public String userIndex(@PathVariable("userId") int userId,Model model){
			model.addAttribute("vos",getQuestion(userId, 0, 10));
			return "index";
			
		}
}
