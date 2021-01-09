package cn.lghuntfor.commons.template;

import cn.lghuntfor.commons.template.builder.ClasspathTemplateBuilder;
import cn.lghuntfor.commons.template.builder.FileTemplateBuilder;
import cn.lghuntfor.commons.template.builder.StringTemplateBuilder;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;

import java.util.Map;

/**
 * 模版解析工具类
 * @author liaogang
 * @date 2020/11/2 15:47
 */
public class TemplateUtils {

    /**
     * 字符串模版解析工具
     * @author liaogang
     * @date 2020/11/3
     * @param templateText 模版内容
     * @param params 解析参数
     * @return java.lang.String
     */
    public static String parseText(String templateText, Map params) {
        GroupTemplate groupTemplate = StringTemplateBuilder.getStringTemplate();
        Template template = groupTemplate.getTemplate(templateText);
        if (params != null) {
            template.binding(params);
        }
        return template.render();
    }

    /**
     * 文件模版解析工具
     * @author liaogang
     * @date 2020/11/3
     * @param rootDir 指定目录
     * @param path 基于目录的文件相对路径
     * @param params 解析参数
     * @return java.lang.String
     */
    public static String parseFile(String rootDir, String path, Map params) {
        GroupTemplate groupTemplate = FileTemplateBuilder.getFileTemplate(rootDir);
        Template template = groupTemplate.getTemplate(path);
        if (params != null) {
            template.binding(params);
        }
        return template.render();
    }

    /**
     * classpath文件模版解析工具
     * @author liaogang
     * @date 2020/11/3
     * @param classpath 基于classpath下的文件相对路径
     * @param params 解析参数
     * @return java.lang.String
     */
    public static String parseClasspathFile(String classpath, Map params) {
        GroupTemplate groupTemplate = ClasspathTemplateBuilder.getClasspathTemplate();
        Template template = groupTemplate.getTemplate(classpath);
        if (params != null) {
            template.binding(params);
        }
        return template.render();
    }

}
