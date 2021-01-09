package cn.lghuntfor.commons.cat.spring;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 针对spring bean的监控埋点
 * @author lghuntfor
 * @date 2020/12/6
 */
public abstract class CatAbstractAspect {

    /**
     * 添加spring bean的监控埋点
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object executeAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        StringBuilder name = new StringBuilder(className).append(".").append(methodName);
        Object result = null;
        Transaction t = null;
        try {
            t = Cat.newTransaction("Spring", name.toString());
            Cat.logMetricForCount("SpringInvoke");
            result = joinPoint.proceed();
            t.setStatus(Transaction.SUCCESS);
        } catch (Throwable e) {
            t.setStatus(e);
            Cat.logError(e);
            throw e;
        } finally {
            t.complete();
        }
        return result;
    }

}
