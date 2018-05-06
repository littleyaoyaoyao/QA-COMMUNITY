package com.practise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class indexController {
	@RequestMapping(path = {"/page/{userId}"})
	@ResponseBody
	public String test(@PathVariable("userId") int userId,
						@RequestParam(value = "type", defaultValue = "7") int type){
		return "what are you doing:" + userId + "type:" + type;
	}
	


}
