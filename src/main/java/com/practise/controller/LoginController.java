package com.practise.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.practise.dao.UserDao;
import com.practise.service.UserService;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	UserService userService;
	
	/**
	 * 注册请求
	 * @param model
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(path={"/reg"}, method={RequestMethod.POST})
	public String reg(Model model, 
						@RequestParam("username") String username, 
						@RequestParam("password") String password,
						@RequestParam("callback") String callback,
						@RequestParam("rememberme") boolean rememberme,
						HttpServletResponse response){
		try {
			Map<String, String> map = userService.register(username, password);
			if(map.containsKey("ticket")){
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				//加上这一句，可以使cookie被同一应用服务器访问，否则只能被当前应用访问；path属性将指定该cookie的页面路径，path字段为可以访问此cookie的页面路径
				cookie.setPath("/");
				if(rememberme){
					cookie.setMaxAge(5*24*3600);
				}
				response.addCookie(cookie);
				if(StringUtils.isNotBlank(callback)){
					return "redirect:" + callback;
				}
				return "redirect:/";
			}else{
				model.addAttribute("msg",map.get("msg"));
				return "login";				
			}
		} catch (Exception e) {
			logger.error("REGISTER ERROR" + e.getMessage());
			return "login";
		}	
	}

	
	/**
	 * 注册和登陆的页面显示
	 * @param model
	 * @return
	 */
	@RequestMapping(path={"/reglogin"}, method={RequestMethod.GET})
	public String regLoginPage(Model model,@RequestParam(value="callback",required=false) String callback){
		model.addAttribute("callback",callback);
		return "login";
	}
	
	
	//---------------------------------------------------------------------------------------
	
	/**
	 * 登录请求
	 * @param model
	 * @param username    用户名
	 * @param password	      密码
	 * @param rememberme  登录时选择的登录状态
	 * @param response
	 * @return
	 */
	@RequestMapping(path={"/login"}, method={RequestMethod.POST})
	public String login(Model model, 
						@RequestParam("username") String username, 
						@RequestParam("password") String password,
						@RequestParam("callback") String callback,
						@RequestParam(value = "rememberme", defaultValue="false") boolean rememberme,
						HttpServletResponse response){
		try {
			Map<String, String> map = userService.login(username, password);
			if(map.containsKey("ticket")){
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				//加上这一句，可以使cookie被同一应用服务器访问，否则只能被当前应用访问；path属性将指定该cookie的页面路径，path字段为可以访问此cookie的页面路径
				cookie.setPath("/");
				if(rememberme){
					cookie.setMaxAge(5*24*3600);
				}
				response.addCookie(cookie);
				if(StringUtils.isNotBlank(callback)){
					return "redirect:" + callback;
				}
				return "redirect:/";
			}else{
				model.addAttribute("msg",map.get("msg"));
				return "login";
			}

		} catch (Exception e) {
			logger.error("登陆异常" + e.getMessage());
			return "login";
		}
		
	}
	
	//-------------------------------------------------------------------------
	/**
	 * 用户登出
	 * @param ticket
	 * @return
	 */
	@RequestMapping(path={"/logout"},method={RequestMethod.GET, RequestMethod.POST})
	public String logout(@CookieValue("ticket") String ticket){
		userService.logout(ticket);
		return "redirect:/";
	}
	
	//-------------------------------------------------------------------------------
	
	
	
	
}
