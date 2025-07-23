package prv.fries.bestellservice.bestellung.rabbitmq;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prv.fries.bestellservice.generated.BestellungDto;

@Service
@RequiredArgsConstructor
public class ProduktServiceRabbit {

    private final BestellungPublisher bestellungPublisher;

    public void pruefeVerfuerbarkeit(BestellungDto bestellungDto) {
        bestellungPublisher.publishBestellungAngelegt(bestellungDto);
    }
}
