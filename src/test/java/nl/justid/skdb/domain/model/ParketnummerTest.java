package nl.justid.skdb.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParketnummerTest {
    @Test
    @DisplayName("Geldig PN-formaat moet succesvol gecreëerd worden.")
    void shouldCreateWhenFormatIsValid() {
        assertDoesNotThrow(() -> new Parketnummer("09/123456-26"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "0912345626", "09123456-26", "09/12345626", "09/12345-26", "09/1A23456-26", "09/123456_26"})
    @DisplayName("Ongeldige formaten of lege waarden moeten IllegalArgumentException gooien.")

    void shouldThrowWhenFormatIsInValid(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> new Parketnummer(invalidValue));
    }
}
