package cn.lghuntfor.commons.cat.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

/**
 * 针对所有springbean的监控
 * @author lghuntfor
 * @date 2020/11/29
 */
@Aspect
@Configuration
public class CatSpringAspect extends CatAbstractAspect {

    /**
     * 切入点
     * 根据实际情况进行修改包名
     */
    @Pointcut("execution(* com.ndccloud..*.*(..))")
    public void methodIntercept(){}

    /**
     * 环绕通知
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "methodIntercept()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.executeAround(joinPoint);
    }
}
