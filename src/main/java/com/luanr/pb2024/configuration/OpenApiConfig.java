package com.luanr.pb2024.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> openApi.getComponents()
                .addSchemas("ObjectId", new StringSchema().example("664b72be760f261fd5698231"));
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
    }
}