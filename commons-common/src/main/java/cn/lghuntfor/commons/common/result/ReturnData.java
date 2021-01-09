package cn.lghuntfor.commons.common.result;

import cn.lghuntfor.commons.common.constants.ReturnMsg;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回数据格式封装类
 * {"code":200, "msg":"成功", "data":"返回数据内容, 可任意类型"}
 * @author liaogang
 * @date 2020/9/2 17:00
 */
@Data
public class ReturnData<T> implements Serializable {

    public static final int SUCCESS_CODE = 1;
    public static final String SUCCESS_MSG = "成功";
    public static final int FAIL_CODE = 500;
    public static final String FAIL_MSG = "服务器异常";

    /**
     * 响应状态码，1表示正常
     */
    private int code;

    /**
     * 返回的消息提示
     */
    private String msg;

    private T data;

    public ReturnData() {
        this.code = SUCCESS_CODE;
        this.msg = SUCCESS_MSG;
    }

    public ReturnData(T data) {
        this.code = SUCCESS_CODE;
        this.msg = SUCCESS_MSG;
        this.data = data;
    }
    public ReturnData(ReturnMsg returnMsg) {
        this.code = returnMsg.getCode();
        this.msg = returnMsg.getMsg();
    }

    public ReturnData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ReturnData buildErrorMsg() {
        this.code = FAIL_CODE;
        this.msg = FAIL_MSG;
        return this;
    }

    public ReturnData setErrorMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }
    public ReturnData setErrorMsg(ReturnMsg returnMsg) {
        this.code = returnMsg.getCode();
        this.msg = returnMsg.getMsg();
        return this;
    }
}
