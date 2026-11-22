package br.com.augustaverde.model;

/**
 * Quanto CO2 as árvores plantadas tiram do ar.
 *
 * @param kgEm20Anos      total de CO2 (em kg) que as árvores absorvem nos primeiros 20 anos
 * @param toneladasEm20Anos o mesmo total, em toneladas
 * @param kgPorAno        média anual de CO2 (em kg) ao longo desses 20 anos
 */
public record ImpactoCarbono(double kgEm20Anos, double toneladasEm20Anos, double kgPorAno) {
}
