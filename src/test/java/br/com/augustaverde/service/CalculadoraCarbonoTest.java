package br.com.augustaverde.service;

import br.com.augustaverde.model.ImpactoCarbono;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class CalculadoraCarbonoTest {

    private final CalculadoraCarbono calculadora = new CalculadoraCarbono();

    @Test
    void zeroArvoresNaoTiramCarbono() {
        ImpactoCarbono impacto = calculadora.calcular(0);

        assertThat(impacto.kgEm20Anos()).isZero();
        assertThat(impacto.toneladasEm20Anos()).isZero();
        assertThat(impacto.kgPorAno()).isZero();
    }

    @Test
    void umaArvoreTira163KgEm20Anos() {
        ImpactoCarbono impacto = calculadora.calcular(1);

        assertThat(impacto.kgEm20Anos()).isCloseTo(163.14, within(0.001));
        assertThat(impacto.toneladasEm20Anos()).isCloseTo(0.16314, within(0.000001));
        assertThat(impacto.kgPorAno()).isCloseTo(8.157, within(0.001));
    }

    @Test
    void milArvoresTiram163ToneladasEm20Anos() {
        ImpactoCarbono impacto = calculadora.calcular(1000);

        assertThat(impacto.kgEm20Anos()).isCloseTo(163_140, within(0.01));
        assertThat(impacto.toneladasEm20Anos()).isCloseTo(163.14, within(0.001));
        assertThat(impacto.kgPorAno()).isCloseTo(8_157, within(0.01));
    }

    @Test
    void recusaNumeroNegativo() {
        assertThatThrownBy(() -> calculadora.calcular(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
