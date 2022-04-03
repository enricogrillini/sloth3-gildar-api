package it.eg.sloth.api.service;

import it.eg.sloth.api.model.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class DocumentServices implements InitializingBean {


    private Map<String, Document> map;

    @Override
    public void afterPropertiesSet() throws Exception {
        map = new LinkedHashMap<>();

        save(new Document("doc-1", "Contratto", "Contratto tra le parti per sottoscrizione conto corrente", OffsetDateTime.now()));
        save(new Document("doc-2", "Recesso", "Norme per il recesso", OffsetDateTime.now()));
        save(new Document("doc-3", "Appendice", "Appendice al contratto di sottoscrizione", OffsetDateTime.now()));
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
    public Document getDocument(String documentId) {
        return map.get(documentId);
    }

    /**
     * Elimina un documento
     *
     * @param documentId
     */
    public void delete(String documentId) {
        map.remove(documentId);
    }

    /**
     * Aggiorna o inserisce un documento
     *
     * @param document
     */
    public void save(Document document) {
        map.put(document.getId(), document);
    }


}

