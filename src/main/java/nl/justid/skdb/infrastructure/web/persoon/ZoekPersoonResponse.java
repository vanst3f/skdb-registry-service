package nl.justid.skdb.infrastructure.web.persoon;

import nl.justid.skdb.domain.model.NatuurlijkPersoon;

public record ZoekPersoonResponse(String skn, String bsn) {
    public ZoekPersoonResponse(NatuurlijkPersoon persoon) {
        this(
            persoon.getStrafrechtketennummer().value(),
            persoon.getBurgerservicenummer().value()
        );
    }
}
