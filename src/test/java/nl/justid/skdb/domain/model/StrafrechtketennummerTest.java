package nl.justid.skdb.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StrafrechtketennummerTest {
    @Test
    @DisplayName("Geldig SKN-formaat moet succesvol gecreëerd worden.")

    void shouldCreateWhenFormatIsValid() {
        assertDoesNotThrow(() -> new Strafrechtketennummer("SKN-2026-123456"));
    }

    @ParameterizedTest
    @NullAndEmptySource 
    @ValueSource(strings = {"   ", "SKN-123", "skn-2026-123456", "ONBEKEND"})
    @DisplayName("Ongeldige formaten of lege waarden moeten IllegalArgumentException gooien.")
    
    void shouldThrowWhenFormatIsInValid(String invalidValue) {
        // assertThrows controleert het type Exception dat gegooid wordt
        assertThrows(IllegalArgumentException.class, () -> new Strafrechtketennummer(invalidValue));
    }
}
