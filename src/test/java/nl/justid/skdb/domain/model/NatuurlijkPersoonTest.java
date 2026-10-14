package nl.justid.skdb.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NatuurlijkPersoonTest {
    private final Strafrechtketennummer geldigSkn = new Strafrechtketennummer("SKN-2026-456789");
    private final Burgerservicenummer geldigBsn = new Burgerservicenummer("111222333");

    @Test
    @DisplayName("Moet succesvol aangemaakt worden met geldig SKN en BSN")
    void shouldCreateWithValidData() {
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(geldigSkn, geldigBsn);

        assertNotNull(persoon);
        assertEquals(geldigSkn, persoon.getStrafrechtketennummer());
        assertEquals(geldigBsn, persoon.getBurgerservicenummer());
    }

    @Test
    @DisplayName("Mag niet aangemaakt worden zonder Strafrechtketennummer (null)")
    void shouldThrowExceptionWhenSknIsNull() {
        assertThrows(NullPointerException.class, () -> new NatuurlijkPersoon(null, geldigBsn));
    }

    @Test
    @DisplayName("Mag niet aangemaakt worden zonder Burgerservicenummer (null)")
    void shouldThrowExceptionWhenBsnIsNull() {
        assertThrows(NullPointerException.class, () -> new NatuurlijkPersoon(geldigSkn, null));
    }

    @Test
    @DisplayName("Twee personen met hetzelfde SKN moeten gelijk zijn (identiteit), ongeacht ander BSN")
    void shouldBeEqualWhenSknIsTheSame() {
        final Burgerservicenummer geldigBsn2 = new Burgerservicenummer("999999999");

        NatuurlijkPersoon persoon1 = new NatuurlijkPersoon(geldigSkn, geldigBsn);
        NatuurlijkPersoon persoon2 = new NatuurlijkPersoon(geldigSkn, geldigBsn2);

        assertEquals(persoon1, persoon2);
    }
}
