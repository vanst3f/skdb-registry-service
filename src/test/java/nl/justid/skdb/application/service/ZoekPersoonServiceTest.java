package nl.justid.skdb.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import nl.justid.skdb.infrastructure.persistence.inmemory.InMemoryPersoonRepository;
import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;
import nl.justid.skdb.domain.exception.PersoonNietGevondenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZoekPersoonServiceTest {
    private InMemoryPersoonRepository repository = new InMemoryPersoonRepository();
    private ZoekPersoonService zoekPersoonService = new ZoekPersoonService(repository);

    private final Strafrechtketennummer skn = new Strafrechtketennummer("SKN-2026-456789");
    private final Burgerservicenummer bsn = new Burgerservicenummer("111222333");

    @Test
    @DisplayName("Moet NatuurlijkPersoon teruggeven bij ophalen met Strafrechtketennummer.")
    void shouldReturnNatuurlijkPersoonWithValidSkn() {
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);
        repository.save(persoon);

        NatuurlijkPersoon result = zoekPersoonService.findBySkn(skn);

        assertThat(result).isEqualTo(persoon);
    }

    @Test
    @DisplayName("Moet NatuurlijkPersoon teruggeven bij ophalen met Burgerservicenummer.")
    void shouldReturnNatuurlijkPersoonWithValidBsn() {
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);
        repository.save(persoon);

        NatuurlijkPersoon result = zoekPersoonService.findByBsn(bsn);

        assertThat(result).isEqualTo(persoon);
    }

    @Test 
    @DisplayName("Moet exception gooien bij zoeken naar niet bestaand NatuurlijkPersoon op Strafrechtketennummer.")
    void shouldThrowPersoonNietGevondenExceptionSkn() {
        assertThatThrownBy(() -> {
            zoekPersoonService.findBySkn(skn);
        }).isInstanceOf(PersoonNietGevondenException.class);
    }

    @Test 
    @DisplayName("Moet PersoonNietGevondenException gooien bij zoeken naar niet bestaand NatuurlijkPersoon op Burgerservicenummer.")
    void shouldThrowPersoonNietGevondenExceptionBsn() {
        assertThatThrownBy(() -> {
            zoekPersoonService.findByBsn(bsn);
        }).isInstanceOf(PersoonNietGevondenException.class);
    }
}
