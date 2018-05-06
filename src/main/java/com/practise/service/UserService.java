package com.practise.service;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practise.dao.LoginTicketDao;
import com.practise.dao.UserDao;
import com.practise.model.LoginTicket;
import com.practise.model.User;
import com.practise.util.DemoUtil;

@Service
public class UserService {
	@Autowired
	UserDao userDao;
	
	@Autowired
	LoginTicketDao loginTicketDao;
	
	/**
	 * 注册
	 * 在Service曾完成逻辑判断等
	 */
	public Map<String, String> register(String username, String password){
		Map<String, String> map = new HashMap<>();
		
		//注册时用户名不能为空
		if(StringUtils.isBlank(username)){
			map.put("msg", "用户名不能为空");
			return map;
		}
		
		//注册时密码不能为空
		if(StringUtils.isBlank(password)){
			map.put("msg", "密码不能为空");
			return map;
		}
		
		//判断用户名是否存在
		User user = userDao.selectByName(username);
		if(user != null){			
			map.put("msg", "用户名已经存在，请更换");
			return map;
		}
		
		user = new User();
		user.setName(username);
		//在设置密码时需要增加salt
		user.setSalt(UUID.randomUUID().toString().substring(0, 6));
		Random random = new Random();
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(500)));
		user.setPassword(DemoUtil.MD5(user.getSalt() + password));
		userDao.addUser(user);
		
		return map;
	}
	
	
	/**
	 * 根据用户id的到用户
	 */
	public User getUser(int id){
		return userDao.selectById(id);
	}
	
	/**
	 * 根据用户名name得到用户
	 * @param name
	 * @return
	 */
	public User selectByName(String name){
		return userDao.selectByName(name);
	}
	
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String, String> login(String username, String password){
		HashMap<String, String> map = new HashMap<String,String>();
		//登录时仍需要判断用户名和密码
		if(StringUtils.isBlank(username)){
			map.put("msg", "用户名不能为空");
			return map;
		}
		if(StringUtils.isBlank(password)){
			map.put("msg", "密码不能为空");
			return map;
		}
		
		User user = userDao.selectByName(username);
		
		if(user == null){
			map.put("msg", "对不起，用户名不存在");
			return map;
		}
		if(!DemoUtil.MD5(user.getSalt() + password).equals(user.getPassword())){
			map.put("msg", "用户名或密码错误");
			return map;
		}
		//如果用户名和密码没有任务错误，即可以正常登录，则在登录时对用户添加ticket,此ticket需要根据userId进行构造
		String ticket = addLoginTicket(user.getId());
		map.put("ticket", ticket);
		return map;
	}
	
	public String addLoginTicket(int userId){
		LoginTicket ticket = new LoginTicket();
		ticket.setUserId(userId);
		Date date = new Date();
		date.setTime(date.getTime() + 3600 * 24 *10);
		ticket.setExpired(date);
		ticket.setStatus(0);
		ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		loginTicketDao.addTicket(ticket);
		return ticket.getTicket();
		
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	public void logout(String ticket){
		loginTicketDao.updateStatus(ticket, 1);
	}
	
	
	
	
	
	
	
	
	
}
