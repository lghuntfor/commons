package cn.lghuntfor.commons.cat.common;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * Cat上下文信息
 * @author lghuntfor
 * @date 2020/11/24
 */
public class CatContext implements Cat.Context {

    /** 存储内容 */
    private Map<String, String> properties = new HashMap<>();

    @Override
    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }
}
