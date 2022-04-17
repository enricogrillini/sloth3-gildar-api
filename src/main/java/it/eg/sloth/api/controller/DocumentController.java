package it.eg.sloth.api.controller;

import it.eg.sloth.api.error.exception.BusinessException;
import it.eg.sloth.api.error.model.ResponseCode;
import it.eg.sloth.api.error.model.ResponseMessage;
import it.eg.sloth.api.model.api.Document;
import it.eg.sloth.api.service.DocumentServices;
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
    private DocumentServices documentServices;

    /**
     * Ritorna la lista di tutti i documenti
     *
     * @return
     */
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Document> getDocuments() {
        return documentServices.getDocuments();
    }

    /**
     * Ritorna un documento
     *
     * @return
     */
    @GetMapping(path = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Document getDocument(@PathVariable Integer documentId) {
        if (documentServices.getDocument(documentId) != null) {
            return documentServices.getDocument(documentId);
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
        if (documentServices.getDocument(documentId) != null) {
            documentServices.delete(documentId);

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
        if (documentServices.getDocument(document.getIdDocument()) == null) {
            documentServices.save(document);
            return new ResponseMessage("Documento creato correttamente");
        } else {
            throw new BusinessException(DOCUMENTO_GIA_PRESENTE);
        }
    }

    /**
     * Aggiorna un documento
     *
     * @return
     */
    @PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage putDocument(@RequestBody Document document) {
        if (documentServices.getDocument(document.getIdDocument()) != null) {
            documentServices.save(document);
            return new ResponseMessage("Documento aggiornato correttamente");
        } else {
            throw new BusinessException(DOCUMENTO_NON_TOVATO, ResponseCode.NOT_FOUND);
        }
    }
}




