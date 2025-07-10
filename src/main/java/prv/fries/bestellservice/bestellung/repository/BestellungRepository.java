package prv.fries.bestellservice.bestellung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prv.fries.bestellservice.bestellung.entity.Bestellung;

import java.util.UUID;

@Repository
public interface BestellungRepository extends JpaRepository<Bestellung, UUID> {

}
