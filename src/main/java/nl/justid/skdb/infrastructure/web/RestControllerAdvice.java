package nl.justid.skdb.infrastructure.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;

import nl.justid.skdb.domain.exception.SknBestaatAlException;
import nl.justid.skdb.domain.exception.BsnBestaatAlException;
import nl.justid.skdb.domain.exception.PersoonNietGevondenException;

@ControllerAdvice 
public class RestControllerAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ProblemDetailsResponse> handlePersoonNietGevondenException(PersoonNietGevondenException exception, HttpServletRequest request) {
        ProblemDetailsResponse error = new ProblemDetailsResponse(
            URI.create("https://example.com"),
            "Not Found",
            HttpStatus.NOT_FOUND.value(),
            exception.getMessage(),
            URI.create(request.getRequestURI()),
            "not-found"
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ProblemDetailsResponse> handleSknBestaatAlException(SknBestaatAlException exception, HttpServletRequest request) {
        ProblemDetailsResponse error = new ProblemDetailsResponse(
            URI.create("https://example.com"),
            "Already Exists",
            HttpStatus.CONFLICT.value(),
            exception.getMessage(),
            URI.create(request.getRequestURI()),
            "skn-already-exists"
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ProblemDetailsResponse> handleBsnBestaatAlException(BsnBestaatAlException exception, HttpServletRequest request) {
        ProblemDetailsResponse error = new ProblemDetailsResponse(
            URI.create("https://example.com"),
            "Already Exists",
            HttpStatus.CONFLICT.value(),
            exception.getMessage(),
            URI.create(request.getRequestURI()),
            "bsn-already-exists"
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ResponseEntity<ProblemDetailsResponse> handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        ProblemDetailsResponse error = new ProblemDetailsResponse(
            URI.create("https://example.com"),
            "Validation Failed",
            HttpStatus.UNPROCESSABLE_CONTENT.value(),
            exception.getMessage(),
            URI.create(request.getRequestURI()),
            "validation-failed"
        );

        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
