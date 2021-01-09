package cn.lghuntfor.commons.cat.mybatis.config;

import cn.lghuntfor.commons.cat.mybatis.interceptor.CatMybatisInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis 拦截器配置
 */
@Configuration
@ConditionalOnClass(value = {SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class CatInterceptorConfig {

    @Bean
    public CatMybatisInterceptor catMybatisInterceptor() {
        return new CatMybatisInterceptor();
    }
}
