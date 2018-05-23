package com.practise.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practise.util.JedisAdapter;
import com.practise.util.RedisKeyUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;


@Service
public class FollowService {

	@Autowired
	JedisAdapter jedisAdapter;
	
	//用户关注某个实体，可关注人、问题、评论等
	/**
	 * 用户关注某个实体，可关注人、问题、评论等
	 * @param userId
	 * @param entityType 实体类型
	 * @param entityId 该类型的id
	 * @return
	 */
	public boolean follow(int userId, int entityType, int entityId){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		Date date = new Date();
		Jedis jedis = jedisAdapter.getJedis();
		Transaction tx = jedisAdapter.multi(jedis);
		tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
		tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
		List<Object> ret = jedisAdapter.exec(tx, jedis);
		return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(1) > 0;
	}
	
	//用户取消关注
	/**
	 * 取消关注
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public boolean unfollow(int userId, int entityType, int entityId){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		Date date = new Date();
		Jedis jedis = jedisAdapter.getJedis();
		Transaction tx = jedisAdapter.multi(jedis);
		tx.zrem(followerKey, String.valueOf(userId));
		tx.zrem(followeeKey,String.valueOf(entityId));
		List<Object> ret = jedisAdapter.exec(tx, jedis);
		return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(1) > 0;
	}
	
	//将获取的set集合转化为List集合
	public List<Integer> getListFromSet(Set<String> set){
		List<Integer> list = new ArrayList<Integer>();
		for(String string : set){
			list.add(Integer.parseInt(string));
		}
		return list;
	}
	
	
	//获得用户的被关注列表（简单理解可以为粉丝列表），最好完成分页
	/**
	 * 获得用户的被关注列表（简单理解可以为粉丝列表）
	 * @param entityType
	 * @param entityId
	 * @param count
	 * @return
	 */
	public List<Integer> getFollowers(int entityType, int entityId, int count){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return getListFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
	}
	
	/**
	 * 获得用户的被关注列表（简单理解可以为粉丝列表），带分页功能
	 * @param entityType
	 * @param entityId
	 * @param offset
	 * @param count
	 * @return
	 */
	public List<Integer> getFollowers(int entityType, int entityId, int offset, int count){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return getListFromSet(jedisAdapter.zrevrange(followerKey, offset, offset + count));
	}
	
	
	//获取用户的关注列表
	public List<Integer> getFollowees(int userId, int entityType, int count){
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return getListFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
	}
	
	public List<Integer> getFollowees(int userId, int entityType, int offset, int count){
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return getListFromSet(jedisAdapter.zrevrange(followeeKey, offset, offset + count));
	}
	
	//获取用户的粉丝数量
	public long getFollowerCount(int entityType, int entityId){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return jedisAdapter.scard(followerKey);
	}
	
	//获取用户的关注数量
	public long getFolloweeCount(int userId, int entityType){
		String followeeKey = RedisKeyUtil.getFollowerKey(userId, entityType);
		return jedisAdapter.scard(followeeKey);
	}
	
	//判断用户是否关注了某个实体
	public boolean isFollower(int userId, int entityType, int entityId){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return jedisAdapter.zsocre(followerKey, String.valueOf(userId)) != 0;
	}
	
}
