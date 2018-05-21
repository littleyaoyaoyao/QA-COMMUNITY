package com.practise.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.practise.util.JedisAdapter;
import com.practise.util.RedisKeyUtil;

/**
 *将EventType分发到不同的Handler 
 * @ClassName:
 * @Description:
 * @author Yao
 * 
 * @date 2018年5月13日 下午2:23:51
 */
@Service 
public class EventConsumer implements InitializingBean, ApplicationContextAware{
	private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
	private Map<EventType, List<EventHandler>>  config = new HashMap<EventType, List<EventHandler>>();
	private ApplicationContext applicationContext;
	
	@Autowired
	JedisAdapter jedisAdapter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//找到event的实现handler
		Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
		if(beans != null){
			for(Map.Entry<String, EventHandler> entry : beans.entrySet()){
				//得到某个handler支持的eventtypes 
				List<EventType> eventTypes = entry.getValue().getSupportEventType();
			
				 for(EventType eventType : eventTypes){
					 if(!config.containsKey(eventType)){
						 config.put(eventType, new ArrayList<EventHandler>());
					 }
					 //将handler添加进去list
					 config.get(eventType).add(entry.getValue());
				 }
			}
		}
		
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while(true){
					String key = RedisKeyUtil.getEventQueue();
					//从阻塞队列中得到所有事件
					//返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值;因此需要进行一个判断
					List<String> events = jedisAdapter.brpop(0, key);
					for(String event : events){
						if(event.equals(key)){
							continue;
						}
						
						EventModel eventModel = JSON.parseObject(event, EventModel.class);
						//之前config已经完成了EventType和List<EventHandler>的绑定
						if(!config.containsKey(eventModel.getType())){
							logger.error("该事件不能被识别，请确认");
							continue;
						}
						
						for(EventHandler handler : config.get(eventModel.getType())){
							handler.doHandle(eventModel);
						}
					}
					
				}
			}
		});
		thread.start();
		
		
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	
}
