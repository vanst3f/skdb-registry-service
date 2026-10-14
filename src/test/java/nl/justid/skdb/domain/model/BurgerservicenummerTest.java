package nl.justid.skdb.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BurgerservicenummerTest {
    @Test
    @DisplayName("Geldig BSN-formaat moet succesvol gecreëerd worden.")

    void shouldCreateWhenFormatIsValid() {
        assertDoesNotThrow(() -> new Burgerservicenummer("123456782"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings={" ", "  ", "\t", "\n", "12345678", "abcdefghi", "123456789", "000000000"})
    void shouldThrowWhenFormatIsInValid(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> new Burgerservicenummer(invalidValue));
    }
}
