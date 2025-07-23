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
import prv.fries.bestellservice.generated.BestellPositionDto;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.StatusDto;
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

    private static final String BESTELLUNG_NICHT_GEFUNDEN = "Bestellung nicht gefunden";

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
            ueberpruefeProdukteVerfuegbar(bestellungDto);
            erstelleZahlung(bestellung);
            erstelleVersandauftrag(bestellung);
        }catch (IllegalStateException e) {
            log.error("Fehler beim Ausführen der Bestellung {}", e.getMessage());
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellung.setStatus(Status.STORNIERT);
            bestellungRepository.save(bestellung);
        }
        return bestellungRepository.findById(bestellung.getId()).orElseThrow(() -> new IllegalStateException(BESTELLUNG_NICHT_GEFUNDEN));
    }

    private void ueberpruefeProdukteVerfuegbar(BestellungDto bestellungDto) {
        var ueberpruefteBestellung = produktService.pruefeVerfuerbarkeit(bestellungDto);
        updatePruefungAbgeschlossen(ueberpruefteBestellung);
    }

    public void erstelleZahlung(Bestellung bestellung) {
        var bezahlteBestellung = paymentServiceRest.erstelleZahlung(bestellung);
        updateZahlungsStatus(bezahlteBestellung);
    }

    private void erstelleVersandauftrag(Bestellung bestellung) {
        var versendeteBestellung = versandService.erstelleVersandauftragRequest(bestellung);
        updateVersandStatus(versendeteBestellung);
    }

    @Override
    public void updateZahlungsStatus(BestellungDto bezahlteBestellung) {
        if (bezahlteBestellung.getStatus() == StatusDto.BEZAHLT) {
            Bestellung bestellung = bestellungRepository.findById(bezahlteBestellung.getId()).orElseThrow(() -> new IllegalStateException("BestellId nicht im ZahlungDto gefunden aber sollte da sein"));
            log.info("Betrag {} wurde für Rechnung {} beglichen", bestellung.getGesamtbetrag(), bestellung.getId());
            bestellung.setStatus(Status.BEZAHLT);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellungRepository.save(bestellung);
        } else {
            throw new IllegalStateException("Zahlung nicht erfolgreich");
        }
    }

    @Override
    public void updateVersandStatus(VersandauftragDto versendeteBestellung){
        if (versendeteBestellung.getStatus() == VersandauftragDto.StatusEnum.VERSENDET) {
            Bestellung bestellung = bestellungRepository.findById(versendeteBestellung.getBestellungId()).orElseThrow(() -> new IllegalStateException("BestellId nicht im VersandAuftragDto gefunden aber sollte da sein"));
            bestellung.setStatus(Status.VERSENDET);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            log.info("Sendung mit Id {} wurde für Bestellung {} versendet", versendeteBestellung.getId(), versendeteBestellung.getBestellungId());
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
    public void updatePruefungAbgeschlossen(BestellungDto ueberpruefteBestellung) {
        if (!ueberpruefteBestellung.getBestellPositionen().stream().allMatch(BestellPositionDto::getVerfuegbar)) {
            Bestellung bestellung = bestellungRepository.findById(ueberpruefteBestellung.getId()).orElseThrow(() -> new IllegalStateException(BESTELLUNG_NICHT_GEFUNDEN));
            bestellung.setStatus(Status.STORNIERT);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellungRepository.save(bestellung);
            throw new IllegalStateException("Produkte nicht verfuegbar");
        }
        log.info("Pruefung abgeschlossen fuer Bestellung {}", ueberpruefteBestellung.getId());
        Bestellung bestellung = bestellungRepository.findById(ueberpruefteBestellung.getId()).orElseThrow(() -> new IllegalStateException(BESTELLUNG_NICHT_GEFUNDEN));
        try {
            bestellung.setStatus(Status.GEPRUEFT);
            bestellung.setLastUpdateAm(OffsetDateTime.now());
            bestellungRepository.save(bestellung);
        }catch(IllegalStateTransitionException e){
            log.error("{} für Bestellung {}", e.getMessage(), bestellung.getId() );
        }
    }


    public List<Bestellung> getAllBestellungen() {
        return bestellungRepository.findAll();
    }

    public void deleteBestellungById(UUID id) {
        bestellungRepository.deleteById(id);
    }

    public Bestellung getBestellungById(UUID bestellId) {
        return bestellungRepository.findById(bestellId).orElseThrow(() -> new IllegalStateException(BESTELLUNG_NICHT_GEFUNDEN));
    }

    public Bestellung updateBestellung(UUID bestellId, Status statusUpdate) {
        Bestellung bestellung = bestellungRepository.findById(bestellId).orElseThrow(() -> new IllegalStateException(BESTELLUNG_NICHT_GEFUNDEN));
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
