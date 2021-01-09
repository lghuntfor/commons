package cn.lghuntfor.commons.logs.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * 日志打印
 * @author liaogang
 * @date 2020/9/15 16:08
 */
@Aspect
@Configuration
@Slf4j
public class BeanLogAop {

    @Value("${log.print.enable:true}")
    private Boolean logPrintEnable;

    /**
     * 切入点, 针对cn.lghuntfor包路径下所有bean的方法
     * @author liaogang
     * @date 2020/9/15
     * @param
     * @return void
     */
    @Pointcut("execution(* cn.lghuntfor..*.*(..))  ")
    public void methodIntercept(){};


    /**
     * 环绕通知, 日志打印
     * @author liaogang
     * @date 2020/9/15
     * @param joinPoint
     * @return java.lang.Object
     */
    @Around(value = "methodIntercept()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        Object result = joinPoint.proceed();
        if (logPrintEnable && !(joinPoint.getTarget() instanceof BeanPostProcessor)) {
            log.info("{}.{}(), elapsed time = {}ms", className, methodName, (System.currentTimeMillis() - startTime));
        }
        return result;
    }
}
