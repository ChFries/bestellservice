package prv.fries.bestellservice.bestellung.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(
        basePackages = "prv.fries.bestellservice",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASPECTJ,
                pattern = "prv.fries.bestellservice.bestellung.rest.*.*"
        )
)
@Profile("RABBITMQ")
public class RabbitPackageConfiguration {
}
