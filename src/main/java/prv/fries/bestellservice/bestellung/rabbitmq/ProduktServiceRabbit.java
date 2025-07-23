package prv.fries.bestellservice.bestellung.rabbitmq;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prv.fries.bestellservice.bestellung.service.ProduktService;
import prv.fries.bestellservice.generated.BestellungDto;

@Service
@RequiredArgsConstructor
public class ProduktServiceRabbit implements ProduktService {

    private final BestellungPublisher bestellungPublisher;

    @Override
    public BestellungDto pruefeVerfuerbarkeit(BestellungDto bestellungDto) {
        bestellungPublisher.publishBestellungAngelegt(bestellungDto);
        return null;
    }
}
