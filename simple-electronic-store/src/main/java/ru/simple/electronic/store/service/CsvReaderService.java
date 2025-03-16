package ru.simple.electronic.store.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.simple.electronic.store.dto.ProductDto;

import java.io.IOException;
import java.io.InputStream;

@Component
public class CsvReaderService {

    private static final CsvMapper CSV_MAPPER = new CsvMapper();

    public Flux<ProductDto> readCsv(InputStream content) {
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        try {
            MappingIterator<ProductDto> iterator = CSV_MAPPER.reader(ProductDto.class).with(schema).readValues(content);
            return Flux.fromIterable(iterator.readAll());
        } catch (IOException e) {
            return Flux.empty();
        }
    }
}
