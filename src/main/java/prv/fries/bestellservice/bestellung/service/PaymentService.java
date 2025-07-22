package prv.fries.bestellservice.bestellung.service;


import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.generated.client.payment.ZahlungDto;

public interface PaymentService {
    ZahlungDto erstelleZahlung(Bestellung bestellung);
}
