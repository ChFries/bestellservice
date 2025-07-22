package prv.fries.bestellservice.bestellung.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQGeneralConfig {

    public static final String EXCHANGE_NAME = "bestellung.exchange";

    public static final String QUEUE_ZAHLUNG_ABGESCHLOSSEN = "bestellung.zahlung.abgeschlossen.queue";
    public static final String QUEUE_VERSAND_ABGESCHLOSSEN = "bestellung.versand.abgeschlossen.queue";
    public static final String QUEUE_BESTELLUNG_EINGEHEND = "bestellung.bestellung.eingehend.queue";
    public static final String QUEUE_PRUEFUNG_ABGESCHLOSSEN = "bestellung.pruefung.abgeschlossen.queue";

    public static final String ROUTING_KEY_ZAHLUNG_ABGESCHLOSSEN = "bestellung.zahlung.abgeschlossen";
    public static final String ROUTING_KEY_VERSAND_ABGESCHLOSSEN = "bestellung.versand.abgeschlossen";
    public static final String ROUTING_KEY_BESTELLUNG_EINGEHEND = "bestellung.bestellung.eingehend";
    public static final String ROUTING_KEY_PRUEFUNG_ABGESCHLOSSEN = "bestellung.pruefung.abgeschlossen";

    public static final String ROUTING_KEY_BESTELLUNG_ANGELEGT = "bestellung.angelegt";
    public static final String ROUTING_KEY_BESTELLUNG_ABGESCHLOSSEN = "bestellung.abgeschlossen";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public TopicExchange bestellungExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue zahlungAbgeschlossenQueue() {
        return new Queue(QUEUE_ZAHLUNG_ABGESCHLOSSEN, true);
    }

    @Bean
    public Queue versandAbgeschlossenQueue() {
        return new Queue(QUEUE_VERSAND_ABGESCHLOSSEN, true);
    }

    @Bean
    public Queue bestellungEingehendQueue() {
        return new Queue(QUEUE_BESTELLUNG_EINGEHEND, true);
    }

    @Bean
    public Queue pruefungAbgeschlossenQueue() {
        return new Queue(QUEUE_PRUEFUNG_ABGESCHLOSSEN, true);
    }

    @Bean
    public Binding bindPruefungQueueToExchange(Queue pruefungAbgeschlossenQueue, TopicExchange bestellungExchange) {
        return BindingBuilder.bind(pruefungAbgeschlossenQueue)
                .to(bestellungExchange)
                .with(ROUTING_KEY_PRUEFUNG_ABGESCHLOSSEN);
    }

    @Bean
    public Binding bindBestellungQueueToExchange(Queue bestellungEingehendQueue, TopicExchange bestellungExchange) {
        return BindingBuilder.bind(bestellungEingehendQueue)
                .to(bestellungExchange)
                .with(ROUTING_KEY_BESTELLUNG_EINGEHEND);
    }

    @Bean
    public Binding bindZahlungQueueToExchange(Queue zahlungAbgeschlossenQueue, TopicExchange bestellungExchange) {
        return BindingBuilder.bind(zahlungAbgeschlossenQueue)
                .to(bestellungExchange)
                .with(ROUTING_KEY_ZAHLUNG_ABGESCHLOSSEN);
    }

    @Bean
    public Binding bindVersandQueueToExchange(Queue versandAbgeschlossenQueue, TopicExchange bestellungExchange) {
        return BindingBuilder.bind(versandAbgeschlossenQueue)
                .to(bestellungExchange)
                .with(ROUTING_KEY_VERSAND_ABGESCHLOSSEN);
    }
}