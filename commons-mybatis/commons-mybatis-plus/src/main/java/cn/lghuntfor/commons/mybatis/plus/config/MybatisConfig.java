package cn.lghuntfor.commons.mybatis.plus.config;

import cn.lghuntfor.commons.mybatis.plus.common.InsertBatchInjector;
import cn.lghuntfor.commons.mybatis.plus.interceptor.MybatisSQLInterceptor;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus有关的配置, 如分页配置
 * @author liaogang
 * @date 2020/9/2 16:08
 */
@Configuration
public class MybatisConfig {

    /** 分页每页显示数量的最大限制数 */
    @Value("${mybatis.page.maxLimit:1000}")
    private Long maxLimit;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor();
        interceptor.setMaxLimit(maxLimit);
        return interceptor;
    }

    @Bean
    public MybatisSQLInterceptor mybatisSQLInterceptor() {
        return new MybatisSQLInterceptor();
    }

    @Bean
    public InsertBatchInjector insertBatchInjector() {
        return new InsertBatchInjector();
    }
}
