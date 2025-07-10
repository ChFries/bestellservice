package prv.fries.bestellservice.bestellung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prv.fries.bestellservice.bestellung.entity.BestellPosition;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.mapper.BestellungMapper;
import prv.fries.bestellservice.bestellung.model.Status;
import prv.fries.bestellservice.bestellung.repository.BestellungRepository;
import prv.fries.bestellservice.generated.BestellungDto;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class BestellService {

    private final BestellungRepository bestellungRepository;

    private final BestellungMapper bestellungMapper;

    public BestellungDto createBestellung(BestellungDto bestellungDto) {
        Bestellung bestellung = bestellungMapper.toEntity(bestellungDto);
        bestellung.setStatus(Status.OFFEN);
        bestellung.setErstelltAm(OffsetDateTime.now());
        for(BestellPosition pos : bestellung.getBestellPositionen()) {
            pos.setBestellung(bestellung);
        }
        bestellung = bestellungRepository.save(bestellung);
        return bestellungMapper.toDTO(bestellung);
    }

}
