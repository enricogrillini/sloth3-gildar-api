package it.eg.sloth.api.model.mapper;

import it.eg.sloth.api.common.PojoMapper;
import it.eg.sloth.api.decodemap.DecodeValue;
import it.eg.sloth.api.model.api.Document;
import it.eg.sloth.api.model.db.DocumentPojo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = PojoMapper.class)
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    @Mapping(source = "flagActive", target = "active")
    Document documentPojoToApi(DocumentPojo document);

    @Mapping(source = "active", target = "flagActive")
    DocumentPojo documentApiToPojo(Document document);
    List<Document> documentPojoToApi(List<DocumentPojo> document);

    @Mapping(source = "idDocument", target = "code")
    @Mapping(source = "name", target = "description")
    @Mapping(source = "flagActive", target = "valid")
    DecodeValue<Integer> toDecodeValue(DocumentPojo pojo);

    List<DecodeValue<Integer>> toDecodeMap(List<DocumentPojo> list);

}