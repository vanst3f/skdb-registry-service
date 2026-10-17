package nl.justid.skdb.infrastructure.web;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import nl.justid.skdb.application.service.RegistreerPersoonService;
import nl.justid.skdb.application.service.ZoekPersoonService;
import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.model.NatuurlijkPersoon;
import nl.justid.skdb.domain.exception.SknBestaatAlException;
import nl.justid.skdb.domain.exception.BsnBestaatAlException;
import nl.justid.skdb.domain.exception.PersoonNietGevondenException;

@RestController
@RequestMapping("/api/personen")
public class PersoonController {
    private final RegistreerPersoonService registreerPersoonService;
    private final ZoekPersoonService zoekPersoonService;

    public PersoonController(RegistreerPersoonService registreerPersoonService, ZoekPersoonService zoekPersoonService) {
        this.registreerPersoonService = registreerPersoonService;
        this.zoekPersoonService = zoekPersoonService;
    }

    @GetMapping("{skn}")
    @ResponseStatus(HttpStatus.OK)
    public ZoekPersoonResponse getBySkn(@PathVariable String skn) {
        final Strafrechtketennummer strafrechtketennummer = new Strafrechtketennummer(skn);
        final NatuurlijkPersoon persoon = zoekPersoonService.findBySkn(strafrechtketennummer).orElseThrow(() -> new PersoonNietGevondenException(strafrechtketennummer));

        return new ZoekPersoonResponse(persoon);
    }

    @GetMapping(params = "bsn")
    @ResponseStatus(HttpStatus.OK)
    public ZoekPersoonResponse getByBsn(@RequestParam String bsn) {
        final Burgerservicenummer burgerservicenummer = new Burgerservicenummer(bsn);
        final NatuurlijkPersoon persoon = zoekPersoonService.findByBsn(burgerservicenummer).orElseThrow(() -> new PersoonNietGevondenException(burgerservicenummer));

        return new ZoekPersoonResponse(persoon);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void POST(@Valid @RequestBody RegistreerPersoonRequest request) {
        final Strafrechtketennummer strafrechtketennummer = new Strafrechtketennummer(request.skn());
        final Burgerservicenummer burgerservicenummer = new Burgerservicenummer(request.bsn());

        registreerPersoonService.registreer(strafrechtketennummer, burgerservicenummer);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public void handleIllegalArgumentException(IllegalArgumentException exception) {}

    @ExceptionHandler({SknBestaatAlException.class, BsnBestaatAlException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleDuplicateDataException() {}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePersoonNietGevondenException(PersoonNietGevondenException exception) {}
}
