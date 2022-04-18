package it.eg.sloth.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.eg.sloth.api.decodemap.DecodeValue;
import it.eg.sloth.api.error.model.ResponseMessage;
import it.eg.sloth.api.model.api.Document;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Decode API", description = "Rest API - Decodifica entità")
@SecurityRequirement(name = "bearerAuth")
public interface DecodeApi {

    @Operation(summary = "Ritorna la decodifica dei documenti")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Decodifica documenti", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DecodeValue.class)))})
    })
    List<DecodeValue<Integer>> getDocuments();

}
