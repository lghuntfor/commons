package cn.lghuntfor.commons.sms.api;

import cn.lghuntfor.commons.common.exception.CommonException;
import lombok.Data;

/**
 * 短信异常
 * @author liaogang
 * @date 2020/11/3 14:18
 */
@Data
public class SmsException extends CommonException {

    public SmsException(Integer exceptionCode, String exceptionMsg) {
        super(exceptionCode, exceptionMsg);
    }

    public SmsException(Integer exceptionCode, String exceptionMsg, Throwable throwable) {
        super(exceptionCode, exceptionMsg, throwable);
    }

}
