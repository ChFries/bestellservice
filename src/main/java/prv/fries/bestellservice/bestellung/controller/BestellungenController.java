package prv.fries.bestellservice.bestellung.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.BestellungApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
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
    @Override
    public ResponseEntity<Void> deleteBestellungById(UUID bestellId) {
        return null;
    }

    @Override
    public ResponseEntity<List<BestellungDto>> getBestellung() {
        var bestellungen = bestellService.getAllBestellungen();
        return ResponseEntity.ok().body(bestellungen);
    }

    @Override
    public ResponseEntity<BestellungDto> getBestellungById(UUID bestellId) {
        return null;
    }

    @Override
    public ResponseEntity<BestellungDto> postBestellung(BestellungDto bestellung) {
        BestellungDto bestellungDto = bestellService.createBestellung(bestellung);
        return ResponseEntity.status(HttpStatus.CREATED).body(bestellungDto);
    }

    @Override
    public ResponseEntity<BestellungDto> updateZahlungsstatusById(UUID bestellId, StatusUpdateDto statusUpdate) {
        return null;
    }
}
