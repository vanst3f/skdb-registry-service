package nl.justid.skdb.infrastructure.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;

class PersoonMapperTest {
    PersoonMapper mapper = new PersoonMapper();

    Strafrechtketennummer skn = new Strafrechtketennummer("SKN-2026-123456");
    Burgerservicenummer bsn = new Burgerservicenummer("123456782");

    @Test
    @DisplayName("Moet een natuurlijk persoon succesvol omzetten naar een database-entiteit (PersoonJpaEntity).")
    void shouldMapToDatabaseRecord() {
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);

        PersoonJpaEntity entiteit = mapper.toJpaEntity(persoon);

        assertThat(entiteit.getStrafrechtketennummer()).isEqualTo(skn.value());
        assertThat(entiteit.getBurgerservicenummer()).isEqualTo(bsn.value());
    }

    @Test
    @DisplayName("Moet een database-entiteit succesvol omzetten naar een natuurlijk persoon (NatuurlijkPersoon).")
    void shouldMapToDomain() {
        PersoonJpaEntity entiteit = new PersoonJpaEntity(skn.value(), bsn.value());

        NatuurlijkPersoon persoon = mapper.toDomain(entiteit);

        assertThat(persoon.getStrafrechtketennummer()).isEqualTo(skn);
        assertThat(persoon.getBurgerservicenummer()).isEqualTo(bsn);
    }
}
