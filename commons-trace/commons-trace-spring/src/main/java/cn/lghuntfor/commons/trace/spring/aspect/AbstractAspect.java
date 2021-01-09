package cn.lghuntfor.commons.trace.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 *
 * @author lghuntfor
 * @date 2020/12/6
 */
@Slf4j
public abstract class AbstractAspect {

    public Object executeAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        Object result = joinPoint.proceed();
        if (!(joinPoint.getTarget() instanceof BeanPostProcessor)) {
            log.info("{}.{}(), elapsed time = {}ms", className, methodName, (System.currentTimeMillis() - startTime));
        }
        return result;
    }

}
