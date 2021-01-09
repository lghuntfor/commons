package cn.lghuntfor.commons.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举类：定义一些通用返回code与msg
 * @author liaogang
 * @date 2020/9/10 09:14
 */
@Getter
@AllArgsConstructor
public enum ReturnMsg {

    SUCCESS(1, "成功")
    ,BAD_REQUEST(400, "请求信息有误")
    ,UNAUTHORIZED(401, "未认证")
    ,FORBIDDEN(403, "没有权限")
    ,NOT_FOUND(404, "资源不存在")
    ,SERVER_ERROR(500, "服务器异常")
    ;

    private int code;

    private String msg;

}
