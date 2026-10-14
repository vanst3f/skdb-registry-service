package nl.justid.skdb.application.service;

import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.repository.PersoonRepository;
import nl.justid.skdb.domain.exception.BsnBestaatAlException;
import nl.justid.skdb.domain.exception.SknBestaatAlException;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;

public class RegistreerPersoonService {
    private final PersoonRepository repository;

    public RegistreerPersoonService(PersoonRepository repository) {
        this.repository = repository;
    }

    public NatuurlijkPersoon registreer(Strafrechtketennummer skn, Burgerservicenummer bsn) {
        if (repository.findBySkn(skn).isPresent()) {
            throw new SknBestaatAlException(skn);
        }
        if (repository.findByBsn(bsn).isPresent()) {
            throw new BsnBestaatAlException(bsn);
        }
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);

        return repository.save(persoon);
    }
}
