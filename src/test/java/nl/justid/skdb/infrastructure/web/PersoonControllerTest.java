package nl.justid.skdb.infrastructure.web;


import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import nl.justid.skdb.application.service.ZoekPersoonService;

import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(PersoonController.class)
public class PersoonControllerTest {
    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean 
    private RegistreerPersoonService registreerPersoonService;

    @MockitoBean
    private ZoekPersoonService zoekPersoonService;

    private final Strafrechtketennummer skn = new Strafrechtketennummer("SKN-2026-123456");
    private final Burgerservicenummer bsn = new Burgerservicenummer("123456782");

    @Test
    @DisplayName("Moet HTTP-statuscode 201 en NatuurlijkPersoon teruggeven bij succesvolle registratie.")
    void shouldReturnNatuurlijkPersoonOnPost() throws Exception {
        Mockito.when(registreerPersoonService.registreer(skn, bsn)).thenReturn(new NatuurlijkPersoon(skn, bsn));

        String json = """
                {
                    "skn": "%s",
                    "bsn": "%s"
                }
                """.formatted(skn.value(), bsn.value());

        mockMvc.perform(post("/api/personen").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Moet HTTP-statuscode 200 en NatuurlijkPersoon teruggeven wanneer die wordt gevonden op Strafrechtketennummer.")
    void shouldReturnNatuurlijkPersoonOnFindBySkn() throws Exception {
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);
        Mockito.when(zoekPersoonService.findBySkn(skn)).thenReturn(Optional.of(persoon));

        mockMvc.perform(get("/api/personen/{skn}", skn.value())).andExpect(status().isOk()).andExpect(jsonPath("$.skn").value(skn.value())).andExpect(jsonPath("$.bsn").value(bsn.value()));
    }

    @Test
    @DisplayName("Moet HTTP-statuscode 200 en NatuurlijkPersoon teruggeven wanneer die wordt gevonden op Burgerservicenummer.")
    void shouldReturnNatuurlijkPersoonOnFindByBsn() throws Exception {
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);
        Mockito.when(zoekPersoonService.findByBsn(bsn)).thenReturn(Optional.of(persoon));

        mockMvc.perform(get("/api/personen?bsn={bsn}", bsn.value())).andExpect(status().isOk()).andExpect(jsonPath("$.skn").value(skn.value())).andExpect(jsonPath("$.bsn").value(bsn.value()));
    }

    @ParameterizedTest 
    @DisplayName("Moet HTTP-statuscode 400 teruggeven bij null waardes, lege strings, en strings met whitespace.")
    @MethodSource("invalidJsonProvider")
    void shouldReturn400WhenNullEmptyOrWhitespace(String json) throws Exception {
        mockMvc.perform(post("/api/personen").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());
        
        Mockito.verifyNoInteractions(registreerPersoonService);
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
        Mockito.when(registreerPersoonService.registreer(skn, bsn)).thenThrow(new SknBestaatAlException(skn));

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
        Mockito.when(registreerPersoonService.registreer(skn, bsn)).thenThrow(new BsnBestaatAlException(bsn));

        String json = """
                {
                    "skn": "%s",
                    "bsn": "%s"
                }
                """.formatted(skn.value(), bsn.value());

        mockMvc.perform(post("/api/personen").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().is(409));
    }

        @Test
    @DisplayName("Moet HTTP-statuscode 404 teruggeven bij zoeken op niet bestaand NatuurlijkPersoon met Strafrechtketennummer (PersoonNietGevondenException)")
    void shouldReturn404WhenNatuurlijkPersoonDoesNotExistSkn() throws Exception {
        Mockito.when(zoekPersoonService.findBySkn(skn)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/personen/{skn}", skn.value())).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Moet HTTP-statuscode 404 teruggeven bij zoeken op niet bestaand NatuurlijkPersoon met Burgerservicenummer (PersoonNietGevondenException)")
    void shouldReturn404WhenNatuurlijkPersoonDoesNotExistBsn() throws Exception {
        Mockito.when(zoekPersoonService.findByBsn(bsn)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/personen?bsn={bsn}", bsn.value())).andExpect(status().isNotFound());
    }
}
