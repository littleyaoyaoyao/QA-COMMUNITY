package com.practise.async;

import java.util.List;

/**
 * EventHandler接口，用于处理处理到达的Event，并分发给相应的Handler
 * @ClassName:
 * @Description:
 * @author Yao
 * 
 * @date 2018年5月12日 下午10:04:46
 */
public interface EventHandler {
	/**
	 * 处理EventModel
	 * @param eventModel
	 */
	void doHandle(EventModel eventModel);
	
	/**
	 * 定义某个Handler能够处理哪些EventType的事件
	 * @return
	 */
	List<EventType> getSupportEventType();
}
