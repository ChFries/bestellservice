package prv.fries.bestellservice.bestellung.controller;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Bestellung;
import org.openapitools.model.StatusUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import prv.fries.bestellservice.generated.BestellungApi;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class BestellungenController implements BestellungApi {
    @Override
    public ResponseEntity<Void> deleteBestellungById(UUID bestellId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Bestellung>> getBestellung() {
        log.info("getBestellung");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Bestellung> getBestellungById(UUID bestellId) {
        return null;
    }

    @Override
    public ResponseEntity<Bestellung> postBestellung(Bestellung bestellung) {
        return null;
    }

    @Override
    public ResponseEntity<Bestellung> updateZahlungsstatusById(UUID bestellId, StatusUpdate statusUpdate) {
        return null;
    }
}
