package br.com.ramon.pix.services;

import br.com.ramon.pix.exception.ResourceNotFoundException;
import br.com.ramon.pix.models.entities.Pagamento;
import br.com.ramon.pix.models.enums.TipoChave;
import br.com.ramon.pix.models.repositories.PagamentoRepository;
import br.com.ramon.pix.validation.ChaveValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    public Pagamento salvarPagamento(Pagamento pagamento) {
        TipoChave tipoChave = ChaveValidation.inferirTipoChave(pagamento.getChavePix());

        pagamento.setChavePix(pagamento.getChavePix());
        pagamento.setTipoChave(tipoChave);

        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public Pagamento updatePagamento(   Pagamento pagamento) {
        pagamentoRepository.findPagamentoById(pagamento.getId()).orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));
        TipoChave tipoChave = ChaveValidation.inferirTipoChave(pagamento.getChavePix());

        pagamento.setChavePix(pagamento.getChavePix());
        pagamento.setTipoChave(tipoChave);

        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public void deletePagamento(UUID id) {
        pagamentoRepository.deleteByIdCustom(id);
    }
}
