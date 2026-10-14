package nl.justid.skdb.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataPersoonRepository extends JpaRepository<PersoonJpaEntity, Long> {
    Optional<PersoonJpaEntity> findByStrafrechtketennummer(String strafrechtketennummer);
    Optional<PersoonJpaEntity> findByBurgerservicenummer(String burgerservicenummer);
}
