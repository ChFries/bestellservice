package prv.fries.bestellservice.bestellung.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import prv.fries.bestellservice.bestellung.service.BestellService;
import prv.fries.bestellservice.generated.BestellungDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class BestellungEventListener {

    private final BestellService bestellService;

    @RabbitListener(queues = RabbitMQGeneralConfig.QUEUE_PRUEFUNG_ABGESCHLOSSEN)
    public void handlePruefungAbgeshchlossen(BestellungDto bestellungUeberprueft) {
        bestellService.updatePruefungAbgeschlossen(bestellungUeberprueft);
    }

    @RabbitListener(queues = RabbitMQGeneralConfig.QUEUE_BESTELLUNG_EINGEHEND)
    public void handleBestellungEingegangen(BestellungDto bestellungErhalten) {
        bestellService.erstelleBestellung(bestellungErhalten);
    }

    @RabbitListener(queues = RabbitMQGeneralConfig.QUEUE_ZAHLUNG_ABGESCHLOSSEN)
    public void handleZahlungAbgeschlossen(BestellungDto zahlungErhalten) {
        bestellService.updateZahlungsStatus1(zahlungErhalten);
    }

    @RabbitListener(queues = RabbitMQGeneralConfig.QUEUE_VERSAND_ABGESCHLOSSEN)
    public void handleVersandAbgeschlossen(BestellungDto versandauftragAbgeschlossen) {
        log.info("Versand abgeschlossen empfangen: {}", versandauftragAbgeschlossen.toString());
        bestellService.updateVersandStatus1(versandauftragAbgeschlossen);
    }
}
