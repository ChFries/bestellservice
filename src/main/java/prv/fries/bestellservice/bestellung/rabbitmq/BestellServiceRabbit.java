package prv.fries.bestellservice.bestellung.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prv.fries.bestellservice.bestellung.entity.BestellPosition;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.mapper.BestellungMapper;
import prv.fries.bestellservice.bestellung.model.Status;
import prv.fries.bestellservice.bestellung.repository.BestellungRepository;
import prv.fries.bestellservice.bestellung.service.BestellService;
import prv.fries.bestellservice.bestellung.service.ProduktService;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.client.payment.ZahlungDto;
import prv.fries.bestellservice.generated.client.versand.VersandauftragDto;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BestellServiceRabbit implements BestellService {

    private final BestellungRepository bestellungRepository;
    private final BestellungMapper bestellungMapper;
    private final ProduktService produktService;

    @Override
    public Bestellung erstelleBestellung(BestellungDto bestellungDto) {
        Bestellung bestellung = bestellungMapper.toEntity(bestellungDto);
        bestellung.setStatus(Status.OFFEN);
        OffsetDateTime now = OffsetDateTime.now();
        bestellung.setErstelltAm(now);
        bestellung.setLastUpdateAm(now);
        bestellung.setGesamtbetrag(calculateSums(bestellung));
        for (BestellPosition pos : bestellung.getBestellPositionen()) {
            pos.setBestellung(bestellung);
        }
        bestellungRepository.save(bestellung);
        produktService.pruefeVerfuerbarkeit(bestellungDto);
        return bestellung;
    }

    @Override
    public void updateZahlungsStatus(ZahlungDto zahlungErhalten) {
        //todo
    }

    @Override
    public void updateVersandStatus(VersandauftragDto versandauftragAbgeschlossen) {
        //todo
    }

    private Double calculateSums(Bestellung bestellung) {
        Double gesamtbetrag = bestellung.getBestellPositionen()
                .stream()
                .map(position -> position.getEinzelpreis() * position.getMenge())
                .reduce(0.0, Double::sum);
        gesamtbetrag = Double.valueOf(String.format("%.2f",gesamtbetrag));
        log.info("Gesamtbetrag für neue Bestellung ist {}", gesamtbetrag);
        return gesamtbetrag;
    }
}
