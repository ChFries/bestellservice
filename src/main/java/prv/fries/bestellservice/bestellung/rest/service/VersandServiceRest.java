package prv.fries.bestellservice.bestellung.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.api.VersandApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import prv.fries.bestellservice.bestellung.service.VersandService;
import prv.fries.bestellservice.generated.BestellungDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class VersandServiceRest implements VersandService {

    private final VersandApi versandApi;

    @Override
    public BestellungDto erstelleVersandauftragRequest(BestellungDto bestellung) {
        try {
            return versandApi.versandPost(bestellung);
        }catch(RestClientException ex){
            throw new IllegalStateException("Fehler beim Abruf des Versandservices");
        }
    }

}
