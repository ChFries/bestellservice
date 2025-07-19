package prv.fries.bestellservice.bestellung.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.api.ProduktApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import prv.fries.bestellservice.bestellung.mapper.ProduktMapper;
import prv.fries.bestellservice.generated.BestellungDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProduktService {

    private final ProduktMapper produktMapper;

    private final ProduktApi produktClient;

    public void pruefeVerfuerbarkeit(BestellungDto bestellungDto) {
        var produkte = bestellungDto.getBestellPositionen().stream().map(produktMapper::fromBestellposition).toList();
        try {
            produktClient.pruefeVerfuegbarkeit(produkte);
        } catch (RestClientException e) {
            throw new IllegalStateException("Fehler beim Abruf des Produktservices");
        }
    }
}
