package br.com.augustaverde.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Guarda quantas árvores o grupo já plantou. A tabela tem uma linha só, sempre com o id {@link #ID_UNICO}.
 */
@Entity
@Table(name = "contador_arvores")
@Getter
@Setter
@NoArgsConstructor
public class ContadorArvores {

    public static final Long ID_UNICO = 1L;

    @Id
    private Long id;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    public ContadorArvores(int quantidade, LocalDateTime atualizadoEm) {
        this.id = ID_UNICO;
        this.quantidade = quantidade;
        this.atualizadoEm = atualizadoEm;
    }
}
