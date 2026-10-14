package nl.justid.skdb.infrastructure.web;


import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;

import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource; 
import org.junit.jupiter.api.DisplayName;

import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;
import nl.justid.skdb.domain.exception.SknBestaatAlException;
import nl.justid.skdb.domain.exception.BsnBestaatAlException;
import nl.justid.skdb.application.service.RegistreerPersoonService;

import java.util.stream.Stream;

@WebMvcTest(PersoonController.class)
public class PersoonControllerTest {
    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean 
    private RegistreerPersoonService service;

    private final Strafrechtketennummer skn = new Strafrechtketennummer("SKN-2026-123456");
    private final Burgerservicenummer bsn = new Burgerservicenummer("123456782");

    @Test
    @DisplayName("Moet HTTP-statuscode 200 en NatuurlijkPersoon teruggeven wanneer die wordt gevonden op Strafrechtketennummer.")
    void shouldReturnInstanceOnFindBySkn() throws Exception {
        Mockito.when(service.registreer(skn, bsn)).thenReturn(new NatuurlijkPersoon(skn, bsn));

        mockMvc.perform(get("/api/personen/{skn}", skn.value())).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Moet HTTP-statuscode 201 en NatuurlijkPersoon teruggeven bij succesvolle registratie.")
    void shouldReturnInstanceOnPost() throws Exception {
        Mockito.when(service.registreer(skn, bsn)).thenReturn(new NatuurlijkPersoon(skn, bsn));

        String json = """
                {
                    "skn": "%s",
                    "bsn": "%s"
                }
                """.formatted(skn.value(), bsn.value());

        mockMvc.perform(post("/api/personen").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated());
    }

    @ParameterizedTest 
    @DisplayName("Moet HTTP-statuscode 400 teruggeven bij null waardes, lege strings, en strings met whitespace.")
    @MethodSource("invalidJsonProvider")
    void shouldReturn400WhenNullEmptyOrWhitespace(String json) throws Exception {
        mockMvc.perform(post("/api/personen").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());
        
        Mockito.verifyNoInteractions(service);
    }

    static Stream<String> invalidJsonProvider() {
        return Stream.of(
            """
            {

            }
            """,
            """
            {
                "skn": "",
                "bsn": ""
            }
            """,
            """
            {
                "skn": "  ",
                "bsn": "  "
            }
            """
        );
    }

    @Test 
    @DisplayName("Moet HTTP-statuscode 422 teruggeven bij geldige json maar met ongeldig formaat (IllegalArgumentException)")
    void shouldReturn422WhenIllegalArgumentException() throws Exception {
        String json = """
                {
                    "skn": "ongeldig-formaat",
                    "bsn": "ongeldig-formaat"
                }
                """;

        mockMvc.perform(post("/api/personen").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().is(422));
    }

    @Test
    @DisplayName("Moet HTTP-statuscode 409 teruggeven bij dubbele data (SknBestaatAlException)")
    void shouldReturn409WhenSknAlreadyExists() throws Exception {
        Mockito.when(service.registreer(skn, bsn)).thenThrow(new SknBestaatAlException(skn));

        String json = """
                {
                    "skn": "%s",
                    "bsn": "%s"
                }
                """.formatted(skn.value(), bsn.value());

        mockMvc.perform(post("/api/personen").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().is(409));
    }

    @Test
    @DisplayName("Moet HTTP-statuscode 409 teruggeven bij dubbele data (BsnBestaatAlException)")
    void shouldReturn409WhenBsnAlreadyExists() throws Exception {
        Mockito.when(service.registreer(skn, bsn)).thenThrow(new BsnBestaatAlException(bsn));

        String json = """
                {
                    "skn": "%s",
                    "bsn": "%s"
                }
                """.formatted(skn.value(), bsn.value());

        mockMvc.perform(post("/api/personen").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().is(409));
    }
}
