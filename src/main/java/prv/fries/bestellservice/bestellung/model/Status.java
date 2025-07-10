package prv.fries.bestellservice.bestellung.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    OFFEN("offen"),
    BEZAHLT("bezahlt"),
    VERSENDET("versendet"),
    ABGESCHLOSSEN("abgeschlossen"),
    STORNIERT("storniert");

    private final String value;
}