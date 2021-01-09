package cn.lghuntfor.commons.cat.mybatis.interceptor;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;


/**
 * Mybatis SQL 拦截器
 */
@Intercepts({
        @Signature(method = "query", type = Executor.class,
                args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class }),
        @Signature(method = "query", type = Executor.class,
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(method = "update", type = Executor.class, args = { MappedStatement.class, Object.class })
})
public class CatMybatisInterceptor implements Interceptor {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    private Executor target;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = this.getStatement(invocation);
        String methodName = this.getMethodName(mappedStatement);
        String sql = this.getSql(invocation,mappedStatement);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        Transaction t = Cat.newTransaction("SQL", methodName);
        Cat.logEvent("SQL.Method", sqlCommandType.name().toLowerCase(), Message.SUCCESS, sql);
        Cat.logMetricForCount("SQLCount");
        Object returnObj = null;
        try {
            returnObj = invocation.proceed();
            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            t.setStatus(e);
            Cat.logError(e);
            logger.error("Method={}, SQL={}", methodName, sql);
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            t.complete();
        }
        return returnObj;
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
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            this.target = (Executor) target;
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

}