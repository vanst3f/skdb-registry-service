package nl.justid.skdb.domain.repository;

import java.util.List;
import java.util.Optional;
import nl.justid.skdb.domain.model.Strafzaak;
import nl.justid.skdb.domain.model.Parketnummer;
import nl.justid.skdb.domain.model.Strafrechtketennummer;

public interface StrafzaakRepository {
    Strafzaak save(Strafzaak strafzaak);
    Optional<Strafzaak> findByParketnummer(Parketnummer parketnummer);
    List<Strafzaak> findBySkn(Strafrechtketennummer strafrechtketennummer);
}
