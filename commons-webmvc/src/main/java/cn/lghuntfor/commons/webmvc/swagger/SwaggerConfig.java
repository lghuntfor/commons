package cn.lghuntfor.commons.webmvc.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.configuration.SwaggerCommonConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 * 默认关闭, 需要配置后才可开启
 * @author liaogang
 * @date 2020/9/1 15:51
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(value = {SwaggerCommonConfiguration.class})
@EnableSwagger2
@ConditionalOnProperty(value = "swagger.enable", havingValue = "true")
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.any()).paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Swagger API Doc")
                .description("This is a restful api document of Swagger.")
                .version("1.0")
                .build();
    }


}
