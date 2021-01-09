package cn.lghuntfor.commons.mybatis.plus.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;


/**
 * mybatis SQL 日志打印
 * @author liaogang
 * @date 2020/9/22
 */
@Intercepts({
        @Signature(method = "query", type = Executor.class,
                args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class }),
        @Signature(method = "query", type = Executor.class,
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(method = "update", type = Executor.class, args = { MappedStatement.class, Object.class })
})
public class MybatisSQLInterceptor implements Interceptor {

    private Executor target;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${mybatis.show.log.enable:true}")
    private Boolean logEnable;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = this.getStatement(invocation);
        String methodName = this.getMethodName(mappedStatement);
        String sql = this.getSql(invocation,mappedStatement);

        Object returnObj = null;
        try {
            long start = System.currentTimeMillis();
            returnObj = invocation.proceed();
            long end = System.currentTimeMillis();
            long time = end - start;

            String showSql = showSql(sql, methodName, time, returnObj);
            if (logEnable) {
                logger.info(showSql);
            }
        } catch (Exception e) {
            logger.error("Method={}, SQL={}", methodName, sql);
            logger.error(e.getMessage(), e);
            throw e;
        }
        return returnObj;
    }

    public static String showSql(String sql, String sqlId, long time,Object returnValue) {
        int total = 1;
        if(returnValue==null) {
            total = 0;
        } else if(returnValue instanceof  List) {
            total = ((List) returnValue).size();
        }
        StringBuilder str = new StringBuilder(100);
        str.append(sqlId).append("(), Time:[").append(time).append("ms")
                .append("],Total:[").append(total)
                .append("],SQL:[").append(sql).append("]");
        return str.toString();
    }

    private MappedStatement getStatement(Invocation invocation) {
        return (MappedStatement)invocation.getArgs()[0];
    }

    private String getMethodName(MappedStatement mappedStatement) {
        String[] strArr = mappedStatement.getId().split("\\.");
        String methodName = strArr[strArr.length - 2] + "." + strArr[strArr.length - 1];
        return methodName;
    }

    private String getSql(Invocation invocation, MappedStatement mappedStatement) {
        Object parameter = null;
        if(invocation.getArgs().length > 1){
            parameter = invocation.getArgs()[1];
        }

        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = sqlResolve(configuration, boundSql);
        return sql;
    }

    private String getDataSourceUrl(DataSource dataSource) throws NoSuchFieldException, IllegalAccessException {
        String url = null;
        if(dataSource instanceof PooledDataSource) {
            Field dataSource1 = dataSource.getClass().getDeclaredField("dataSource");
            dataSource1.setAccessible(true);
            UnpooledDataSource dataSource2 = (UnpooledDataSource)dataSource1.get(dataSource);
            url =dataSource2.getUrl();
        } else {
            //other dataSource expand
        }
        return url;
    }

    public String sqlResolve(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(resolveParameterValue(parameterObject)));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(resolveParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(resolveParameterValue(obj)));
                    }
                }
            }
        }
        return sql;
    }

    private String resolveParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    @Override
    public void setProperties(Properties properties) {
    }

}