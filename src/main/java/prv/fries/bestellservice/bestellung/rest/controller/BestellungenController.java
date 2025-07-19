package prv.fries.bestellservice.bestellung.rest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.BestellungApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.mapper.BestellungMapper;
import prv.fries.bestellservice.bestellung.service.BestellService;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.StatusUpdateDto;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class BestellungenController implements BestellungApi {

    private final BestellService bestellService;
    private final BestellungMapper bestellungMapper;
    @Override
    public ResponseEntity<Void> deleteBestellungById(UUID bestellId) {
        bestellService.deleteBestellungById(bestellId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<BestellungDto>> getBestellung() {
        var bestellungen = bestellService.getAllBestellungen().stream().map(bestellungMapper::toDTO).toList();
        return ResponseEntity.ok().body(bestellungen);
    }

    @Override
    public ResponseEntity<BestellungDto> getBestellungById(UUID bestellId) {
        var bestellung = bestellService.getBestellungById(bestellId);
        return ResponseEntity.ok().body(bestellungMapper.toDTO(bestellung));
    }

    @Override
    public ResponseEntity<BestellungDto> postBestellung(BestellungDto bestellungDto) {
        Bestellung bestellung = bestellService.createBestellung(bestellungMapper.toEntity(bestellungDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(bestellungMapper.toDTO(bestellung));
    }

    @Override
    public ResponseEntity<BestellungDto> updateZahlungsstatusById(UUID bestellId, StatusUpdateDto statusUpdate) {
        Bestellung bestellung = bestellService.updateBestellung(bestellId, bestellungMapper.toStatus(statusUpdate.getStatus()));
        return ResponseEntity.status(HttpStatus.OK).body(bestellungMapper.toDTO(bestellung));
    }
}
