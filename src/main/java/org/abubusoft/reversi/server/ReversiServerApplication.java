package org.abubusoft.reversi.server;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class ReversiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReversiServerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(name -> {
				User user = new User(name, name.toLowerCase() + "@domain.com");
				userRepository.save(user);
			});
			userRepository.findAll().forEach(System.out::println);
		};
	}

	@Bean
	public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
		return new OpenAPI().info(new Info().title("Foobar API")
						.version(appVersion)
						.description("This is a sample Foobar server created using springdocs - a library for OpenAPI 3 with spring boot.")
						.termsOfService("http://swagger.io/terms/")
						.license(new License().name("Apache 2.0")
										.url("http://springdoc.org")));
	}
}
