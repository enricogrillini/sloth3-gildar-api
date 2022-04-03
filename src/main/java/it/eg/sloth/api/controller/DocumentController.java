package it.eg.sloth.api.controller;

import it.eg.sloth.api.error.BusinessException;
import it.eg.sloth.api.error.ResponseCode;
import it.eg.sloth.api.error.ResponseMessage;
import it.eg.sloth.api.model.Document;

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
    public Document getDocument(@PathVariable String documentId) {
        if (documentServices.getDocument(documentId) != null) {
            return documentServices.getDocument(documentId);
        } else {
            throw new BusinessException(ResponseCode.DOCUMENTO_NON_TROVATO);
        }
    }

    /**
     * Elimina un documento
     *
     * @return
     */
    @DeleteMapping(path = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage deleteDocument(@PathVariable String documentId) {
        if (documentServices.getDocument(documentId) != null) {
            documentServices.delete(documentId);

            return new ResponseMessage(true, ResponseCode.OK, "Documento eliminato correttamente");
        } else {
            throw new BusinessException(ResponseCode.DOCUMENTO_NON_TROVATO);
        }
    }


    /**
     * Crea un documento
     *
     * @return
     */
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage postDocument(@RequestBody Document document) {
        if (documentServices.getDocument(document.getId()) == null) {
            documentServices.save(document);
            return new ResponseMessage(true, ResponseCode.OK, "Documento creato correttamente");
        } else {
            throw new BusinessException(ResponseCode.DOCUMENTO_GIA_PRESENTE);
        }
    }

    /**
     * Aggiorna un documento
     *
     * @return
     */
    @PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage putDocument(@RequestBody Document document) {
        if (documentServices.getDocument(document.getId()) != null) {
            documentServices.save(document);
            return new ResponseMessage(true, ResponseCode.OK, "Documento aggiornato correttamente");
        } else {
            throw new BusinessException(ResponseCode.DOCUMENTO_NON_TROVATO);
        }
    }
}



