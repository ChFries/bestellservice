package prv.fries.bestellservice.bestellung.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.api.ZahlungApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.mapper.ZahlungMapper;
import prv.fries.bestellservice.bestellung.model.Status;
import prv.fries.bestellservice.bestellung.service.PaymentService;
import prv.fries.bestellservice.generated.client.payment.ZahlungDto;

import java.time.OffsetDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceRest implements PaymentService {

    private final ZahlungApi zahlungApiClient;

    private final ZahlungMapper zahlungMapper;

    @Override
    public void erstelleZahlung(Bestellung bestellung) {
        ZahlungDto dto = zahlungMapper.fromEntity(bestellung);
        try {
            ZahlungDto response = zahlungApiClient.postZahlung(dto);
            if (response.getStatus() == ZahlungDto.StatusEnum.ERFOLGREICH) {
                log.info("Betrag {} wurde für Rechnung {} beglichen", response.getBetrag(), response.getBestellungId());
                bestellung.setStatus(Status.BEZAHLT);
                bestellung.setLastUpdateAm(OffsetDateTime.now());
            }
        } catch (RestClientException e) {
            throw new IllegalStateException("Fehler beim Abruf des Paymentservices");
        }
    }


}
