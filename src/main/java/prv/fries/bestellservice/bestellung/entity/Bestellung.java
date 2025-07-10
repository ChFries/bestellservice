package prv.fries.bestellservice.bestellung.entity;

import jakarta.persistence.*;
import lombok.Data;
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

    private String zahlungsreferenz;

    private String versandreferenz;

}
