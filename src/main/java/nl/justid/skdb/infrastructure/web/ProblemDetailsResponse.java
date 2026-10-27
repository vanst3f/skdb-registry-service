package nl.justid.skdb.infrastructure.web;

import java.net.URI;

public record ProblemDetailsResponse(
    URI type,
    String title,
    int status,
    String detail,
    URI instance,
    String errorKey
) {}
