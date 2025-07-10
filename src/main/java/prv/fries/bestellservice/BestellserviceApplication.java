package prv.fries.bestellservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
public class BestellserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BestellserviceApplication.class, args);
	}

}
