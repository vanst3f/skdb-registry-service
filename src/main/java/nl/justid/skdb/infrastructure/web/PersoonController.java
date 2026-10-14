package nl.justid.skdb.infrastructure.web;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import nl.justid.skdb.application.service.RegistreerPersoonService;
import nl.justid.skdb.domain.model.Strafrechtketennummer;
import nl.justid.skdb.domain.model.Burgerservicenummer;
import nl.justid.skdb.domain.exception.SknBestaatAlException;
import nl.justid.skdb.domain.exception.BsnBestaatAlException;

@RestController
@RequestMapping("/api/personen")
public class PersoonController {
    private final RegistreerPersoonService registreerPersoonService;

    public PersoonController(RegistreerPersoonService registreerPersoonService) {
        this.registreerPersoonService = registreerPersoonService;
    }

    @GetMapping("/{skn}")
    @ResponseStatus(HttpStatus.OK)
    public void getPersonBySkn(@PathVariable String skn) {
        final Strafrechtketennummer strafrechtketennummer = new Strafrechtketennummer(skn);

        registreerPersoonService.findBySkn(strafrechtketennummer);
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
}
