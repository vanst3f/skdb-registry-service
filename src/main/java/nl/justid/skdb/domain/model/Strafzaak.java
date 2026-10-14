package nl.justid.skdb.domain.model;

import java.util.Objects;

public class Strafzaak {
    private final Parketnummer parketnummer;
    private final Strafrechtketennummer verdachteSkn;
    private ZaakStatus status;

    public Strafzaak(Parketnummer inkomendParketNummer, Strafrechtketennummer inkomendSkn) {
        if (inkomendParketNummer == null) {
            throw new NullPointerException("Parketnummer mag niet leeg zijn.");
        }
        if (inkomendSkn == null) {
            throw new NullPointerException("Strafrechtketennummer mag niet leeg zijn.");
        }

        this.parketnummer = inkomendParketNummer;
        this.verdachteSkn = inkomendSkn;
        this.status = ZaakStatus.IN_ONDERZOEK;
    }

    public Parketnummer getParketnummer() {
        return this.parketnummer;
    }

    public Strafrechtketennummer getStrafrechtketennummer() {
        return this.verdachteSkn;
    }

    public ZaakStatus getStatus() {
        return this.status;
    }

    public void seponeer() {
        if (this.status == ZaakStatus.GEDAGVAARD || this.status == ZaakStatus.GESEPONEERD) {
            throw new IllegalStateException("Strafzaak die al gedagvaard is mag niet geseponeerd of nogmaals gedagvaard worden.");
        }
        this.status = ZaakStatus.GESEPONEERD;
    }

    public void dagvaard() {
        if (this.status == ZaakStatus.GESEPONEERD || this.status == ZaakStatus.GEDAGVAARD) {
            throw new IllegalStateException("Strafzaak die al geseponeerd is mag niet gedagvaard of nogmaals geseponeerd worden.");
        }
        this.status = ZaakStatus.GEDAGVAARD;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Strafzaak that)) return false; // vergelijkt het inkomende object en maakt een nieuwe Strafzaak that
        return this.parketnummer.equals(that.parketnummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.parketnummer);
    }
}
