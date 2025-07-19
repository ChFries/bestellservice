package prv.fries.bestellservice.bestellung.rest.payment;

import org.openapitools.client.api.ZahlungApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import prv.fries.bestellservice.generated.client.ApiClient;

@Configuration
public class PaymentConfiguration {


    @Value("${payment.url}")
    private String paymentUrl;

    @Bean("paymentApiClient")
    public ApiClient apiClient(RestTemplate restTemplate) {
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(paymentUrl);
        return apiClient;
    }

    @Bean
    public ZahlungApi zahlungApi(@Qualifier("paymentApiClient") ApiClient apiClient) {
        return new ZahlungApi(apiClient);
    }
}
