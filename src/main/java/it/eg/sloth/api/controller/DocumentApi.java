package it.eg.sloth.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.eg.sloth.api.error.model.ResponseMessage;
import it.eg.sloth.api.model.Document;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Document API", description = "Rest API - Document CRUD")
@SecurityRequirement(name = "bearerAuth")
public interface DocumentApi {

    @Operation(summary = "Ritorna la lista di documenti")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista di documenti", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Document.class)))})
    })
    List<Document> getDocuments();

    @Operation(summary = "Ritorna un documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento trovato", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "400", description = "Errore nel reperimento del documento", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))})
    })
    Document getDocument(@PathVariable String documentId);


    @Operation(summary = "Elimina un documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))}),
            @ApiResponse(responseCode = "400", description = "Impossibile cancellare il documento", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))})
    })
    ResponseMessage deleteDocument(@PathVariable String documentId);


    @Operation(summary = "Aggiunge un documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))}),
            @ApiResponse(responseCode = "400", description = "Impossibile inserire il documento", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))})
    })
    ResponseMessage postDocument(@RequestBody Document document);

    @Operation(summary = "Aggiorna un documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))}),
            @ApiResponse(responseCode = "400", description = "Impossibile aggiornare il documento", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))})
    })
    ResponseMessage putDocument(@RequestBody Document document);

}
