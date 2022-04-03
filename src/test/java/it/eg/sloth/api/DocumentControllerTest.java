package it.eg.sloth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.eg.sloth.api.error.ResponseCode;
import it.eg.sloth.api.error.ResponseMessage;
import it.eg.sloth.api.model.Document;
import it.eg.sloth.core.jwt.JwtUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.time.OffsetDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DocumentControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String URI = "/api/v1/document";
    private static final String URI_ID = "/api/v1/document/{documentId}";

    private static PrivateKey privateKey;

    @BeforeAll
    public static void aaa() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        privateKey = JwtUtil.getPrivateKey("private_key_jwt.pem");
    }

    @Test
    @Order(1)
    void getDocumentsTest() throws Exception {
        String jwtToken = JwtUtil.createJWT("www.iam.com", "reader-1", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Verifico che la lista di documenti non sia vuota
        Document[] documents = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Document[].class);
        assertEquals(3, documents.length);
    }

    @Test
    @Order(2)
    void getDocumentTest() throws Exception {
        String jwtToken = JwtUtil.createJWT("www.iam.com", "reader-1", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get(URI_ID, "doc-1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        Document documento = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Document.class);
        assertEquals("doc-1", documento.getId());
    }

    @Test
    @Order(3)
    void getDocumentTestKO() throws Exception {
        String jwtToken = JwtUtil.createJWT("www.iam.com", "reader-1", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get(URI_ID, "XX")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertEquals(false, responseMessage.getSuccess());
        assertEquals("Documento non trovato", responseMessage.getMessage());
    }

    @Test
    @Order(4)
    void deleteDocumentTest() throws Exception {
        String jwtToken = JwtUtil.createJWT("www.iam.com", "admin-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(URI_ID, "doc-1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertEquals(true, responseMessage.getSuccess());
        assertEquals(ResponseCode.OK.name(), responseMessage.getCode());
        assertEquals(ResponseCode.OK.getMessage(), responseMessage.getMessage());
        assertEquals("Documento eliminato correttamente", responseMessage.getDescription());
    }

    @Test
    @Order(5)
    void deleteDocumentTestKO() throws Exception {
        String jwtToken = JwtUtil.createJWT("www.iam.com", "admin-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(URI_ID, "XX")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertEquals(false, responseMessage.getSuccess());
        assertEquals("Documento non trovato", responseMessage.getMessage());
    }

    @Test
    @Order(6)
    void deleteDocumentTestKOSec() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(URI_ID, "XX")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer fake"))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.FORBIDDEN.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @Order(7)
    void postDocumentTest() throws Exception {
        Document document = new Document("doc-5", "Documento 5", "Descrizione Documento 5", OffsetDateTime.now());
        String documentStr = objectMapper.writeValueAsString(document);

        String jwtToken = JwtUtil.createJWT("www.iam.com", "writer-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr)
                        .header(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertEquals(true, responseMessage.getSuccess());
        assertEquals(ResponseCode.OK.name(), responseMessage.getCode());
        assertEquals(ResponseCode.OK.getMessage(), responseMessage.getMessage());
        assertEquals("Documento creato correttamente", responseMessage.getDescription());
    }

    @Test
    @Order(8)
    void postDocumentTestKO() throws Exception {
        Document document = new Document("doc-2", "Documento 5", "Descrizione Documento 5", OffsetDateTime.now());
        String documentStr = objectMapper.writeValueAsString(document);
        String jwtToken = JwtUtil.createJWT("www.iam.com", "writer-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr)
                        .header(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), ResponseMessage.class);
        assertEquals(false, responseMessage.getSuccess());
        assertEquals("Id documento è già presente", responseMessage.getMessage());
    }


    @Test
    @Order(9)
    void postDocumentTestKOSec() throws Exception {
        Document document = new Document("doc-2", "Documento 5", "Descrizione Documento 5", OffsetDateTime.now());
        String documentStr = objectMapper.writeValueAsString(document);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr)
                        .header("Authorization", "Bearer fake"))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.FORBIDDEN.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @Order(10)
    void putDocumentTest() throws Exception {
        Document document = new Document("doc-2", "Documento 2", "Descrizione corretta", OffsetDateTime.now());
        String documentStr = objectMapper.writeValueAsString(document);
        String jwtToken = JwtUtil.createJWT("www.iam.com", "writer-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr)
                        .header(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertEquals(true, responseMessage.getSuccess());
        assertEquals(ResponseCode.OK.name(), responseMessage.getCode());
        assertEquals(ResponseCode.OK.getMessage(), responseMessage.getMessage());
        assertEquals("Documento aggiornato correttamente", responseMessage.getDescription());
    }

    @Test
    @Order(11)
    void putDocumentTestKO() throws Exception {
        Document document = new Document("doc-6", "Documento 6", "Descrizione Documento 6", OffsetDateTime.now());
        String documentStr = objectMapper.writeValueAsString(document);
        String jwtToken = JwtUtil.createJWT("www.iam.com", "writer-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr)
                        .header(JwtUtil.TOKEN_HEADER, JwtUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), ResponseMessage.class);
        assertEquals(false, responseMessage.getSuccess());
        assertEquals("Documento non trovato", responseMessage.getMessage());
    }

    @Test
    @Order(12)
    void putDocumentTestKOSec() throws Exception {
        Document document = new Document("doc-6", "Documento 6", "Descrizione Documento 6", OffsetDateTime.now());
        String documentStr = objectMapper.writeValueAsString(document);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.FORBIDDEN.value(), mvcResult.getResponse().getStatus());
    }

}