package cn.lghuntfor.commons.cache.redis.common;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;

/**
 * key的生成策略
 * 注解上没有手动设置key时, 将使用以下生成策略
 * @author liaogang
 * @date 2020/5/18
 */
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder builder = new StringBuilder();
        builder.append(target.getClass().getSimpleName()).append(":")
                .append(method.getName()).append(":")
                .append(SimpleKeyGenerator.generateKey(params));
        return builder.toString();
    }
}
