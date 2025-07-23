package prv.fries.bestellservice.bestellung.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusConverter implements AttributeConverter<Status, String> {


    @Override
    public String convertToDatabaseColumn(Status attribute) {
        return attribute.getValue();
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        return Status.valueOf(dbData.toUpperCase());
    }

}
