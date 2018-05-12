package com.practise.util;

/**
 * Redis中key的命名
 * @ClassName:
 * @Description:
 * @author Yao
 * 
 * @date 2018年5月5日 下午3:57:54
 */
public class RedisKeyUtil {
	private static String SPLIT = ":";
	private static String LIKE_IT = "LIKE";
	private static String DISLIKE_IT = "DISLIKE";
	private static String EVENTQUEUE = "EVENT_QUEUE";
	
	public static String getLikeKey(int entityType, int entityId){
		//LIKE:1:12  为问题12喜欢
		return LIKE_IT + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}
	
	public static String getDislikeKey(int entityType, int entityId){
		return DISLIKE_IT + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}
	
	public static String getEventQueue(){
		return EVENTQUEUE;
	}
	
}
