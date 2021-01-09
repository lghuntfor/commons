package cn.lghuntfor.commons.cat.feign.interceptor;

import cn.lghuntfor.commons.cat.common.CatContext;
import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Transaction;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign 拦截器
 * @author lghuntfor
 * @date 2020/11/27
 */
public class CatFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Transaction t = Cat.newTransaction(CatConstants.TYPE_REMOTE_CALL, requestTemplate.request().url());

        try {
            /** 保存和传递CAT调用链上下文 */
            CatContext context = new CatContext();
            Cat.logRemoteCallClient(context);
            Cat.logMetricForCount("FeignInvokeApi");

            requestTemplate.header(CatContext.ROOT, context.getProperty(CatContext.ROOT));
            requestTemplate.header(CatContext.PARENT, context.getProperty(CatContext.PARENT));
            requestTemplate.header(CatContext.CHILD, context.getProperty(CatContext.CHILD));

            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            Cat.logError(e);
            t.setStatus(e);
        } finally {
            t.complete();
        }
    }
}
