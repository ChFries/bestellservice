package prv.fries.bestellservice.bestellung.service;

import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.generated.BestellungDto;

public interface BestellService {

    Bestellung erstelleBestellung(BestellungDto bestellungDto);

    void updateZahlungsStatus(BestellungDto zahlungErhalten);

    void updateVersandStatus(BestellungDto versandauftragAbgeschlossen);

    void updatePruefungAbgeschlossen(BestellungDto bestellungUeberprueft);
}
