package nl.justid.skdb.domain.model;

import java.util.Objects;

public class NatuurlijkPersoon {
    private final Strafrechtketennummer strafrechtketennummer;
    private final Burgerservicenummer burgerservicenummer;

    public NatuurlijkPersoon(Strafrechtketennummer inkomendSkn, Burgerservicenummer inkomendBsn) {
        if (inkomendSkn == null) {
            throw new NullPointerException("Strafrechtketennummer mag niet leeg zijn.");
        }

        if (inkomendBsn == null) {
            throw new NullPointerException("Burgerservicenummer mag niet leeg zijn.");
        }

        this.strafrechtketennummer = inkomendSkn;
        this.burgerservicenummer = inkomendBsn;
    }

    public Strafrechtketennummer getStrafrechtketennummer() {
        return strafrechtketennummer;
    }

    public Burgerservicenummer getBurgerservicenummer() {
        return burgerservicenummer;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NatuurlijkPersoon that)) return false; // als o een instantie is van NatuurlijkPersoon, bindt de waarde aan de variabele that
        return this.strafrechtketennummer.equals(that.strafrechtketennummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.strafrechtketennummer);
    }
}