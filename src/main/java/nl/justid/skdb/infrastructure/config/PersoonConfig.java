package nl.justid.skdb.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import nl.justid.skdb.application.service.RegistreerPersoonService;
import nl.justid.skdb.application.service.ZoekPersoonService;
import nl.justid.skdb.domain.repository.PersoonRepository;


@Configuration 
public class PersoonConfig {
    @Bean
    public RegistreerPersoonService registreerPersoonService(PersoonRepository repository) {
        final RegistreerPersoonService service = new RegistreerPersoonService(repository);

        return service;
    }

    @Bean 
    public ZoekPersoonService zoekPersoonService(PersoonRepository repository) {
        final ZoekPersoonService service = new ZoekPersoonService(repository);

        return service;
    }
}
