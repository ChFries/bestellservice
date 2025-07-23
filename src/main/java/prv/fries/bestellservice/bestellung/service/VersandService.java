package prv.fries.bestellservice.bestellung.service;

import prv.fries.bestellservice.generated.BestellungDto;

public interface VersandService {

    BestellungDto erstelleVersandauftragRequest(BestellungDto bestellung);

}
