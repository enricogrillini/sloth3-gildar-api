package it.eg.sloth.api.service;

import it.eg.sloth.api.model.api.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class DocumentServices implements InitializingBean {


    private Map<Integer, Document> map;

    @Override
    public void afterPropertiesSet() {
        map = new LinkedHashMap<>();

        save(new Document(1, "Contratto", LocalDate.now(), 50.4, true));
        save(new Document(2, "Recesso", LocalDate.now(), 50.4, true));
        save(new Document(3, "Appendice", LocalDate.now(), 50.4, true));
    }

    /**
     * Ritorna la lista documenti
     *
     * @return
     */
    public List<Document> getDocuments() {
        return Collections.unmodifiableList(new ArrayList<>(map.values()));
    }

    /**
     * Ritorna un singolo documento
     *
     * @param documentId
     * @return
     */
    public Document getDocument(Integer documentId) {
        return map.get(documentId);
    }

    /**
     * Elimina un documento
     *
     * @param documentId
     */
    public void delete(Integer documentId) {
        map.remove(documentId);
    }

    /**
     * Aggiorna o inserisce un documento
     *
     * @param document
     */
    public void save(Document document) {
        map.put(document.getIdDocument(), document);
    }


}

