package com.xsj.app.util.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @PackageName:com.rxzc.rxzcapp.jedis
 * @Description:
 * @author:Xsj
 * @date 2020/4/20 0020 17:38
 */

@Configuration
public class JedisPoolFactory {
     private static JedisPool jedisPool = null;
        /* @Value("${spring.redis.host:127.0.0.1}")*/
        @Value("${spring.redis.host}")
        private String host;
        @Value("${spring.redis.port:6379}")
        private Integer port;
        @Value("${spring.redis.password:}")
        private String password;
        @Value("${spring.redis.database:0}")
        private Integer database;
        @Value("${spring.redis.jedis.pool.max-active}")
        private Integer maxActive;
        @Value("${spring.redis.jedis.pool.max-idle:}")
        private Integer maxIdle;
        @Value("${spring.redis.jedis.pool.max-wait:5}")
        private Integer maxWait;
        @Value("${spring.redis.jedis.pool.min-idle:}")
        private Integer minIdle;
        @Value("${spring.redis.jedis.pool.timeout:7200}")
        private Integer timeout;
        @Value("${spring.redis.jedis.pool.shutdown-timeout:7200}")
        private Integer shutdowntimeout;
 private Logger logger = LoggerFactory.getLogger(JedisPoolFactory.class);
 @Bean
	public JedisPool generateJedisPoolFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(maxActive);
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMinIdle(minIdle);
		JedisPool jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
		logger.info("JedisPool注入成功！");
        logger.info("redis地址：" + host + ":" + port);
		return jedisPool;
	}

}
