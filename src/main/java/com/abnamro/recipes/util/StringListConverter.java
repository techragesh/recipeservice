package com.abnamro.recipes.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute == null
                ? null
                : attribute.stream().map(s -> s.toLowerCase().trim()).collect(Collectors.joining(","));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return dbData == null
                ? null
                : Arrays.stream(dbData.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
