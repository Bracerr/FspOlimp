package com.Bracerr.AuthService.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Loyalty System Api",
                description = "Loyalty System", version = "1.0.0. \n почти все MVC контроллеры скрыты для лаконичности",
                contact = @Contact(
                        name = "Andrey Kirgizov",
                        email = "niker299@gmail.com",
                        url = "https://github.com/Bracerr"
                )

        )
)
public class OpenApiConfig {

}