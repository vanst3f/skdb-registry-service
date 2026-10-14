package nl.justid.skdb.domain.model;

import java.util.regex.Pattern;

public record Parketnummer(String value) {
    private static final Pattern PN_PATTERN = Pattern.compile("^\\d{2}/\\d{6}-\\d{2}$");

    public Parketnummer {
        if (value == null || !PN_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Ongeldig PN-formaat.");
        }
    }
}
