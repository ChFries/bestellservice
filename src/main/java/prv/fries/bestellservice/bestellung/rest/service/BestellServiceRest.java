package prv.fries.bestellservice.bestellung.rest.service;

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
import prv.fries.bestellservice.bestellung.service.PaymentService;
import prv.fries.bestellservice.bestellung.service.ProduktService;
import prv.fries.bestellservice.bestellung.service.VersandService;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.client.payment.ZahlungDto;
import prv.fries.bestellservice.generated.client.versand.VersandauftragDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BestellServiceRest implements BestellService {

    private final BestellungRepository bestellungRepository;


    private final BestellungMapper bestellungMapper;

    private final ProduktService produktService;

    private final PaymentService paymentServiceRest;

    private final VersandService versandService;

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
        bestellung = bestellungRepository.save(bestellung);
        log.info("Bestellung angelegt");
        bestellungDto.setId(bestellung.getId());
        try {
            produktService.pruefeVerfuerbarkeit(bestellungDto);
            updatePruefung(bestellung);
            createZahlung(bestellung);
            erstelleVersandauftrag(bestellung);
        }catch (IllegalStateException e) {
            log.error("Fehler beim Ausführen der Bestellung {}", e.getMessage());
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellung.setStatus(Status.STORNIERT);
            bestellungRepository.save(bestellung);
        }
        return bestellungRepository.findById(bestellung.getId()).orElseThrow(() -> new IllegalStateException("Bestellung nicht gefunden"));
    }

    private void updatePruefung(Bestellung bestellung) {
        try {
            bestellung.setStatus(Status.GEPRUEFT);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellungRepository.save(bestellung);
        }catch(IllegalStateTransitionException e){
            log.error("{} für Bestellung {}", e.getMessage(), bestellung.getId() );
        }
    }

    public void createZahlung(Bestellung bestellung) {
        var response = paymentServiceRest.erstelleZahlung(bestellung);
        updateZahlungsStatus(response);
    }

    @Override
    public void updateZahlungsStatus(ZahlungDto response) {
        if (response.getStatus() == ZahlungDto.StatusEnum.ERFOLGREICH) {
            Bestellung bestellung = bestellungRepository.findById(response.getBestellungId()).orElseThrow(() -> new IllegalStateException("BestellId nicht im ZahlungDto gefunden aber sollte da sein"));
            log.info("Betrag {} wurde für Rechnung {} beglichen", response.getBetrag(), response.getBestellungId());
            bestellung.setStatus(Status.BEZAHLT);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellungRepository.save(bestellung);
        } else {
            throw new IllegalStateException("Zahlung nicht erfolgreich");
        }
    }

    @Override
    public void updateZahlungsStatus1(BestellungDto zahlungErhalten) {
        //Todo Refactor
    }

    private void erstelleVersandauftrag(Bestellung bestellung) {
        var response = versandService.erstelleVersandauftragRequest(bestellung);
        updateVersandStatus(response);
    }

    @Override
    public void updateVersandStatus(VersandauftragDto versandauftragAbgeschlossen){
        if (versandauftragAbgeschlossen.getStatus() == VersandauftragDto.StatusEnum.VERSENDET) {
            Bestellung bestellung = bestellungRepository.findById(versandauftragAbgeschlossen.getBestellungId()).orElseThrow(() -> new IllegalStateException("BestellId nicht im VersandAuftragDto gefunden aber sollte da sein"));
            bestellung.setStatus(Status.VERSENDET);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            log.info("Sendung mit Id {} wurde für Bestellung {} versendet", versandauftragAbgeschlossen.getId(), versandauftragAbgeschlossen.getBestellungId());
            bestellungRepository.save(bestellung);
        }else {
            throw new IllegalStateException("Versandauftrag nicht erfolgreich");
        }
    }

    @Override
    public void updateVersandStatus1(BestellungDto versandauftragAbgeschlossen) {
        // Todo refactor
    }

    @Override
    public void updatePruefungAbgeschlossen(BestellungDto produktVerfuegbarAbgeschlossen) {
        //unnecessary for produktservice
    }


    public List<Bestellung> getAllBestellungen() {
        return bestellungRepository.findAll();
    }

    public void deleteBestellungById(UUID id) {
        bestellungRepository.deleteById(id);
    }

    public Bestellung getBestellungById(UUID bestellId) {
        return bestellungRepository.findById(bestellId).orElseThrow(() -> new RuntimeException("not found"));
    }

    public Bestellung updateBestellung(UUID bestellId, Status statusUpdate) {
        Bestellung bestellung = bestellungRepository.findById(bestellId).orElseThrow(() -> new RuntimeException("not found"));
        bestellung.setStatus(statusUpdate);
        return bestellungRepository.save(bestellung);
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
