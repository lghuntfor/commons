package cn.lghuntfor.commons.cache.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import cn.lghuntfor.commons.cache.redis.service.RedisService;
import cn.lghuntfor.commons.cache.redis.service.impl.RedisServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * redis基础配置
 * @author liaogang
 * @date 2020/9/1
 */
@Configuration
public class RedisBaseConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * redis模板
     * 用于重置RedisAutoConfiguration中的RedisTemplate<Object, Object>
     * @param factory
     * @return
     * @Description:
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        logger.info("===== init redis RedisTemplate =====");
        RedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setDefaultSerializer(stringRedisSerializer());
        redisTemplate.setStringSerializer(stringRedisSerializer());
        redisTemplate.setHashKeySerializer(stringRedisSerializer());

        redisTemplate.setValueSerializer(fastjsonSerializer());
        redisTemplate.setHashValueSerializer(fastjsonSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * json序列化
     * @return
     * @Description:
     */
    @Bean(name = "stringRedisSerializer")
    public StringRedisSerializer stringRedisSerializer() {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        return stringRedisSerializer;
    }

    /**
     * jdk序列化
     * @author liaogang
     * @date 2020/9/16
     */
    @Bean(name = "jdkSerializer")
    public JdkSerializationRedisSerializer jdkSerializer() {
        JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
        return jdkSerializer;
    }

    /**
     * fastjson序列化
     * @return
     * @Description:
     */
    @Bean(name = "fastjsonSerializer")
    public GenericFastJsonRedisSerializer fastjsonSerializer() {
        GenericFastJsonRedisSerializer jsonSerializer = new GenericFastJsonRedisSerializer();
        return jsonSerializer;
    }

    @Bean
    public RedisService redisCacheService() {
        RedisService cacheService = new RedisServiceImpl();
        return cacheService;
    }
}
