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
	private static String FOLLOWER_IT = "FOLLOWER";
	private static String IT_FOLLOWEE = "FOLLOWEE";
	
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
	
	/**
	 * 不是局限于人的关注者，也可以拓展到问题的关注者，回答的关注者
	 * @return
	 */
	public static String getFollowerKey(int entityType, int entityId){
		return FOLLOWER_IT + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}
	
	/**
	 * 不限于关注人，也可以关注（收藏）问题等
	 * @param userId 关注动作的发起人
	 * @param entityType 关注的类型，如人、问题、评论等
	 * @return
	 */
	public static String getFolloweeKey(int userId, int entityType){
		return IT_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
	}
	
	
	
	
	
}
