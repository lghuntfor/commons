package cn.lghuntfor.commons.webmvc.serialzation;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * json转换时, 日期的处理
 * @author liaogang
 * @since 2018/8/10
 */
public class JsonDateFormat extends DateFormat {

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        if(date!=null) {
            return new StringBuffer(String.valueOf(date.getTime()));
        }
        return null;
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        if(StrUtil.isNotBlank(source)) {
            try {
                /** 各类型时间格式的转换 */
                return DateUtil.parse(source);
            } catch (Exception e) {
                if (NumberUtil.isLong(source)) {
                    /** 时间戳类型的转换 */
                    try {
                        return new Date(NumberUtil.parseLong(source));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Date parse(String source) {
        return parse(source, null);
    }

    @Override
    public Object clone() {
        return new JsonDateFormat();
    }
}
