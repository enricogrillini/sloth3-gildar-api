package it.eg.sloth.api.controller;

import it.eg.sloth.api.error.exception.BusinessException;
import it.eg.sloth.api.error.model.ResponseCode;
import it.eg.sloth.api.error.model.ResponseMessage;
import it.eg.sloth.api.model.api.Document;
import it.eg.sloth.api.model.db.DocumentPojo;
import it.eg.sloth.api.model.mapper.DocumentMapper;
import it.eg.sloth.api.service.DocumentRepository;
import it.eg.sloth.core.base.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
@Slf4j
public class DocumentController implements DocumentApi {

    private static final String DOCUMENTO_NON_TOVATO = "Documento non trovato";
    private static final String DOCUMENTO_GIA_PRESENTE = "Documento già presente";

    @Autowired
    private DocumentRepository documentRepository;

    /**
     * Ritorna la lista di tutti i documenti
     *
     * @return
     */
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Document> getDocuments() {
        List<DocumentPojo> list = documentRepository.select(null);

        return DocumentMapper.INSTANCE.documentPojoToApi(list);
    }

    /**
     * Ritorna un documento
     *
     * @return
     */
    @GetMapping(path = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Document getDocument(@PathVariable Integer documentId) {
        List<DocumentPojo> list = documentRepository.select(documentId);
        if (!list.isEmpty()) {
            return DocumentMapper.INSTANCE.documentPojoToApi(list.get(0));
        } else {
            throw new BusinessException(DOCUMENTO_NON_TOVATO, ResponseCode.NOT_FOUND);
        }
    }

    /**
     * Elimina un documento
     *
     * @return
     */
    @DeleteMapping(path = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage deleteDocument(@PathVariable Integer documentId) {
        if (!documentRepository.select(documentId).isEmpty()) {
            documentRepository.delete(documentId);

            return new ResponseMessage("Documento eliminato correttamente");
        } else {
            throw new BusinessException(DOCUMENTO_NON_TOVATO, ResponseCode.NOT_FOUND);
        }
    }


    /**
     * Crea un documento
     *
     * @return
     */
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage postDocument(@RequestBody Document document) {
        if (ObjectUtil.isNull(document.getIdDocument())) {
            throw new BusinessException("Specificare l'idDocument", ResponseCode.BUSINESS_ERROR);
        } else if (!documentRepository.select(document.getIdDocument()).isEmpty()) {
            throw new BusinessException(DOCUMENTO_GIA_PRESENTE);
        } else {
            DocumentPojo documentPojo = DocumentMapper.INSTANCE.documentApiToPojo(document);
            documentRepository.insert(documentPojo);

            return new ResponseMessage("Documento creato correttamente");
        }
    }

    /**
     * Aggiorna un documento
     *
     * @return
     */
    @PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage putDocument(@RequestBody Document document) {
        if (ObjectUtil.isNull(document.getIdDocument())) {
            throw new BusinessException("Specificare l'idDocument", ResponseCode.BUSINESS_ERROR);
        } else if (documentRepository.select(document.getIdDocument()).isEmpty()) {
            throw new BusinessException(DOCUMENTO_NON_TOVATO, ResponseCode.NOT_FOUND);
        } else {
            DocumentPojo documentPojo = DocumentMapper.INSTANCE.documentApiToPojo(document);
            documentRepository.update(documentPojo);

            return new ResponseMessage("Documento aggiornato correttamente");
        }
    }
}




