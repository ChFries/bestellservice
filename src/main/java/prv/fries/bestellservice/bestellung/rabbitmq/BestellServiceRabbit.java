package prv.fries.bestellservice.bestellung.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prv.fries.bestellservice.bestellung.entity.BestellPosition;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.exceptions.IllegalStateTransitionException;
import prv.fries.bestellservice.bestellung.mapper.BestellungMapper;
import prv.fries.bestellservice.bestellung.model.Status;
import prv.fries.bestellservice.bestellung.repository.BestellungRepository;
import prv.fries.bestellservice.bestellung.service.BestellService;
import prv.fries.bestellservice.bestellung.service.ProduktService;
import prv.fries.bestellservice.generated.BestellPositionDto;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.StatusDto;
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
        bestellungDto.setId(bestellung.getId());
        produktService.pruefeVerfuerbarkeit(bestellungDto);
        return bestellung;
    }

    @Override
    public void updateZahlungsStatus(BestellungDto zahlungErhalten) {
        if (zahlungErhalten.getStatus() == StatusDto.BEZAHLT) {
            Bestellung bestellung = bestellungRepository.findById(zahlungErhalten.getId()).orElseThrow(() -> new IllegalStateException("BestellId nicht im ZahlungDto gefunden aber sollte da sein"));
            log.info("Rechnung {} beglichen", zahlungErhalten.getId());
            bestellung.setStatus(Status.BEZAHLT);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellungRepository.save(bestellung);
        } else {
            throw new IllegalStateException("Zahlung nicht erfolgreich");
        }
    }


    @Override
    public void updateVersandStatus(VersandauftragDto versandauftragAbgeschlossen) {
        //todo
    }

    @Override
    public void updateVersandStatus1(BestellungDto versandauftragAbgeschlossen) {
        if (versandauftragAbgeschlossen.getStatus() == StatusDto.VERSENDET) {
            Bestellung bestellung = bestellungRepository.findById(versandauftragAbgeschlossen.getId()).orElseThrow(() -> new IllegalStateException("BestellId nicht im VersandAuftragDto gefunden aber sollte da sein"));
            bestellung.setStatus(Status.VERSENDET);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            log.info("Sendung mit Id {} wurde für Bestellung {} versendet", versandauftragAbgeschlossen.getId(), versandauftragAbgeschlossen.getId());
            bestellungRepository.save(bestellung);
        }else {
            throw new IllegalStateException("Versandauftrag nicht erfolgreich");
        }
    }

    @Override
    public void updatePruefungAbgeschlossen(BestellungDto ueberpruefteBestellung) {
        if (!ueberpruefteBestellung.getBestellPositionen().stream().allMatch(BestellPositionDto::getVerfuegbar)) {
            Bestellung bestellung = bestellungRepository.findById(ueberpruefteBestellung.getId()).orElseThrow(() -> new IllegalStateException("Bestellung nicht gefunden"));
            bestellung.setStatus(Status.STORNIERT);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellungRepository.save(bestellung);
            throw new IllegalStateException("Produkte nicht verfuegbar");
        }
        log.info("Pruefung abgeschlossen fuer Bestellung {}", ueberpruefteBestellung.getId());
        Bestellung bestellung = bestellungRepository.findById(ueberpruefteBestellung.getId()).orElseThrow(() -> new IllegalStateException("Bestellung nicht gefunden"));
        try {
            bestellung.setStatus(Status.GEPRUEFT);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellungRepository.save(bestellung);
        }catch(IllegalStateTransitionException e){
            log.error("{} für Bestellung {}", e.getMessage(), bestellung.getId() );
        }
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
