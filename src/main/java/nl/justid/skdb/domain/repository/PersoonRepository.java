package nl.justid.skdb.domain.repository;

import java.util.Optional;

import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;
import nl.justid.skdb.domain.model.Strafrechtketennummer;

public interface PersoonRepository {
    NatuurlijkPersoon save(NatuurlijkPersoon persoon);
    Optional<NatuurlijkPersoon> findBySkn(Strafrechtketennummer strafrechtketennummer);
    Optional<NatuurlijkPersoon> findByBsn(Burgerservicenummer burgerservicenummer);
}
