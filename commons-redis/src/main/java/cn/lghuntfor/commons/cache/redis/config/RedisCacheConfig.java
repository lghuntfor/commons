package cn.lghuntfor.commons.cache.redis.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cn.lghuntfor.commons.cache.redis.common.CacheExpireEnum;
import cn.lghuntfor.commons.cache.redis.common.CustomCacheErrorHandler;
import cn.lghuntfor.commons.cache.redis.common.CustomKeyGenerator;
import cn.lghuntfor.commons.cache.redis.common.NullValueSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


/**
 * 配置spring-cache相关bean,
 * 开启缓存注解功能
 * @author liaogang
 * @date 2020/9/1
 */
@Configuration
@ConditionalOnBean(value = RedisBaseConfig.class)
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisConnectionFactory factory;

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

    /**
     * 配置sping cache 管理器
     * @author liaogang
     * @date 2020/5/18
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        logger.info("===== init cache RedisCacheManager =====");
        CacheKeyPrefix cacheKeyPrefix = (cacheName -> "CACHE:" + cacheName + ":");
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(this.getObjectMapper());
        RedisSerializationContext.SerializationPair<Object> serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer);

        RedisCacheConfiguration cacheConfig = this.buildCacheConfig(cacheKeyPrefix, serializationPair, CacheExpireEnum.DAY.getTtl());

        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        for (CacheExpireEnum value : CacheExpireEnum.VALUES) {
            configMap.put(value.getCacheName(), this.buildCacheConfig(cacheKeyPrefix, serializationPair, value.getTtl()));
        }

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, cacheConfig, configMap);
        return redisCacheManager;
    }

    /**
     * 创建一个RedisCacheConfiguration
     */
    public RedisCacheConfiguration buildCacheConfig(CacheKeyPrefix cacheKeyPrefix, RedisSerializationContext.SerializationPair<Object> serializationPair, long expire) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .computePrefixWith(cacheKeyPrefix)
            .entryTtl(Duration.ofSeconds(expire))
            .serializeValuesWith(serializationPair);
        return cacheConfig;
    }

    /**
     * 对象序列化
     * @return
     */
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule((new SimpleModule()).addSerializer(new NullValueSerializer(null)));
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // 解析器支持解析结束符
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        //反序列化忽略不存在字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //将该标记放在属性上，如果该属性为NULL则不参与序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }
}
