package prv.fries.bestellservice.bestellung.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import prv.fries.bestellservice.generated.client.produkt.ProduktVerfuegbarkeitDto;

@Component
public class BestellungPublisher {

    private final RabbitTemplate rabbitTemplate;

    public BestellungPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishBestellungAngelegt(ProduktVerfuegbarkeitDto message) {
        rabbitTemplate.convertAndSend(
                RabbitMQGeneralConfig.EXCHANGE_NAME,
                RabbitMQGeneralConfig.ROUTING_KEY_BESTELLUNG_ANGELEGT,
                message
        );
    }

    public void publishBestellungAbgeschlossen(String message) {
        rabbitTemplate.convertAndSend(
                RabbitMQGeneralConfig.EXCHANGE_NAME,
                RabbitMQGeneralConfig.ROUTING_KEY_BESTELLUNG_ABGESCHLOSSEN,
                message
        );
    }
}