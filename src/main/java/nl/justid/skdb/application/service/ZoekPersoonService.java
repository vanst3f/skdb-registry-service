package nl.justid.skdb.application.service;

import nl.justid.skdb.domain.repository.PersoonRepository;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;
import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;

import java.util.Optional;

public class ZoekPersoonService {
    private final PersoonRepository repository;

    public ZoekPersoonService(PersoonRepository repository) {
        this.repository = repository;
    }

    public Optional<NatuurlijkPersoon> findBySkn(Strafrechtketennummer strafrechtketennummer) {
        return repository.findBySkn(strafrechtketennummer);
    }

    public Optional<NatuurlijkPersoon> findByBsn(Burgerservicenummer burgerservicenummer) {
        return repository.findByBsn(burgerservicenummer);
    }
}
