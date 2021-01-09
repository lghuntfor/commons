package cn.lghuntfor.commons.cat.dubbo.filter;

import cn.lghuntfor.commons.cat.common.CatContext;
import cn.lghuntfor.commons.cat.dubbo.constants.CatConstants;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.AbstractMessage;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.support.RpcUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * cat dubbo filter
 * 传递调用链路信息
 * 监控cat调用信息
 * @author lghuntfor
 * @date 2020/11/24
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = -9000)
public class CatDubboFilter implements Filter {

    /**
     * CatContext
     */
    private static final InheritableThreadLocal<CatContext> CAT_CONTEXT = new InheritableThreadLocal<>();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = invoker.getUrl();
        String sideKey = url.getParameter(CommonConstants.SIDE_KEY);
        String loggerName = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
        String type = CatConstants.CROSS_CONSUMER;
        if (CommonConstants.PROVIDER_SIDE.equals(sideKey)) {
            type = CatConstants.CROSS_SERVER;
        }
        Transaction transaction = Cat.newTransaction(type, loggerName);
        Result result = null;
        try {

            CatContext context = getContext();
            if (CommonConstants.CONSUMER_SIDE.equals(sideKey)) {
                RpcContext.getContext().setAttachment(CommonConstants.APPLICATION_KEY,invoker.getUrl().getParameter(CommonConstants.APPLICATION_KEY));
                createConsumerCross(url, transaction);
                Cat.logMetricForCount("DubboConsumerInvoke");
                Cat.logRemoteCallClient(context);
            } else {
                createProviderCross(transaction);
                Cat.logMetricForCount("DubboProviderInvoke");
                Cat.logRemoteCallServer(context);
            }
            setAttachment(context);
            result = invoker.invoke(invocation);

            boolean isAsync = RpcUtils.isAsync(invoker.getUrl(), invocation);

            //异步的不能判断是否有异常,这样会阻塞住接口(<AsyncRpcResult>hasException->getRpcResult->resultFuture.get()
            if (isAsync) {
                transaction.setStatus(Transaction.SUCCESS);
                return result;
            }

            if (result.hasException()) {
                //给调用接口出现异常进行打点
                Throwable throwable = result.getException();
                Event event = null;
                if (RpcException.class == throwable.getClass()) {
                    Throwable caseBy = throwable.getCause();
                    if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
                        event = Cat.newEvent(CatConstants.DUBBO_TIMEOUT_ERROR, loggerName);
                    } else {
                        event = Cat.newEvent(CatConstants.DUBBO_REMOTING_ERROR, loggerName);
                    }
                } else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
                    event = Cat.newEvent(CatConstants.DUBBO_REMOTING_ERROR, loggerName);
                } else {
                    event = Cat.newEvent(CatConstants.DUBBO_BIZ_ERROR, loggerName);
                }
                event.setStatus(result.getException());
                completeEvent(event);
                transaction.addChild(event);
                transaction.setStatus(result.getException().getClass().getSimpleName());
            } else {
                transaction.setStatus(Transaction.SUCCESS);
            }
            return result;
        } catch (RuntimeException e) {
            Cat.logError(e);
            Event event = null;
            if (RpcException.class == e.getClass()) {
                Throwable caseBy = e.getCause();
                if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
                    event = Cat.newEvent(CatConstants.DUBBO_TIMEOUT_ERROR, loggerName);
                } else {
                    event = Cat.newEvent(CatConstants.DUBBO_REMOTING_ERROR, loggerName);
                }
            } else {
                event = Cat.newEvent(CatConstants.DUBBO_BIZ_ERROR, loggerName);
            }
            event.setStatus(e);
            completeEvent(event);
            transaction.addChild(event);
            transaction.setStatus(e.getClass().getSimpleName());
            if (result == null) {
                throw e;
            } else {
                return result;
            }
        } finally {
            transaction.complete();
            CAT_CONTEXT.remove();
        }
    }

    private String getProviderAppName(URL url) {
        String appName = url.getParameter(CatConstants.PROVIDER_APPLICATION_NAME);
        if (StringUtils.isEmpty(appName)) {
            String interfaceName = url.getParameter(CommonConstants.INTERFACE_KEY);
            appName = interfaceName.substring(0, interfaceName.lastIndexOf('.'));
        }
        return appName;
    }

    private void setAttachment(CatContext context) {
        RpcContext.getContext().setAttachment(CatContext.ROOT, context.getProperty(CatContext.ROOT));
        RpcContext.getContext().setAttachment(CatContext.CHILD, context.getProperty(CatContext.CHILD));
        RpcContext.getContext().setAttachment(CatContext.PARENT, context.getProperty(CatContext.PARENT));
    }

    private CatContext getContext() {
        CatContext context = CAT_CONTEXT.get();
        if (context == null) {
            context = initContext();
            CAT_CONTEXT.set(context);
        }
        return context;
    }

    private CatContext initContext() {
        CatContext context = new CatContext();
        Map<String, Object> attachments = RpcContext.getContext().getObjectAttachments();
        if (attachments != null && attachments.size() > 0) {
            for (Map.Entry<String, Object> entry : attachments.entrySet()) {
                if (CatContext.CHILD.equals(entry.getKey())
                        || CatContext.ROOT.equals(entry.getKey())
                        || CatContext.PARENT.equals(entry.getKey())) {
                    context.addProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }
        return context;
    }

    private void createConsumerCross(URL url, Transaction transaction) {
        Event crossAppEvent = Cat.newEvent(CatConstants.CONSUMER_CALL_APP, getProviderAppName(url));
        Event crossServerEvent = Cat.newEvent(CatConstants.CONSUMER_CALL_SERVER, url.getHost());
        Event crossPortEvent = Cat.newEvent(CatConstants.CONSUMER_CALL_PORT, url.getPort() + "");
        crossAppEvent.setStatus(Event.SUCCESS);
        crossServerEvent.setStatus(Event.SUCCESS);
        crossPortEvent.setStatus(Event.SUCCESS);
        completeEvent(crossAppEvent);
        completeEvent(crossPortEvent);
        completeEvent(crossServerEvent);
        transaction.addChild(crossAppEvent);
        transaction.addChild(crossPortEvent);
        transaction.addChild(crossServerEvent);
    }

    private void completeEvent(Event event) {
        AbstractMessage message = (AbstractMessage) event;
        message.setCompleted(true);
    }

    private void createProviderCross(Transaction transaction) {
        String consumerAppName = RpcContext.getContext().getAttachment(CommonConstants.APPLICATION_KEY);
        if (StringUtils.isEmpty(consumerAppName)) {
            consumerAppName = RpcContext.getContext().getRemoteHost() + ":" + RpcContext.getContext().getRemotePort();
        }
        Event crossAppEvent = Cat.newEvent(CatConstants.PROVIDER_CALL_APP, consumerAppName);
        Event crossServerEvent = Cat.newEvent(CatConstants.PROVIDER_CALL_SERVER, RpcContext.getContext().getRemoteHost());
        crossAppEvent.setStatus(Event.SUCCESS);
        crossServerEvent.setStatus(Event.SUCCESS);
        completeEvent(crossAppEvent);
        completeEvent(crossServerEvent);
        transaction.addChild(crossAppEvent);
        transaction.addChild(crossServerEvent);
    }
}
