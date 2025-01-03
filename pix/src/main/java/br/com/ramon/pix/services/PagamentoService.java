package br.com.ramon.pix.services;

import br.com.ramon.pix.exception.ResourceNotFoundException;
import br.com.ramon.pix.models.entities.Pagamento;
import br.com.ramon.pix.models.entities.Recorrencia;
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

    public void definirTipoChavePix(Pagamento pagamento){
        if (pagamento.getChavePix() == null){
            throw new NullPointerException("Chave pix null");
        }else{
            TipoChave tipoChave = ChaveValidation.inferirTipoChave(pagamento.getChavePix());
            pagamento.setTipoChavePix(tipoChave);
            pagamento.setChavePix(pagamento.getChavePix());
        }
    }

    public Pagamento salvarPagamento(Pagamento pagamento) {
        definirTipoChavePix(pagamento);
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento updatePagamento(Pagamento pagamento) {
        Pagamento pagamentoExistente = pagamentoRepository.findPagamentoById(pagamento.getId()).orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado!"));

        if(pagamento.getDataInclusao() != null){
            pagamentoExistente.setDataInclusao(pagamento.getDataInclusao());
        }
        if(pagamento.getDataPagamento() != null){
            pagamentoExistente.setDataPagamento(pagamento.getDataPagamento());
        }
        if(pagamento.getValor() != null){
            pagamentoExistente.setValor(pagamento.getValor());
        }
        if(pagamento.getDescricao() != null){
            pagamentoExistente.setDescricao(pagamento.getDescricao());
        }
        if(pagamento.getChavePix() != null) {
            pagamentoExistente.setChavePix(pagamento.getChavePix());
            definirTipoChavePix(pagamento);
            pagamentoExistente.setTipoChavePix(pagamento.getTipoChavePix());
        }
        if(pagamento.getTipoChavePix() != null){
            pagamentoExistente.setTipoChavePix(pagamento.getTipoChavePix());
        }
        if (pagamento.getRecorrencia() != null) {
            if (pagamentoExistente.getRecorrencia() == null) {
                pagamentoExistente.setRecorrencia(new Recorrencia());
            }
            Recorrencia recorrenciaExistente = pagamentoExistente.getRecorrencia();
            Recorrencia recorrencia = pagamento.getRecorrencia();

            if (recorrencia.getFrequencia() != null) {
                recorrenciaExistente.setFrequencia(recorrencia.getFrequencia());
            }
            if (recorrencia.getDataFinal() != null) {
                recorrenciaExistente.setDataFinal(recorrencia.getDataFinal());
            }
        }
        return pagamentoRepository.save(pagamentoExistente);
    }

    @Transactional
    public void deletePagamento(UUID id) {
        pagamentoRepository.deleteByIdCustom(id);
    }
}
