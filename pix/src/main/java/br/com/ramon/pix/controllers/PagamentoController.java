package br.com.ramon.pix.controllers;

import br.com.ramon.pix.models.entities.Pagamento;
import br.com.ramon.pix.models.repositories.PagamentoRepository;
import br.com.ramon.pix.services.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Pagamento> criarPagamento(@Valid Pagamento pagamento) {
        Pagamento novoPagamento = pagamentoService.salvarPagamento(pagamento);
        return ResponseEntity.ok(novoPagamento);
    }

    @GetMapping
    public Iterable<Pagamento> listaPagamento(){
        return pagamentoRepository.findAll();
    }

    @GetMapping(path="/{id}")
    public Optional<Pagamento> obterPagamentoId(@PathVariable UUID id){
        return pagamentoRepository.findPagamentoById(id);
    }

    @DeleteMapping(path="/{id}")
    public void excluirPagamento(@PathVariable UUID id){
        pagamentoService.deletePagamento(id);
    }

    @PutMapping(path="/update")
    public ResponseEntity<Pagamento> updatePagamento(@Valid @RequestBody Pagamento pagamento){
        Pagamento atualizado = pagamentoService.updatePagamento(pagamento);
        return ResponseEntity.ok(atualizado);
    }
}
