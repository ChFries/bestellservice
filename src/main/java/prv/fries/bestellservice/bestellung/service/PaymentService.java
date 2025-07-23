package prv.fries.bestellservice.bestellung.service;


import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.generated.BestellungDto;

public interface PaymentService {
    BestellungDto erstelleZahlung(Bestellung bestellung);
}
