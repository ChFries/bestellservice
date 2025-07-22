package prv.fries.bestellservice.bestellung.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import prv.fries.bestellservice.bestellung.service.BestellService;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.client.payment.ZahlungDto;
import prv.fries.bestellservice.generated.client.produkt.ProduktVerfuegbarDto;
import prv.fries.bestellservice.generated.client.versand.VersandauftragDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class BestellungEventListener {

    private final BestellService bestellService;

    @RabbitListener(queues = RabbitMQGeneralConfig.QUEUE_PRUEFUNG_ABGESCHLOSSEN)
    public void handlePruefungAbgeshchlossen(ProduktVerfuegbarDto produktVerfuegbarDto) {
        bestellService.updatePruefungAbgeschlossen(produktVerfuegbarDto);
    }

    @RabbitListener(queues = RabbitMQGeneralConfig.QUEUE_BESTELLUNG_EINGEHEND)
    public void handleBestellungEingegangen(BestellungDto bestellungErhalten) {
        bestellService.erstelleBestellung(bestellungErhalten);
    }

    @RabbitListener(queues = RabbitMQGeneralConfig.QUEUE_ZAHLUNG_ABGESCHLOSSEN)
    public void handleZahlungAbgeschlossen(ZahlungDto zahlungErhalten) {
        // Verarbeite Zahlung abgeschlossen
        log.info("Zahlung abgeschlossen empfangen: {}", zahlungErhalten.toString());
        bestellService.updateZahlungsStatus(zahlungErhalten);
    }

    @RabbitListener(queues = RabbitMQGeneralConfig.QUEUE_VERSAND_ABGESCHLOSSEN)
    public void handleVersandAbgeschlossen(VersandauftragDto versandauftragAbgeschlossen) {
        // Verarbeite Versand abgeschlossen
        log.info("Versand abgeschlossen empfangen: {}", versandauftragAbgeschlossen.toString());
        bestellService.updateVersandStatus(versandauftragAbgeschlossen);
    }
}
