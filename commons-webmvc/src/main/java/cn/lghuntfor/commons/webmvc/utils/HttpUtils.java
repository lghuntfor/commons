package cn.lghuntfor.commons.webmvc.utils;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * http工具类
 * @author liaogang
 * @date 2020/10/28 15:01
 */
public class HttpUtils {

    public static final String X_REAL_IP = "X-Real-IP";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * 获取的客户端的真实ip地址
     * @author liaogang
     * @date 2020/10/28
     * @param req
     * @return java.lang.String
     */
    public static String getClientIp(HttpServletRequest req) {
        if (StrUtil.isNotBlank(req.getHeader(X_REAL_IP))) {
            return req.getHeader(X_REAL_IP);
        }
        if (StrUtil.isNotBlank(req.getHeader(X_FORWARDED_FOR))) {
            return req.getHeader(X_FORWARDED_FOR);
        }
        return req.getRemoteAddr();
    }

}
