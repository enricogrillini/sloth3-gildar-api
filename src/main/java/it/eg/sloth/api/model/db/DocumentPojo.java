package it.eg.sloth.api.model.db;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentPojo {
    Integer idDocument;
    String name;
    LocalDate documentDate;
    Double cost;
    String flagActive;
}