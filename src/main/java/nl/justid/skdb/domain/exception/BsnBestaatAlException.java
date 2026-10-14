package nl.justid.skdb.domain.exception;

import nl.justid.skdb.domain.model.Burgerservicenummer;

public class BsnBestaatAlException extends RuntimeException {
    public BsnBestaatAlException(Burgerservicenummer burgerservicenummer) {
        super("Burgerservicenummer " + burgerservicenummer.value() + " bestaat al.");
    }
}
