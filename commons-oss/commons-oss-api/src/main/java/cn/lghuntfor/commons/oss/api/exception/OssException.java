package cn.lghuntfor.commons.oss.api.exception;

import cn.lghuntfor.commons.common.exception.CommonException;

/**
 * oss异常类
 * @author liaogang
 * @date 2020/10/13 09:54
 */
public class OssException extends CommonException {

    public OssException() {
    }

    public OssException(Integer exceptionCode, String exceptionMsg) {
        super(exceptionCode, exceptionMsg);
    }

    public OssException(Integer exceptionCode, String exceptionMsg, Throwable throwable) {
        super(exceptionCode, exceptionMsg, throwable);
    }
}
