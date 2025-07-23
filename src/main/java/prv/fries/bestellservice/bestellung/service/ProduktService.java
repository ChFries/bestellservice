package prv.fries.bestellservice.bestellung.service;

import prv.fries.bestellservice.generated.BestellungDto;

public interface ProduktService {

    BestellungDto pruefeVerfuerbarkeit(BestellungDto bestellungDto);
}
