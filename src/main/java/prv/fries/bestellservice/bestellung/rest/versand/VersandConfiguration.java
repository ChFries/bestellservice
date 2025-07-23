package prv.fries.bestellservice.bestellung.rest.versand;

import org.openapitools.client.api.VersandApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import prv.fries.bestellservice.ApiClient;

@Configuration
public class VersandConfiguration {

    @Value("${versand.url}")
    private String versandUrl;

    @Bean("versandApiClient")
    public ApiClient apiClient(RestTemplate restTemplate) {
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(versandUrl);
        return apiClient;
    }

    @Bean
    public VersandApi versandApi(@Qualifier("versandApiClient") ApiClient apiClient) {
        return new VersandApi(apiClient);
    }
}
