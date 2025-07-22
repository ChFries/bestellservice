package prv.fries.bestellservice.bestellung.entity;

import jakarta.persistence.*;
import lombok.Data;
import prv.fries.bestellservice.bestellung.exceptions.IllegalStateTransitionException;
import prv.fries.bestellservice.bestellung.model.Status;
import prv.fries.bestellservice.bestellung.model.StatusConverter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Bestellung {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID kundeId;

    @OneToMany(mappedBy = "bestellung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BestellPosition> bestellPositionen = new ArrayList<>();

    @Column(nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status;

    @Column(nullable = false)
    private OffsetDateTime erstelltAm;

    @Column(nullable = false)
    private OffsetDateTime lastUpdateAm;

    @Column
    private Double gesamtbetrag;

    private String zahlungsreferenz;

    private String versandreferenz;

    public void setStatus(Status status) {
        if(this.status == null){
            this.status = status;
        }
        else if(canTransition(this.status, status)){
            this.status = status;
        }else {
            throw new IllegalStateTransitionException("Illegaler Statuswechsel von " + this.status +" nach " + status + " entdeckt");
        }
    }

    public static boolean canTransition(Status currentStatus, Status newStatus) {
        if (currentStatus == newStatus) {
            return true; // Kein Wechsel, aber gültig
        }

        // Von jedem Status außer ABGESCHLOSSEN darf man nach STORNIERT wechseln
        if (newStatus == Status.STORNIERT && currentStatus != Status.ABGESCHLOSSEN) {
            return true;
        }

        return switch (currentStatus) {
            case OFFEN -> newStatus == Status.GEPRUEFT;
            case GEPRUEFT -> newStatus == Status.BEZAHLT;
            case BEZAHLT -> newStatus == Status.VERSENDET;
            case VERSENDET -> newStatus == Status.ABGESCHLOSSEN;
            case ABGESCHLOSSEN -> false; // Kein Wechsel mehr erlaubt
            case STORNIERT -> false;     // Stornierte Bestellungen sind final
        };
    }
}
