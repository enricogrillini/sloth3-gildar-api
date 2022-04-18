package it.eg.sloth.api.controller;

import it.eg.sloth.api.decodemap.DecodeValue;
import it.eg.sloth.api.model.db.DocumentPojo;
import it.eg.sloth.api.model.mapper.DocumentMapper;
import it.eg.sloth.api.service.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/decode")
@Slf4j
public class DecodeController implements DecodeApi {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    @GetMapping(path = "document", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DecodeValue<Integer>> getDocuments() {
        List<DocumentPojo> list = documentRepository.select(null);

        return DocumentMapper.INSTANCE.toDecodeMap(list);
    }
}




