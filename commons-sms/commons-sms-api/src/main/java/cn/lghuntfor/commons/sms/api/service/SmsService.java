package cn.lghuntfor.commons.sms.api.service;

import cn.lghuntfor.commons.common.result.ReturnData;

import java.util.Map;

/**
 * 短信服务接口
 * 在此处定义通用的短信操作方法
 * 方便扩展各类型的短信实现(七牛云, 阿里云, 华为云等)
 * @author liaogang
 * @date 2020/8/28 11:41
 */
public interface SmsService {

    /**
     * 手机短信发送
     * @author liaogang
     * @date 2020/9/16
     * @param mobiles 手机号数组
     * @param signature 签名
     * @param templateId 短信类型或模版的id
     * @param params 发送参数内容
     * @return cn.lghuntfor.commons.common.result.ReturnData<java.lang.Object>
     */
    ReturnData<Object> sendMessage(String[] mobiles, String signature, String templateId, Map<String, String> params);

    /**
     * 发送国外短信
     * @author liaogang
     * @date 2020/11/2
     * @param mobile
     * @param signature
     * @param templateId
     * @param params 发送参数内容
     * @return cn.lghuntfor.commons.common.result.ReturnData<java.lang.Object>
     */
    ReturnData<Object> sendOverseaMessage(String mobile, String signature, String templateId, Map<String, String> params);

    /**
     * 发送自定义内容短信
     * @author liaogang
     * @date 2020/11/2
     * @param mobile
     * @param content
     * @return cn.lghuntfor.commons.common.result.ReturnData<java.lang.Object>
     */
    ReturnData<Object> sendFulltextMessage(String[] mobile, String content);


}
