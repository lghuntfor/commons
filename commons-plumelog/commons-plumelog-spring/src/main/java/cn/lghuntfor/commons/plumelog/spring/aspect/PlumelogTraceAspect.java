package cn.lghuntfor.commons.plumelog.spring.aspect;

import com.plumelog.trace.aspect.AbstractAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * aop
 * @author lghuntfor
 * @date 2020/12/3
 */
@Aspect
public class PlumelogTraceAspect extends AbstractAspect {

    @Around("execution(* cn.lghuntfor..*.*(..))")
    public Object around(JoinPoint joinPoint) throws Throwable {
        return super.aroundExecute(joinPoint);
    }
}
