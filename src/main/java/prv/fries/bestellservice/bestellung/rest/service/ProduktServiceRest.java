package prv.fries.bestellservice.bestellung.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.api.ProduktApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import prv.fries.bestellservice.bestellung.mapper.ProduktMapper;
import prv.fries.bestellservice.bestellung.service.ProduktService;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.client.produkt.ProduktVerfuegbarkeitDto;
import prv.fries.bestellservice.generated.client.produkt.UeberprueftePositionen;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProduktServiceRest implements ProduktService {

    private final ProduktMapper produktMapper;

    private final ProduktApi produktClient;

    @Override
    public void pruefeVerfuerbarkeit(BestellungDto bestellungDto) {
        var positionen = bestellungDto.getBestellPositionen().stream().map(produktMapper::fromBestellposition).toList();
        var produktVerfuegbarkeitsDto = new ProduktVerfuegbarkeitDto();
        produktVerfuegbarkeitsDto.setBestellId(bestellungDto.getId());
        produktVerfuegbarkeitsDto.setPositionen(positionen);
        try {
            var ueberpruefteProdukte = produktClient.pruefeVerfuegbarkeit(produktVerfuegbarkeitsDto);
            if (!ueberpruefteProdukte.getPositionen().stream().allMatch(UeberprueftePositionen::getVerfuegbar)) {
                throw new IllegalStateException("Produkte nicht verfuegbar");
            }
            log.info("Pruefung abgeschlossen fuer Bestellung {}", ueberpruefteProdukte.getBestellId());
        } catch (RestClientException e) {
            throw new IllegalStateException("Fehler beim Abruf des Produktservices");
        }
    }
}
