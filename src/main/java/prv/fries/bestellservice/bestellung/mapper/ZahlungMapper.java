package prv.fries.bestellservice.bestellung.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;
import prv.fries.bestellservice.bestellung.entity.Bestellung;
import prv.fries.bestellservice.generated.client.payment.ZahlungDto;

@Mapper(componentModel = "spring")
public interface ZahlungMapper {

    @Mapping(target = "bestellungId", source="id")
    @Mapping(target = "zahlungsart", source="zahlungsreferenz", qualifiedByName = "zahlungsartMapper")
    @Mapping(target = "betrag", source="gesamtbetrag")
    @Mapping(target = "status", ignore = true)
    ZahlungDto fromEntity(Bestellung bestellung);


    @Named("zahlungsartMapper")
    default ZahlungDto.ZahlungsartEnum fromZahlungsreferenz(String zahlungsreferenz){
        if(zahlungsreferenz==null || zahlungsreferenz.isEmpty()) {
            return ZahlungDto.ZahlungsartEnum.PAYPAL;
        }
        return mapZahlungsreferenzToZahlungsartEnum(zahlungsreferenz);
    }

    @ValueMapping(target ="KREDITKARTE", source= "KREDITKARTE")
    @ValueMapping(target ="PAYPAL", source= "PAYPAL")
    @ValueMapping(target ="VORKASSE", source= "VORKASSE")
    @ValueMapping(target ="RECHNUNG", source= "RECHNUNG")
    ZahlungDto.ZahlungsartEnum mapZahlungsreferenzToZahlungsartEnum(String zahlungsreferenz);

}
