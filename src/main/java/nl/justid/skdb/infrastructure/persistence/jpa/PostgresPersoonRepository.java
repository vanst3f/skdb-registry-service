package nl.justid.skdb.infrastructure.persistence.jpa;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;

import nl.justid.skdb.domain.repository.PersoonRepository;

@Repository
public class PostgresPersoonRepository implements PersoonRepository {
    private final SpringDataPersoonRepository springDataPersoonRepository;
    private final PersoonMapper mapper;

    public PostgresPersoonRepository(SpringDataPersoonRepository repository, PersoonMapper mapper) {
        this.springDataPersoonRepository = repository;
        this.mapper = mapper;
    }

    public NatuurlijkPersoon save(NatuurlijkPersoon persoon) {
        PersoonJpaEntity entiteit = mapper.toJpaEntity(persoon);
        springDataPersoonRepository.save(entiteit);
        return mapper.toDomain(entiteit);
    }

    public Optional<NatuurlijkPersoon> findBySkn(Strafrechtketennummer strafrechtketennummer) {
        Optional<PersoonJpaEntity> entiteit = springDataPersoonRepository.findByStrafrechtketennummer(strafrechtketennummer.value());
        return entiteit.map(mapper::toDomain);
    }

    public Optional<NatuurlijkPersoon> findByBsn(Burgerservicenummer burgerservicenummer) {
        Optional<PersoonJpaEntity> entiteit = springDataPersoonRepository.findByBurgerservicenummer(burgerservicenummer.value());
        return entiteit.map(mapper::toDomain);
    }
}
