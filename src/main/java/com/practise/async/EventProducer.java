package com.practise.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.practise.util.JedisAdapter;
import com.practise.util.RedisKeyUtil;

@Service
public class EventProducer {
	
	@Autowired
	JedisAdapter jedisAdapter;
	
	public boolean fireEvent(EventModel eventModel){
		try {
			String key = RedisKeyUtil.getEventQueue();
			String json = JSONObject.toJSONString(eventModel);
			jedisAdapter.lpush(key, json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
