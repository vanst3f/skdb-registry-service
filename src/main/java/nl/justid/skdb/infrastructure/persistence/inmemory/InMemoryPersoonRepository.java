package nl.justid.skdb.infrastructure.persistence.inmemory;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import nl.justid.skdb.domain.repository.PersoonRepository;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;
import nl.justid.skdb.domain.model.Strafrechtketennummer;


public class InMemoryPersoonRepository implements PersoonRepository {
    private final Map<Strafrechtketennummer, NatuurlijkPersoon> repository = new HashMap<>();

    public InMemoryPersoonRepository() {}

    public NatuurlijkPersoon save(NatuurlijkPersoon persoon) {
        repository.put(persoon.getStrafrechtketennummer(), persoon);
        return persoon;
    }

    public Optional<NatuurlijkPersoon> findBySkn(Strafrechtketennummer strafrechtketennummer) {
        return Optional.ofNullable(repository.get(strafrechtketennummer));
    }

    public Optional<NatuurlijkPersoon> findByBsn(Burgerservicenummer burgerservicenummer) {
        for (NatuurlijkPersoon persoon : repository.values()) {
            if (persoon.getBurgerservicenummer().equals(burgerservicenummer)) {
                return Optional.of(persoon);
            };
        }
        return Optional.empty();
    }
}
