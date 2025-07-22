package prv.fries.bestellservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@ComponentScan(
		basePackages = "prv.fries.bestellservice.bestellung.configuration"
)
public class BestellserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BestellserviceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
