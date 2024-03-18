package se2.alpha.riskappbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RiskAppBackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(RiskAppBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RiskAppBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> logger.info("API has successfully started and is available at http://localhost:8080/health");
	}
}
