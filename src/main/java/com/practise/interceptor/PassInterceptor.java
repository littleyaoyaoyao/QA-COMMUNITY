package com.practise.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.practise.dao.LoginTicketDao;
import com.practise.dao.UserDao;
import com.practise.model.HostHolder;
import com.practise.model.LoginTicket;
import com.practise.model.User;

@Component
public class PassInterceptor implements HandlerInterceptor{

	@Autowired
	LoginTicketDao loginTicketDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	HostHolder hostHold;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		hostHold.clear();
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView modelAndView)
			throws Exception {
		if(modelAndView != null && hostHold.getUser() != null){
			modelAndView.addObject("user",hostHold.getUser());
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj) throws Exception {
		String ticket = null;
		Cookie[] cookies = httpServletRequest.getCookies();
		if(httpServletRequest.getCookies() != null){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("ticket")){
					ticket = cookie.getValue();
					break;
				}
			}
		}
		
		if(ticket != null){
			LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
			if(loginTicket == null || loginTicket.getStatus() != 0 || loginTicket.getExpired().before(new Date())){
				return true;
			}
			
			//当ticket可用时，
			User user = userDao.selectById(loginTicket.getUserId());
			//放到上下文，多线程考虑。因为后面还有很多的链路过来，且都会经过这个拦截器
			hostHold.setUser(user);
		}
		
		return true;
	}

}
