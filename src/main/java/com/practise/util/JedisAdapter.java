package com.practise.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JedisAdapter implements InitializingBean{
	private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
	
	private JedisPool pool;

	@Override
	public void afterPropertiesSet() throws Exception {
		pool = new JedisPool("redis://127.0.0.1:6379/8");
	}
	
	//set
	public long sadd(String key, String val){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sadd(key, val);
		} catch (Exception e) {
			logger.error("set添加发生异常"+e.getMessage());
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return 0;
	}
	
	/**
	 * set中元素移除
	 * @param key
	 * @param val
	 * @return
	 */
	public long srem(String key, String val){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.srem(key, val);
		} catch (Exception e) {
			logger.error("set移除发生异常"+e.getMessage());
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return 0;
	}
	
	/**
	 * 获取set中元素的个数
	 * @param key
	 * @return 
	 */
	public long scard(String key){
		  Jedis jedis = null;
	        try {
	            jedis = pool.getResource();
	            return jedis.scard(key);
	        } catch (Exception e) {
	            logger.error("set获取发生异常" + e.getMessage());
	        } finally {
	            if (jedis != null) {
	                jedis.close();
	            }
	        }
	        return 0;
	}
	
	/**
	 * 判断元素是否在set中
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("set判断发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }
	
	/**
	 * push event into the "queue"
	 * @param key
	 * @param value
	 * @return
	 */
	public long lpush(String key, String value){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error("list push发生异常" + e.getMessage());
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return 0;
	}
	
	
	//它是 RPOP 命令的阻塞版本，当给定列表内没有任何元素可供弹出的时候，连接将被 BRPOP 命令阻塞，直到等待超时或发现可弹出元素为止。
	public List<String> brpop(int timeout, String key){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.brpop(timeout, key);
		} catch (Exception e) {
			logger.error("list brpop发生异常" + e.getMessage());
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return null;
	}
	
	
	
	public static void main(String[] args){
		Jedis jedis = new Jedis("redis://127.0.0.1:6379/8");
		System.out.println(jedis.scard("LIKE:2:3"));
		System.out.println(jedis.sismember("LIKE:2:3", "22"));
	}
	
}
