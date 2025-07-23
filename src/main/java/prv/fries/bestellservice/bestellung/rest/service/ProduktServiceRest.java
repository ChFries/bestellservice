package prv.fries.bestellservice.bestellung.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.api.ProduktApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import prv.fries.bestellservice.bestellung.service.ProduktService;
import prv.fries.bestellservice.generated.BestellungDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProduktServiceRest implements ProduktService {

    private final ProduktApi produktClient;

    @Override
    public BestellungDto pruefeVerfuerbarkeit(BestellungDto bestellungDto) {
        try {
            return produktClient.pruefeVerfuegbarkeit(bestellungDto);
        } catch (RestClientException e) {
            throw new IllegalStateException("Fehler beim Abruf des Produktservices");
        }
    }
}
