package br.com.ramon.pix.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.ramon.pix.exception.ResourceNotFoundException;
import br.com.ramon.pix.models.entities.DestinoPagamento;
import br.com.ramon.pix.models.entities.Pagamento;
import br.com.ramon.pix.models.entities.Recorrencia;
import br.com.ramon.pix.models.enums.StatusPagamento;
import br.com.ramon.pix.models.repositories.PagamentoRepository;
import br.com.ramon.pix.validation.ChaveValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class PagamentoService {

    @Autowired
    private final PagamentoRepository pagamentoRepository;

    private final Logger logger = LoggerFactory.getLogger(PagamentoService.class);

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public void definirTipoChavePix(Pagamento pagamento) {
        if (pagamento.getDestino().getChavePix() == null){
            throw new NullPointerException("Chave pix null");
        }else{
            ChaveValidation.inferirTipoChave(pagamento);
        }
    }

    public void definirStatusPix(Pagamento pagamento){
        if(pagamento.getDataInclusao() != null){
            if (pagamento.getDataPagamento().isAfter(pagamento.getDataInclusao())) {
                pagamento.setStatus(StatusPagamento.AGENDADO);
            } else if (pagamento.getDataPagamento().isEqual(pagamento.getDataInclusao()) || pagamento.getDataPagamento().isBefore(pagamento.getDataInclusao())) {
                pagamento.setStatus(StatusPagamento.EFETUADO);
            } else if (pagamento.getDataPagamento().isBefore(pagamento.getDataInclusao())) {
                throw new IllegalArgumentException("A data do pagamento não pode ser no passado.");
            }
        }else{
            if (pagamento.getDataPagamento().isAfter(LocalDate.now())) {
                pagamento.setStatus(StatusPagamento.AGENDADO);
            } else if (pagamento.getDataPagamento().isEqual(LocalDate.now()) || pagamento.getDataPagamento().isBefore(LocalDate.now())) {
                pagamento.setStatus(StatusPagamento.EFETUADO);
            } else if (pagamento.getDataPagamento().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("A data do pagamento não pode ser no passado.");
            }
        }
    }

    public Pagamento salvarPagamento(Pagamento pagamento) {
        pagamento.setDataInclusao(LocalDate.now());
        if (pagamento.getDataPagamento() == null){
            throw new NullPointerException("Data do pagamento null");
        }else if(pagamento.getDataPagamento().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("A data do pagamento deve ser atual ou futura!");
        }else{
            definirStatusPix(pagamento);
        }
        if (pagamento.getDestino().getChavePix() == null){
            throw new NullPointerException("Chave pix null");
        }else{
            definirTipoChavePix(pagamento);
        }
        if (pagamento.getValor() == null) {
            throw new NullPointerException("Valor null");
        }
        List<Pagamento> pagamentoExistente = pagamentoRepository.findByValorAndDataPagamentoAndDestino(pagamento.getValor(), pagamento.getDataPagamento(), pagamento.getDestino());
        if (!pagamentoExistente.isEmpty()) {
            logger.warn("Já existe um pagamento {} com as mesmas condições: valor={}, data={}, destino={}",
                    pagamento.getStatus(),
                    pagamento.getValor(),
                    pagamento.getDataPagamento(),
                    pagamento.getDestino());
        }
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento updatePagamento(UUID id, Pagamento pagamento) {
        Pagamento pagamentoExistente = pagamentoRepository.findPagamentoById(id).orElseThrow(ResourceNotFoundException::new);

        if(pagamento.getStatus() != null){
            pagamentoExistente.setStatus(pagamento.getStatus());
        }else{
            if(pagamento.getDataPagamento() != null && pagamento.getDataInclusao() != null) {
                definirStatusPix(pagamento);
                pagamentoExistente.setStatus(pagamento.getStatus());
            }else if(pagamentoExistente.getDataPagamento() != null && pagamentoExistente.getDataInclusao() != null){
                definirStatusPix(pagamentoExistente);
            }
        }
        if(pagamento.getDataInclusao() != null){
            pagamentoExistente.setDataInclusao(pagamento.getDataInclusao());
        }
        if(pagamento.getDataPagamento() != null){
            if(pagamento.getDataInclusao() != null){
                if(pagamento.getDataPagamento().isBefore(pagamento.getDataInclusao())){
                    throw new IllegalArgumentException("A data do pagamento deve ser atual ou futura em relação a data da inclusão!");
                }
            }else if(pagamento.getDataPagamento().isBefore(pagamentoExistente.getDataInclusao())){
                    throw new IllegalArgumentException("A data do pagamento deve ser atual ou futura em relação a data da inclusão!");
            }else{
                pagamentoExistente.setDataPagamento(pagamento.getDataPagamento());
            }
        }
        if(pagamento.getValor() != null){
            pagamentoExistente.setValor(pagamento.getValor());
        }
        if(pagamento.getDescricao() != null){
            pagamentoExistente.setDescricao(pagamento.getDescricao());
        }
        if(pagamento.getDestino() != null){
            if(pagamentoExistente.getDestino() == null){
                pagamentoExistente.setDestino(new DestinoPagamento());
            }
            DestinoPagamento destinoExistente = pagamentoExistente.getDestino();
            DestinoPagamento destino = pagamento.getDestino();

            if(destino.getChavePix() != null){
                destinoExistente.setChavePix(destino.getChavePix());
                definirTipoChavePix(pagamento);
                destinoExistente.setTipoChavePix(pagamento.getDestino().getTipoChavePix());
            }
            if(destino.getTipoChavePix() != null){
                destinoExistente.setTipoChavePix(destino.getTipoChavePix());
            }
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

    @Transactional
    public void deleteAllPagamentos() {
        pagamentoRepository.deleteAll();
    }

    public void cancelarPagamento(UUID id) {
        Pagamento pagamento = pagamentoRepository.findPagamentoById(id).orElseThrow(ResourceNotFoundException::new);
        pagamento.setStatus(StatusPagamento.CANCELADO);
        pagamentoRepository.save(pagamento);
    }
}
