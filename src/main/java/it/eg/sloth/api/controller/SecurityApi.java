package it.eg.sloth.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.eg.sloth.api.error.model.ResponseMessage;
import it.eg.sloth.api.model.Jwt;
import it.eg.sloth.api.model.Login;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Security API", description = "Rest API - Login")
public interface SecurityApi {

    @Operation(summary = "Aggiunge un documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Jwt.class))}),
            @ApiResponse(responseCode = "400", description = "Impossibile inserire il documento", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))})
    })
    Jwt login(@RequestBody Login login);

}
