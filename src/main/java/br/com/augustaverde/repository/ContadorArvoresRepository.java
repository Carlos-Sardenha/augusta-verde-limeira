package br.com.augustaverde.repository;

import br.com.augustaverde.model.ContadorArvores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContadorArvoresRepository extends JpaRepository<ContadorArvores, Long> {
}
