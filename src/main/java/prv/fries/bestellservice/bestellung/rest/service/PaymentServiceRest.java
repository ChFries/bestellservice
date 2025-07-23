package prv.fries.bestellservice.bestellung.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.api.ZahlungApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.mapper.BestellungMapper;
import prv.fries.bestellservice.bestellung.service.PaymentService;
import prv.fries.bestellservice.generated.BestellungDto;



@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceRest implements PaymentService {

    private final ZahlungApi zahlungApiClient;

    private final BestellungMapper bestellungMapper;
    @Override
    public BestellungDto erstelleZahlung(Bestellung bestellung) {
        BestellungDto dto = bestellungMapper.toDTO(bestellung);
        try {
            return zahlungApiClient.postZahlung(dto);
        } catch (RestClientException e) {
            throw new IllegalStateException("Fehler beim Abruf des Paymentservices");
        }
    }


}
