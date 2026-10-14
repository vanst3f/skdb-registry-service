package nl.justid.skdb.infrastructure.persistence.jpa;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "persons")
public class PersoonJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skn", nullable = false, unique = true)
    private String strafrechtketennummer;

    @Column(name = "bsn", nullable = false, unique = true)
    private String burgerservicenummer;

    protected PersoonJpaEntity() {}

    public PersoonJpaEntity(Long id, String strafrechtketennummer, String burgerservicenummer) {
        this.id = id;
        this.strafrechtketennummer = strafrechtketennummer;
        this.burgerservicenummer = burgerservicenummer;
    }

    public PersoonJpaEntity(String strafrechtketennummer, String burgerservicenummer) {
        this.strafrechtketennummer = strafrechtketennummer;
        this.burgerservicenummer = burgerservicenummer;
    }

    public String getStrafrechtketennummer() {
        return this.strafrechtketennummer;
    }

    public String getBurgerservicenummer() {
        return this.burgerservicenummer;
    }
}
