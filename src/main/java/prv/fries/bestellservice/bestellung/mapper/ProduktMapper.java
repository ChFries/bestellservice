package prv.fries.bestellservice.bestellung.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import prv.fries.bestellservice.generated.BestellPositionDto;
import prv.fries.bestellservice.generated.client.produkt.AbgefragtePositionen;

@Mapper(componentModel = "spring")
public interface ProduktMapper {

    @Mapping(target = "produktId", source ="produktId")
    @Mapping(target = "menge", source ="menge")
    @Mapping(target = "preis", source ="einzelpreis")
    AbgefragtePositionen fromBestellposition(BestellPositionDto bestellPositionDto);

}
