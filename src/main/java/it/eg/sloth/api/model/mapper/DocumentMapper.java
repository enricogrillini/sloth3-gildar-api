package it.eg.sloth.api.model.mapper;

import it.eg.sloth.api.common.BffMapper;
import it.eg.sloth.api.model.api.Document;
import it.eg.sloth.api.model.db.DocumentPojo;
import it.eg.sloth.api.model.prova.Car;
import it.eg.sloth.api.model.prova.CarBff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = BffMapper.class)
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    @Mapping(source = "flagActive", target = "active")
    Document documentPojoToApi(DocumentPojo document);

    @Mapping(source = "active", target = "flagActive")
    DocumentPojo documentApiToPojo(Document document);
}