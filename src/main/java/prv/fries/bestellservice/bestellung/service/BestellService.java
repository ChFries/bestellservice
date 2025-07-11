package prv.fries.bestellservice.bestellung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prv.fries.bestellservice.bestellung.entity.BestellPosition;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.mapper.BestellungMapper;
import prv.fries.bestellservice.bestellung.model.Status;
import prv.fries.bestellservice.bestellung.repository.BestellungRepository;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.StatusUpdateDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BestellService {

    private final BestellungRepository bestellungRepository;

    private final BestellungMapper bestellungMapper;

    public BestellungDto createBestellung(BestellungDto bestellungDto) {
        Bestellung bestellung = bestellungMapper.toEntity(bestellungDto);
        bestellung.setStatus(Status.OFFEN);
        bestellung.setErstelltAm(OffsetDateTime.now());
        for (BestellPosition pos : bestellung.getBestellPositionen()) {
            pos.setBestellung(bestellung);
        }
        bestellung = bestellungRepository.save(bestellung);
        return bestellungMapper.toDTO(bestellung);
    }

    public List<BestellungDto> getAllBestellungen() {
        return bestellungRepository.findAll().stream().map(bestellungMapper::toDTO).toList();
    }

    public void deleteBestellungById(UUID id) {
        bestellungRepository.deleteById(id);
    }

    public Bestellung getBestellungById(UUID bestellId) {
        return bestellungRepository.findById(bestellId).orElseThrow(() -> new RuntimeException("not found"));
    }

    public BestellungDto updateBestellung(UUID bestellId, StatusUpdateDto statusUpdate) {
        Bestellung bestellung = bestellungRepository.findById(bestellId).orElseThrow(() -> new RuntimeException("not found"));
        bestellung.setStatus(bestellungMapper.toStatus(statusUpdate.getStatus()));
        return bestellungMapper.toDTO(bestellungRepository.save(bestellung));
    }
}
