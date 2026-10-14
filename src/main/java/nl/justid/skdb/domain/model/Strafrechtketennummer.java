package nl.justid.skdb.domain.model;

import java.util.regex.Pattern;

public record Strafrechtketennummer(String value) {
    private static final Pattern SKN_PATTERN = Pattern.compile("^SKN-\\d{4}-\\d{6}$");

    public Strafrechtketennummer {
        if (value == null || !SKN_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Ongeldig SKN-formaat. Verwacht formaat: SKN-JJJJ-XXXXXX");
        }
    }
}
