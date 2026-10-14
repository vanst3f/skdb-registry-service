package nl.justid.skdb.infrastructure.web;

import jakarta.validation.constraints.NotBlank;

public record RegistreerPersoonRequest(@NotBlank String skn, @NotBlank String bsn) {}
