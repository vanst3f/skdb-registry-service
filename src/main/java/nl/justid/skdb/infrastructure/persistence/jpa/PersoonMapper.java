package nl.justid.skdb.infrastructure.persistence.jpa;


import org.springframework.stereotype.Component;

import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;

@Component 
public class PersoonMapper {
    public PersoonMapper() {}

    public PersoonJpaEntity toJpaEntity(NatuurlijkPersoon persoon) {
        PersoonJpaEntity entiteit = new PersoonJpaEntity(persoon.getStrafrechtketennummer().value(), persoon.getBurgerservicenummer().value());

        return entiteit;
    }

    public NatuurlijkPersoon toDomain(PersoonJpaEntity entiteit) {
        Strafrechtketennummer skn = new Strafrechtketennummer(entiteit.getStrafrechtketennummer());
        Burgerservicenummer bsn = new Burgerservicenummer(entiteit.getBurgerservicenummer());
        NatuurlijkPersoon persoon = new NatuurlijkPersoon(skn, bsn);

        return persoon;
    }
}
