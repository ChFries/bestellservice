package prv.fries.bestellservice.bestellung.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.api.VersandApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.model.Status;
import prv.fries.bestellservice.generated.client.versand.VersandauftragDto;
import prv.fries.bestellservice.generated.client.versand.VersandauftragRequest;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class VersandService {

    private final VersandApi versandApiClient;

    public void erstelleZahlung(Bestellung bestellung) {

        VersandauftragRequest versandAuftragRequest = new VersandauftragRequest();
        versandAuftragRequest.setBestellungId(bestellung.getId());
        versandAuftragRequest.setKundenId(bestellung.getKundeId());

        try {
            VersandauftragDto response = versandApiClient.versandPost(versandAuftragRequest);
            if (response.getStatus() == VersandauftragDto.StatusEnum.VERSENDET) {
                bestellung.setStatus(Status.VERSENDET);
                bestellung.setLastUpdateAm(OffsetDateTime.now());
                log.info("Sendung mit Id {} wurde für Bestellung {} versendet", response.getId(), response.getBestellungId());
            }
        }catch(RestClientException ex){
            throw new IllegalStateException("Fehler beim Abruf des Versandservices");
        }
    }

}
