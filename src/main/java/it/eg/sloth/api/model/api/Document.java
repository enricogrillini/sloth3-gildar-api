package it.eg.sloth.api.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Schema(description = "Id", required = true, example = "15")
    @NotNull
    Integer idDocument;

    @Schema(description = "Name", required = true, example = "Titolo")
    @NotNull
    String name;

    @Schema(description = "Document date", required = true)
    @NotNull
    LocalDate documentDate;

    @Schema(description = "Cost", required = false)
    Double cost;

    @Schema(description = "Active", required = false)
    Boolean active;


}
