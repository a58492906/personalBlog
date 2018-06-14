package com.mark.demo.configution;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * ${DESCRIPTION}
 *
 * @author eric
 * 
 */
@EnableSwagger2
@Configuration
public class Swagger2Config {

	@Bean  
    public Docket createRestApi() {  
        return new Docket(DocumentationType.SWAGGER_2)  
                .apiInfo(apiInfo())  
                .select()  
                .apis(RequestHandlerSelectors.basePackage("com.mark.demo.controller"))  
                .paths(PathSelectors.any())  
                .build();  
    }  
      
    @SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {  
        return new ApiInfoBuilder()  
                .title("后台基础数据API说明文档")  
                .description("author-eric")  
                //.termsOfServiceUrl("http://mindao.com.cn")  
                .contact("mengmeng")  
                .version("1.0")  
                .build();  
    }  
}