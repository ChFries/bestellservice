package prv.fries.bestellservice.bestellung.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prv.fries.bestellservice.bestellung.entity.BestellPosition;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.mapper.BestellungMapper;
import prv.fries.bestellservice.bestellung.model.Status;
import prv.fries.bestellservice.bestellung.repository.BestellungRepository;
import prv.fries.bestellservice.bestellung.rest.service.PaymentService;
import prv.fries.bestellservice.bestellung.rest.service.ProduktService;
import prv.fries.bestellservice.generated.BestellungDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BestellService {

    private final BestellungRepository bestellungRepository;


    private final BestellungMapper bestellungMapper;

    private final ProduktService produktService;

    private final PaymentService paymentService;

    public Bestellung createBestellung(BestellungDto bestellungDto) {
        produktService.pruefeVerfuerbarkeit(bestellungDto);
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
        log.info("Bestellung with verfuegbaren Produkten created");

        paymentService.erstelleZahlung(bestellung);

        return bestellungRepository.save(bestellung);
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
