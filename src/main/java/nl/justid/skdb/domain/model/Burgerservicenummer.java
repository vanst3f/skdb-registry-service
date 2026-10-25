package nl.justid.skdb.domain.model;

import java.util.regex.Pattern;

public record Burgerservicenummer(String value) {
    private static final Pattern BSN_PATTERN = Pattern.compile("^\\d{9}$");

    public Burgerservicenummer {
        if (value == null || !BSN_PATTERN.matcher(value).matches() || (value.equals("123456789") || value.equals("000000000"))) {
            throw new IllegalArgumentException("Ongeldige BSN-formaat.");
        }

        int num = 0;

        for(int i = 0; i < (value.length() - 1); i++) {
            num += Character.getNumericValue(value.charAt(i)) * (9 - i);
        }
        num += Character.getNumericValue(value.charAt(8)) * -1;

        if (!(num > 0) || !(num % 11 == 0)) {
            throw new IllegalArgumentException("Waarde niet geslaagd voor 11-proef.");
        }
    }
}