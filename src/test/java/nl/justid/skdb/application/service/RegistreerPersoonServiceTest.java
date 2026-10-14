package nl.justid.skdb.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import nl.justid.skdb.infrastructure.persistence.inmemory.InMemoryPersoonRepository;
import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;

import nl.justid.skdb.domain.exception.BsnBestaatAlException;
import nl.justid.skdb.domain.exception.SknBestaatAlException;

class RegistreerPersoonServiceTest {
    private InMemoryPersoonRepository repository = new InMemoryPersoonRepository();
    private RegistreerPersoonService registreerPersoonService = new RegistreerPersoonService(repository);

    private final Strafrechtketennummer skn = new Strafrechtketennummer("SKN-2026-456789");
    private final Burgerservicenummer bsn = new Burgerservicenummer("111222333");

    @Test
    @DisplayName("Moet succesvol geregistreerd worden wanneer SKN en BSN beschikbaar zijn en NatuurlijkPersoon teruggeven.")
    void shouldReturnNatuurlijkPersoon() {
        NatuurlijkPersoon persoon = registreerPersoonService.registreer(skn, bsn);

        Optional<NatuurlijkPersoon> result = repository.findBySkn(skn);
        
        assertThat(result).contains(persoon);
    }

    @Test
    @DisplayName("Mag niet aangemaakt worden wanneer SKN al bestaat.")
    void shouldThrowExceptionWhenSknAlreadyExists() {
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);

        repository.save(persoon);

        assertThatThrownBy(() -> {
            registreerPersoonService.registreer(skn, bsn);
        }).isInstanceOf(SknBestaatAlException.class);
    }

    @Test
    @DisplayName("Mag niet aangemaakt worden wanneer BSN al bestaat.")
    void shouldThrowExceptionWhenBsnAlreadyExists() {
        Strafrechtketennummer skn2 = new Strafrechtketennummer("SKN-2026-123456");
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);

        repository.save(persoon);

        assertThatThrownBy(() -> {
            registreerPersoonService.registreer(skn2, bsn);
        }).isInstanceOf(BsnBestaatAlException.class);
    }
}
