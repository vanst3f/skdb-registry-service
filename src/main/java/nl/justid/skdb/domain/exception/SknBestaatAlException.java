package nl.justid.skdb.domain.exception;

import nl.justid.skdb.domain.model.Strafrechtketennummer;

public class SknBestaatAlException extends RuntimeException {
    public SknBestaatAlException(Strafrechtketennummer strafrechtketennummer) {
        super("Strafrechtketennummer " + strafrechtketennummer.value() + " bestaat al.");
    }
}
