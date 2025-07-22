package prv.fries.bestellservice.bestellung.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.repository.BestellungRepository;
import prv.fries.bestellservice.bestellung.service.BestellService;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.client.payment.ZahlungDto;
import prv.fries.bestellservice.generated.client.versand.VersandauftragDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class BestellServiceRabbit implements BestellService {

    private final BestellungRepository bestellungRepository;


    @Override
    public Bestellung erstelleBestellung(BestellungDto bestellungDto) {
        return null;
    }

    @Override
    public void updateZahlungsStatus(ZahlungDto zahlungErhalten) {
        //todo
    }

    @Override
    public void updateVersandStatus(VersandauftragDto versandauftragAbgeschlossen) {
        //todo
    }
}
