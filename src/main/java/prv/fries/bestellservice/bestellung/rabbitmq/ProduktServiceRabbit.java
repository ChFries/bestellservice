package prv.fries.bestellservice.bestellung.rabbitmq;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prv.fries.bestellservice.bestellung.mapper.ProduktMapper;
import prv.fries.bestellservice.bestellung.service.ProduktService;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.client.produkt.ProduktVerfuegbarkeitDto;

@Service
@RequiredArgsConstructor
public class ProduktServiceRabbit implements ProduktService {

    private final ProduktMapper produktMapper;
    private final BestellungPublisher bestellungPublisher;

    @Override
    public void pruefeVerfuerbarkeit(BestellungDto bestellungDto) {
        var positionen = bestellungDto.getBestellPositionen().stream().map(produktMapper::fromBestellposition).toList();
        var produktVerfuegbarkeitsDto = new ProduktVerfuegbarkeitDto();
        produktVerfuegbarkeitsDto.setBestellId(bestellungDto.getId());
        produktVerfuegbarkeitsDto.setPositionen(positionen);
        bestellungPublisher.publishBestellungAngelegt(produktVerfuegbarkeitsDto);
    }
}
