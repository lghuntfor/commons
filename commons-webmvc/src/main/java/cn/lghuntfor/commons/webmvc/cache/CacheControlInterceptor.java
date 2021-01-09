package cn.lghuntfor.commons.webmvc.cache;

import cn.hutool.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * wet http 缓存拦截器
 * @author liaogang
 * @date 2021/1/7 9:03
 */
@AllArgsConstructor
public class CacheControlInterceptor implements HandlerInterceptor {

    /** 是否开启 */
    private boolean enable;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod) || !enable) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        CacheControl controlAnnotation = method.getAnnotation(CacheControl.class);
        if (controlAnnotation == null) {
            return true;
        }
        long minAge = controlAnnotation.minAge();
        long maxAge = controlAnnotation.maxAge();
        /** 缓存时间, 单位秒, 随机取最小最大之间的值 */
        long cacheTimes = (minAge >= maxAge) ? minAge : RandomUtil.randomLong(minAge, maxAge);

        response.setDateHeader("Last-Modified", new Date().getTime());
        response.setDateHeader("Expires", System.currentTimeMillis() + 1000 * cacheTimes);
        response.setHeader("Cache-Control", "maxAge=" + cacheTimes);
        return true;
    }
}
