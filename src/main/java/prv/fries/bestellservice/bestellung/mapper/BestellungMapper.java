package prv.fries.bestellservice.bestellung.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.bestellung.model.Status;
import prv.fries.bestellservice.generated.BestellungDto;
import prv.fries.bestellservice.generated.StatusDto;

@Mapper(componentModel = "spring")
public interface BestellungMapper {

    BestellungDto toDTO(Bestellung bestellung);
    Bestellung toEntity(BestellungDto bestellung);

    @ValueMapping(source="OFFEN", target="OFFEN")
    @ValueMapping(source="BEZAHLT", target="BEZAHLT")
    @ValueMapping(source="VERSENDET", target="VERSENDET")
    @ValueMapping(source="ABGESCHLOSSEN", target="ABGESCHLOSSEN")
    @ValueMapping(source="STORNIERT", target="STORNIERT")
    Status toStatus(StatusDto statusDto);

    @ValueMapping(source="OFFEN", target="OFFEN")
    @ValueMapping(source="BEZAHLT", target="BEZAHLT")
    @ValueMapping(source="VERSENDET", target="VERSENDET")
    @ValueMapping(source="ABGESCHLOSSEN", target="ABGESCHLOSSEN")
    @ValueMapping(source="STORNIERT", target="STORNIERT")
    StatusDto toStatus(Status status);


}
