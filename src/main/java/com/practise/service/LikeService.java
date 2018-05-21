package com.practise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practise.util.DemoUtil;
import com.practise.util.JedisAdapter;
import com.practise.util.RedisKeyUtil;

@Service
public class LikeService {
	
	@Autowired
	JedisAdapter jedisAdapter;
	
	public long like(int userId, int entityType, int entityId){
		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		jedisAdapter.sadd(likeKey, String.valueOf(userId));
		
		String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
		jedisAdapter.srem(dislikeKey, String.valueOf(userId));
		
		return jedisAdapter.scard(likeKey);
	}
	
	public long dislike(int userId, int entityType, int entityId){
		String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
		jedisAdapter.sadd(dislikeKey, String.valueOf(userId));
		
		String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
		jedisAdapter.srem(likeKey, String.valueOf(userId));
		
		return jedisAdapter.scard(dislikeKey);
	}
	
	/**
	 * 统计赞的总数
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public long getLikeCount(int entityType, int entityId) {
	    String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
	    return jedisAdapter.scard(likeKey);
	}
	
	/**
	 * 判断当前用户是否已经like or dislike
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }
	
}
