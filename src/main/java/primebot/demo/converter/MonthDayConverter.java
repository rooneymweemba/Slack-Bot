package primebot.demo.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.MonthDay;

@Converter(autoApply = true)
public class MonthDayConverter implements AttributeConverter<MonthDay, String> {

    @Override
    public String convertToDatabaseColumn(MonthDay attribute) {
        return attribute == null
                ? null
                : String.format("%02d-%02d",
                attribute.getMonthValue(),
                attribute.getDayOfMonth());
    }

    @Override
    public MonthDay convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return MonthDay.parse("--" + dbData);
    }
}