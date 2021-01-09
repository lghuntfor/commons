package cn.lghuntfor.commons.webmvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * controller基础类, 封装一些通用的处理方法等
 * @author liaogang
 * @date 2020/9/4 11:18
 */
public abstract class BaseController {

    /**
     * 日志对象, 子类可直接复用, 不需要再定义
     */
    protected Logger log = LoggerFactory.getLogger(this.getClass());

}
