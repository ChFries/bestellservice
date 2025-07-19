package prv.fries.bestellservice.bestellung.rest.produkt;

import org.openapitools.client.api.ProduktApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import prv.fries.bestellservice.generated.client.ApiClient;

@Configuration
public class ProduktConfiguration {

    @Value("${produkt.url}")
    private String produktUrl;

    @Bean("produktApiClient")
    public ApiClient apiClient(RestTemplate restTemplate) {
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(produktUrl);
        return apiClient;
    }

    @Bean
    public ProduktApi produktApi(@Qualifier("produktApiClient") ApiClient apiClient) {
        return new ProduktApi(apiClient);
    }
}
