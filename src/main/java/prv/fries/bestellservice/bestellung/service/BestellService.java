package prv.fries.bestellservice.bestellung.service;

import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.client.payment.ZahlungDto;
import prv.fries.bestellservice.generated.client.produkt.ProduktVerfuegbarDto;
import prv.fries.bestellservice.generated.client.versand.VersandauftragDto;

public interface BestellService {

    Bestellung erstelleBestellung(BestellungDto bestellungDto);

    void updateZahlungsStatus(ZahlungDto zahlungErhalten);

    void updateVersandStatus(VersandauftragDto versandauftragAbgeschlossen);

    void updatePruefungAbgeschlossen(ProduktVerfuegbarDto produktVerfuegbarAbgeschlossen);
}
