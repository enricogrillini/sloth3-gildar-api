package it.eg.sloth.api.model.db;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DocumentPojo {
    Integer idDocument;
    String name;
    LocalDate documentDate;
    Double cost;
    String flagActive;

    String userName;
    LocalDateTime dataUltimoAggiornamento;
}