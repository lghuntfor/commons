package cn.lghuntfor.commons.common.constants;

import java.util.regex.Pattern;

/**
 * 常用正则常量
 * @author liaogang
 * @date 2020/11/4 11:44
 */
public interface RegexConst {

    /**
     * 英文字母 、数字和下划线
     */
    String GENERAL_REGEX = "^\\w+$";
    Pattern GENERAL = Pattern.compile(GENERAL_REGEX);

    /**
     * 移动电话
     */
    String MOBILE_REGEX = "(?:0|86|\\+86)?1[3-9]\\d{9}";
    Pattern MOBILE = Pattern.compile(MOBILE_REGEX);

    /**
     * 邮件
     */
    String EMAIL_REGEX = "(\\w|.)+@\\w+(\\.\\w+){1,2}";
    Pattern EMAIL = Pattern.compile(EMAIL_REGEX);


}
