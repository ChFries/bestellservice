package prv.fries.bestellservice.bestellung.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.api.VersandApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.service.VersandService;
import prv.fries.bestellservice.generated.client.versand.VersandauftragDto;
import prv.fries.bestellservice.generated.client.versand.VersandauftragRequest;


@Service
@RequiredArgsConstructor
@Slf4j
public class VersandServiceRest implements VersandService {

    private final VersandApi versandApiClient;

    @Override
    public VersandauftragDto erstelleVersandauftragRequest(Bestellung bestellung) {

        VersandauftragRequest versandAuftragRequest = new VersandauftragRequest();
        versandAuftragRequest.setBestellungId(bestellung.getId());
        versandAuftragRequest.setKundenId(bestellung.getKundeId());

        try {
            return versandApiClient.versandPost(versandAuftragRequest);

        }catch(RestClientException ex){
            throw new IllegalStateException("Fehler beim Abruf des Versandservices");
        }
    }

}
