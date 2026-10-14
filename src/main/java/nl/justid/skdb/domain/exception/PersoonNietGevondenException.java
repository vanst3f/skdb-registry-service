package nl.justid.skdb.domain.exception;

import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.Strafrechtketennummer;

public class PersoonNietGevondenException extends RuntimeException {
    public PersoonNietGevondenException(Strafrechtketennummer strafrechtketennummer) {
        super("NatuurlijkPersoon met " + strafrechtketennummer.value() + " bestaat niet.");
    }
    public PersoonNietGevondenException(Burgerservicenummer burgerservicenummer) {
        super("NatuurlijkPersoon met " + burgerservicenummer.value() + " bestaat niet.");
    }
}
