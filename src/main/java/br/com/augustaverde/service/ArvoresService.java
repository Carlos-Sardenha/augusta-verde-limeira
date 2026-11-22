package br.com.augustaverde.service;

import br.com.augustaverde.model.ContadorArvores;
import br.com.augustaverde.repository.ContadorArvoresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArvoresService {

    private final ContadorArvoresRepository repository;
    private final Clock clock;

    /** Quantas árvores já foram plantadas. Antes do primeiro cadastro do admin, é zero. */
    @Transactional(readOnly = true)
    public int quantidadeAtual() {
        return repository.findById(ContadorArvores.ID_UNICO)
                .map(ContadorArvores::getQuantidade)
                .orElse(0);
    }

    @Transactional
    public void atualizar(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("O número de árvores não pode ser negativo");
        }
        LocalDateTime agora = LocalDateTime.now(clock);
        ContadorArvores contador = repository.findById(ContadorArvores.ID_UNICO)
                .orElseGet(() -> new ContadorArvores(quantidade, agora));
        contador.setQuantidade(quantidade);
        contador.setAtualizadoEm(agora);
        repository.save(contador);
    }
}
