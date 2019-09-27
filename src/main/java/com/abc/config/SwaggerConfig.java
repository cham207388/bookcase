package com.abc.config;

import com.abc.SpringApplicationContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
@Slf4j
@Data
public class SwaggerConfig {

	@Bean
	public Docket api() {
		log.info("Swagger invoked");
		return new Docket(DocumentationType.SWAGGER_2)
				.produces(getProducesAndConsumes())
				.consumes(getProducesAndConsumes())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Bookcase API",
				"An electronic library",
				"1.0",
				"All Rights Reserved",
				new Contact("Alhagie Bai Cham",
						"www.abc.com",
						"alhagiebcham.ee@gmail.com"),
				"License of API",
				"API license URL",
				Collections.emptyList()
		);
	}
	private Set<String> getProducesAndConsumes(){
		return new HashSet<>(
				Arrays.asList("application/json", "application/xml"));
	}
	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}
}
