package cn.lghuntfor.commons.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用异常定义
 * @author liaogang
 * @date 2020/9/10 08:58
 */
@Data
public class CommonException extends RuntimeException {

    /**
     * 异常时返回code码
     */
    private Integer exceptionCode;

    /**
     * 异常时返回提示信息
     */
    private String exceptionMsg;

    public CommonException() {
    }

    public CommonException(Integer exceptionCode, String exceptionMsg) {
        this.exceptionCode = exceptionCode;
        this.exceptionMsg = exceptionMsg;
    }

    public CommonException(Integer exceptionCode, String exceptionMsg, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.exceptionMsg = exceptionMsg;
    }

    public CommonException(Integer exceptionCode, String exceptionMsg, Throwable throwable) {
        super(throwable);
        this.exceptionCode = exceptionCode;
        this.exceptionMsg = exceptionMsg;
    }
}
