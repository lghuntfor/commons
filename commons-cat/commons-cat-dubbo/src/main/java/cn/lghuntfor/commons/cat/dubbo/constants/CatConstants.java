package cn.lghuntfor.commons.cat.dubbo.constants;

/**
 * Created by bieber on 2015/11/12.
 */
public interface CatConstants {

    String CROSS_CONSUMER ="DubboCall";

    String CROSS_SERVER = "DubboService";
    
    String PROVIDER_APPLICATION_NAME="serverApplicationName";
    
    String CONSUMER_CALL_SERVER="DubboCall.server";
    
    String CONSUMER_CALL_APP="DubboCall.app";
    
    String CONSUMER_CALL_PORT="DubboCall.port";
    
    String PROVIDER_CALL_SERVER="DubboService.client";
    
    String PROVIDER_CALL_APP="DubboService.app";

    String FORK_MESSAGE_ID="m_forkedMessageId";

    String FORK_ROOT_MESSAGE_ID="m_rootMessageId";

    String FORK_PARENT_MESSAGE_ID="m_parentMessageId";

    String DUBBO_BIZ_ERROR="DUBBO_BIZ_ERROR";

    String DUBBO_TIMEOUT_ERROR="DUBBO_TIMEOUT_ERROR";

    String DUBBO_REMOTING_ERROR="DUBBO_REMOTING_ERROR";
}
