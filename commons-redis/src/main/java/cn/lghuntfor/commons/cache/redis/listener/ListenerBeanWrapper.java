package cn.lghuntfor.commons.cache.redis.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * redis消息订阅bean的封装
 * @author liaogang
 * @date 2020/9/7 15:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListenerBeanWrapper {

    /** 消息监听的bean对象 */
    private Object listenBean;

    /** 消息监听的方法 */
    private Method listenMethod;

    /** method的参数长度 */
    private int argsLength;

}
