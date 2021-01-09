package cn.lghuntfor.commons.sms.qiniu.service.impl;

import cn.lghuntfor.commons.common.constants.ReturnMsg;
import cn.lghuntfor.commons.common.result.ReturnData;
import cn.lghuntfor.commons.sms.api.SmsException;
import cn.lghuntfor.commons.sms.api.service.SmsService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 七牛短信服务实现类
 * @author liaogang
 * @date 2020/9/16 11:53
 */
public class QiniuSmsServiceImpl implements SmsService {

    @Autowired
    private SmsManager smsManager;

    @Override
    public ReturnData<Object> sendMessage(String[] mobiles, String signature, String templateId, Map<String, String> params) {
        try {
            Response resp = smsManager.sendMessage(templateId, mobiles, params);
            return new ReturnData<>(resp.getInfo());
        } catch (QiniuException e) {
            throw new SmsException(ReturnMsg.SERVER_ERROR.getCode(), "短信发送失败", e);
        }
    }

    @Override
    public ReturnData<Object> sendOverseaMessage(String mobile, String signature, String templateId, Map<String, String> params) {
        try {
            Response resp = smsManager.sendOverseaMessage(templateId, mobile, params);
            return new ReturnData<>(resp.getInfo());
        } catch (QiniuException e) {
            throw new SmsException(ReturnMsg.SERVER_ERROR.getCode(), "短信发送失败", e);
        }
    }

    @Override
    public ReturnData<Object> sendFulltextMessage(String[] mobiles, String content) {
        try {
            Response resp = smsManager.sendFulltextMessage(mobiles, content);
            return new ReturnData<>(resp.getInfo());
        } catch (QiniuException e) {
            throw new SmsException(ReturnMsg.SERVER_ERROR.getCode(), "短信发送失败", e);
        }
    }

}
