package br.com.augustaverde.service;

import br.com.augustaverde.model.ImpactoCarbono;
import org.springframework.stereotype.Component;

/**
 * Converte número de árvores em CO2 absorvido.
 *
 * <p>Fator: uma árvore nativa da Mata Atlântica absorve em média 163,14 kg de CO2 equivalente nos
 * primeiros 20 anos. Fonte: estudo do Instituto Totum com a ESALQ/USP e a Fundação SOS Mata Atlântica
 * (2013), feito em plantios de restauração.
 */
@Component
public class CalculadoraCarbono {

    public static final double KG_CO2_POR_ARVORE_EM_20_ANOS = 163.14;
    public static final int ANOS = 20;

    public ImpactoCarbono calcular(int arvores) {
        if (arvores < 0) {
            throw new IllegalArgumentException("O número de árvores não pode ser negativo");
        }
        double kgEm20Anos = arvores * KG_CO2_POR_ARVORE_EM_20_ANOS;
        return new ImpactoCarbono(kgEm20Anos, kgEm20Anos / 1000, kgEm20Anos / ANOS);
    }
}
