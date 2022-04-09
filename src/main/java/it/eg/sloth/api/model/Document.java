package it.eg.sloth.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Schema(description = "Id", required = true, example = "doc-1")
    private String id;

    @Schema(description = "Name", required = true, example = "Titolo")
    private String name;

    @Schema(description = "Description", required = true, example = "Descrizione")
    private String description;

    @Schema(description = "Data", required = true, example = "Descrizione")
    private OffsetDateTime data;

}
