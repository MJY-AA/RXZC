package com.xsj.app.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @PackageName:com.rxzc.rxzcapp.jedis
 * @Description:
 * @author:Xsj
 * @date 2020/4/20 0020 17:43
 */

@Component
public class RedisUtil {

    @Autowired
	private JedisPool jedisPool;

	/**
	 * 存储字符串键值对
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) throws Exception {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.set(key, value);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			jedis.close();
		}
	}

	/**
	 * 得到对应键的字符串值
	 * @param key
	 * @return
	 */
	public String get(String key) throws Exception {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.get(key);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			jedis.close();
		}
	}

	/**
	 * 删除字符串键值对
	 * @param key
	 * @return
	 * @author hw
	 * @date 2018年12月14日
	 */
	public Long del(String key) throws Exception {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.del(key);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			jedis.close();
		}
	}

	/**
	 * 存储对象
	 * @param key
	 * @param value
	 * @return
	 * @author hw
	 * @date 2018年12月14日
	 */
	public String setObject(String key, Object value) throws Exception {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.set(key.getBytes(), ObjectUtil.serialize(value));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			jedis.close();
		}
	}

	/**
	 * 得到对应键的对象
	 * @param key
	 * @return
	 * @author hw
	 * @date 2018年12月14日
	 */
	public Object getObject(String key) throws Exception {
		Jedis jedis = jedisPool.getResource();
		try {
			byte[] byteArr =  jedis.get(key.getBytes());
			return ObjectUtil.unSerialize(byteArr);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			jedis.close();
		}
	}
}

