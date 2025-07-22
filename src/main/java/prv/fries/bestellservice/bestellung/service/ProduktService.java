package prv.fries.bestellservice.bestellung.service;

import prv.fries.bestellservice.generated.BestellungDto;

public interface ProduktService {

    void pruefeVerfuerbarkeit(BestellungDto bestellungDto);
}
