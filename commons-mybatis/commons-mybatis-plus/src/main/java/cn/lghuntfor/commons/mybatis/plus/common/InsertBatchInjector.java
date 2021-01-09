package cn.lghuntfor.commons.mybatis.plus.common;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

/**
 * 添加批量插入的方法注入
 * @author liaogang
 * @date 2020/11/18 16:51
 */
public class InsertBatchInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }
}
