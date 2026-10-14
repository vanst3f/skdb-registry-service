package nl.justid.skdb.infrastructure.persistence.jpa;

import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import nl.justid.skdb.TestcontainersConfiguration;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import({TestcontainersConfiguration.class, PostgresPersoonRepository.class, PersoonMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@DataJpaTest 
class PostgresPersoonRepositoryTest {
    @Autowired
    private PostgresPersoonRepository repository;
 
    private final Strafrechtketennummer skn = new Strafrechtketennummer("SKN-2026-123456");
    private final Burgerservicenummer bsn = new Burgerservicenummer("123456782");

    @Test
    @DisplayName("Moet succesvol opgeslagen en gevonden worden met geldige SKN en BSN.")
    void shouldSaveAndBeFoundWithValidData() {
        final NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);
        repository.save(persoon);
        assertThat(repository.findBySkn(skn)).isPresent();
        assertThat(repository.findByBsn(bsn)).isPresent();
    }

    @Test
    @DisplayName("Mag geen twee instanties van NatuurlijkPersoon met hetzelfde SKN opslaan.")
    void shouldThrowIfSknIsTheSame() {
        final Burgerservicenummer bsn2 = new Burgerservicenummer("123456783");
        final NatuurlijkPersoon persoon1 = new NatuurlijkPersoon(skn, bsn);
        final NatuurlijkPersoon persoon2 = new NatuurlijkPersoon(skn, bsn2);

        repository.save(persoon1);

        assertThatThrownBy(() -> {
            repository.save(persoon2);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Mag geen twee instanties van NatuurlijkPersoon met hetzelfde BSN opslaan.")
    void shouldThrowIfBsnIsTheSame() {
        final Strafrechtketennummer skn2 = new Strafrechtketennummer("SKN-2026-123457");
        final NatuurlijkPersoon persoon1 = new NatuurlijkPersoon(skn, bsn);
        final NatuurlijkPersoon persoon2 = new NatuurlijkPersoon(skn2, bsn);

        repository.save(persoon1);

        assertThatThrownBy(() -> {
            repository.save(persoon2);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
