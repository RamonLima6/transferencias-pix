package br.com.ramon.pix.models.repositories;

import br.com.ramon.pix.models.entities.Pagamento;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PagamentoRepository extends PagingAndSortingRepository<Pagamento, Integer>, CrudRepository<Pagamento, Integer> {

    @Query("SELECT p FROM Pagamento p WHERE p.id = :id")
    Optional<Pagamento> findPagamentoById(@Param("id") UUID id);

    @Modifying
    @Query("DELETE FROM Pagamento p WHERE p.id = :id")
    void deleteByIdCustom(@Param("id") UUID id);
}