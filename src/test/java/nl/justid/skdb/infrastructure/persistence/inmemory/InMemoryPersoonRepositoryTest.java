package nl.justid.skdb.infrastructure.persistence.inmemory;

import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryPersoonRepositoryTest {
    private InMemoryPersoonRepository repository = new InMemoryPersoonRepository();
    
    private final Strafrechtketennummer strafrechtketennummer = new Strafrechtketennummer("SKN-2026-123456");
    private final Burgerservicenummer burgerservicenummer = new Burgerservicenummer("123456782");

    @Test
    @DisplayName("Persoon moet worden opgeslagen en teruggevonden worden per strafrechtketennummer.")
    void shouldCreateAndFindWithValidData() {
        final NatuurlijkPersoon persoon = new NatuurlijkPersoon(strafrechtketennummer, burgerservicenummer);

        assertThat(repository.save(persoon)).isEqualTo(persoon);
        assertThat(repository.findBySkn(strafrechtketennummer)).hasValue(persoon);
        assertThat(repository.findByBsn(burgerservicenummer)).hasValue(persoon);
    }

    @Test
    @DisplayName("Moet leeg zijn wanneer SKN of BSN niet bestaat.")
    void shouldBeEmptyWhenSknOrBsnDoNotExist() {
        assertThat(repository.findBySkn(strafrechtketennummer)).isEmpty();
        assertThat(repository.findByBsn(burgerservicenummer)).isEmpty();
    }
}
