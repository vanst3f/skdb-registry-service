package nl.justid.skdb.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StrafzaakTest {
    private final Parketnummer parketnummer = new Parketnummer("09/123456-26");
    private final Strafrechtketennummer verdachteSkn = new Strafrechtketennummer("SKN-2026-123456");

    @Test
    @DisplayName("Strafzaak met geldig parketnummer en SKN moet succesvol gecreëerd worden.")
    void shouldCreateWithValidData() {
        final Strafzaak strafzaak = new Strafzaak(parketnummer, verdachteSkn);

        assertThat(strafzaak).isNotNull();
        assertThat(strafzaak.getParketnummer()).isEqualTo(parketnummer);
        assertThat(strafzaak.getStrafrechtketennummer()).isEqualTo(verdachteSkn);
        assertThat(strafzaak.getStatus()).isEqualTo(ZaakStatus.IN_ONDERZOEK);
    }

    @Test
    @DisplayName("Strafzaak moet geseponeerd kunnen worden.")
    void shouldHaveRightStatusIfDismissed() {
        final Strafzaak strafzaak = new Strafzaak(parketnummer, verdachteSkn);
        strafzaak.seponeer();

        assertThat(strafzaak.getStatus()).isEqualTo(ZaakStatus.GESEPONEERD);
    }

    @Test
    @DisplayName("Strafzaak moet gedagvaard kunnen worden.")
    void shouldHaveRightStatusIfSummoned() {
        final Strafzaak strafzaak = new Strafzaak(parketnummer, verdachteSkn);
        strafzaak.dagvaard();

        assertThat(strafzaak.getStatus()).isEqualTo(ZaakStatus.GEDAGVAARD);
    }

    @Test
    @DisplayName("Twee strafzaken met hetzelfde parketnummer moeten gelijk zijn aan elkaar.")
    void shouldBeEqualIfParketnumIsTheSame() {
        final Strafrechtketennummer verdachteSkn2 = new Strafrechtketennummer("SKN-2026-123457");

        final Strafzaak strafzaak1 = new Strafzaak(parketnummer, verdachteSkn);
        final Strafzaak strafzaak2 = new Strafzaak(parketnummer, verdachteSkn2);

        assertThat(strafzaak1.hashCode()).isEqualTo(strafzaak2.hashCode());
    }

    @Test
    @DisplayName("Strafzaak mag niet gecreëerd worden zonder parketnummer.")
    void shouldThrowIfParketnumIsNull() {
        assertThatThrownBy(() -> {
            new Strafzaak(null, verdachteSkn);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Strafzaak mag niet gecreëerd worden zonder strafrechtketennummer.")
    void shouldThrowIfSknIsNull() {
        assertThatThrownBy(() -> {
            new Strafzaak(parketnummer, null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Strafzaak die al gedagvaard is mag niet geseponeerd of nogmaals gedagvaard worden.")
    void shouldThrowIfAlreadySummoned() {
        final Strafzaak strafzaak = new Strafzaak(parketnummer, verdachteSkn);
        strafzaak.dagvaard();

        assertThatThrownBy(() -> {
            strafzaak.seponeer();
        }).isInstanceOf(IllegalStateException.class);

        assertThatThrownBy(() -> {
            strafzaak.dagvaard();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Strafzaak die al geseponeerd is mag niet gedagvaard of nogmaals geseponeerd worden.")
    void shouldThrowIfAlreadyDismissed() {
        final Strafzaak strafzaak = new Strafzaak(parketnummer, verdachteSkn);
        strafzaak.seponeer();

        assertThatThrownBy(() -> {
            strafzaak.dagvaard();
        }).isInstanceOf(IllegalStateException.class);

        assertThatThrownBy(() -> {
            strafzaak.seponeer();
        }).isInstanceOf(IllegalStateException.class);
    }
}
