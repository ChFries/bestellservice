package prv.fries.bestellservice.bestellung.service;

import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.generated.client.versand.VersandauftragDto;

public interface VersandService {

    VersandauftragDto erstelleVersandauftragRequest(Bestellung bestellung);

}
