package prv.fries.bestellservice.bestellung.service;


import prv.fries.bestellservice.bestellung.entity.Bestellung;

public interface PaymentService {
    void erstelleZahlung(Bestellung bestellung);
}
