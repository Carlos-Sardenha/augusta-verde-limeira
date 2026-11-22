package br.com.augustaverde.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Dados do formulário em que o admin informa quantas árvores já foram plantadas. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoArvoresForm {

    @NotNull(message = "Informe o número de árvores")
    @Min(value = 0, message = "O número de árvores não pode ser negativo")
    @Max(value = 10_000_000, message = "Esse número parece alto demais, confira")
    private Integer quantidade;
}
