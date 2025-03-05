package ru.simple.electronic.store.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;
import ru.simple.electronic.store.dto.ProductDto;

import java.io.IOException;
import java.util.List;

@Component
public class CsvReaderService {

    private static final CsvMapper CSV_MAPPER = new CsvMapper();

    public List<ProductDto> readCsv(byte[] content) {
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        try {
            MappingIterator<ProductDto> iterator = CSV_MAPPER.reader(ProductDto.class).with(schema).readValues(content);
            return iterator.readAll();
        } catch (IOException e) {
            return List.of();
        }
    }
}
