package com.asconsoft.gintaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)  
				.select() 
				.apis(RequestHandlerSelectors.basePackage("com.asconsoft.gintaa.business.api"))
				.paths(PathSelectors.regex("/.*"))                          
				.build().apiInfo(apiEndPointsInfo());                                           
	}
	
	private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Gintaa-business Api Documentation")
            .description("Gintaa Business Management REST API")
            .contact(new Contact("Ascon Infrastructure India Ltd", "http://www.asconindia.com/", null))
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .version("v1")
            .build();
    }

}
