package nl.justid.skdb.infrastructure.web.persoon;

import nl.justid.skdb.domain.model.NatuurlijkPersoon;

public record RegistreerPersoonResponse(String skn, String bsn) {
    public RegistreerPersoonResponse(NatuurlijkPersoon persoon) {
        this(
            persoon.getStrafrechtketennummer().value(),
            persoon.getBurgerservicenummer().value()
        );
    }
}
