package it.eg.sloth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.eg.sloth.api.error.model.ResponseCode;
import it.eg.sloth.api.error.model.ResponseMessage;
import it.eg.sloth.api.model.api.Document;
import it.eg.sloth.core.token.JwtUtil;
import it.eg.sloth.core.token.TokenUtil;
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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

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
    public static void beforeAll() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
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
                        .header(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + jwtToken))
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
                        .get(URI_ID, 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        Document documento = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Document.class);
        assertEquals(1, documento.getIdDocument());
    }

    @Test
    @Order(3)
    void getDocumentTestKO() throws Exception {
        String jwtToken = JwtUtil.createJWT("www.iam.com", "reader-1", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get(URI_ID, 0)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertFalse(responseMessage.isSuccess());
        assertEquals(ResponseCode.NOT_FOUND, responseMessage.getCode());
        assertEquals("Documento non trovato", responseMessage.getDescription());
    }

    @Test
    @Order(4)
    void deleteDocumentTest() throws Exception {
        String jwtToken = JwtUtil.createJWT("www.iam.com", "admin-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(URI_ID, 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertTrue(responseMessage.isSuccess());
        assertEquals(ResponseCode.OK, responseMessage.getCode());
        assertEquals("Documento eliminato correttamente", responseMessage.getDescription());
    }

    @Test
    @Order(5)
    void deleteDocumentTestKO() throws Exception {
        String jwtToken = JwtUtil.createJWT("www.iam.com", "admin-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(URI_ID, 0)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertFalse(responseMessage.isSuccess());
        assertEquals(ResponseCode.NOT_FOUND, responseMessage.getCode());
        assertEquals("Documento non trovato", responseMessage.getDescription());
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
        Document document = new Document(5, "Documento 5", LocalDate.now(), 50.4, true);
        String documentStr = objectMapper.writeValueAsString(document);

        String jwtToken = JwtUtil.createJWT("www.iam.com", "writer-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr)
                        .header(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertTrue(responseMessage.isSuccess());
        assertEquals(ResponseCode.OK, responseMessage.getCode());
        assertEquals("Documento creato correttamente", responseMessage.getDescription());
    }

    @Test
    @Order(8)
    void postDocumentTestKO() throws Exception {
        Document document = new Document(2, "Documento 5", LocalDate.now(), 50.4, true);
        String documentStr = objectMapper.writeValueAsString(document);
        String jwtToken = JwtUtil.createJWT("www.iam.com", "writer-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr)
                        .header(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), ResponseMessage.class);
        assertFalse(responseMessage.isSuccess());
        assertEquals(ResponseCode.BUSINESS_ERROR, responseMessage.getCode());
        assertEquals("Documento già presente", responseMessage.getDescription());
    }


    @Test
    @Order(9)
    void postDocumentTestKOSec() throws Exception {
        Document document = new Document(2, "Documento 5", LocalDate.now(), 50.4, true);
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
        Document document = new Document(2, "Documento 5", LocalDate.now(), 50.4, true);
        String documentStr = objectMapper.writeValueAsString(document);
        String jwtToken = JwtUtil.createJWT("www.iam.com", "writer-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr)
                        .header(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseMessage.class);
        assertTrue(responseMessage.isSuccess());
        assertEquals(ResponseCode.OK, responseMessage.getCode());
        assertEquals("Documento aggiornato correttamente", responseMessage.getDescription());
    }

    @Test
    @Order(11)
    void putDocumentTestKO() throws Exception {
        Document document = new Document(6, "Documento 6", LocalDate.now(), 50.4, true);
        String documentStr = objectMapper.writeValueAsString(document);
        String jwtToken = JwtUtil.createJWT("www.iam.com", "writer-2", "gildar-api", 3600 * 1000, new HashMap<>(), privateKey);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put(URI)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(documentStr)
                        .header(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + jwtToken))
                .andReturn();

        // Verifico lo stato della risposta
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());

        // Verifico che lo Documento sia corretto
        ResponseMessage responseMessage = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), ResponseMessage.class);
        assertFalse(responseMessage.isSuccess());
        assertEquals(ResponseCode.NOT_FOUND, responseMessage.getCode());
        assertEquals("Documento non trovato", responseMessage.getDescription());
    }

    @Test
    @Order(12)
    void putDocumentTestKOSec() throws Exception {
        Document document = new Document(6, "Documento 6", LocalDate.now(), 50.4, true);
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