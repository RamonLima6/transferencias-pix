package br.com.ramon.pix.controllers;

import br.com.ramon.pix.exception.ResourceNotFoundException;
import br.com.ramon.pix.models.entities.Pagamento;
import br.com.ramon.pix.models.repositories.PagamentoRepository;
import br.com.ramon.pix.services.PagamentoService;
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

    @PostMapping("/criar")
    public ResponseEntity<Pagamento> criarPagamento(@RequestBody Pagamento pagamento) {
        Pagamento novoPagamento = pagamentoService.salvarPagamento(pagamento);
        return ResponseEntity.ok(novoPagamento);
    }

    @GetMapping
    public Iterable<Pagamento> listaPagamento(){
        return pagamentoRepository.findAll();
    }

    @GetMapping(path="/consultar/{id}")
    public Optional<Pagamento> obterPagamentoId(@PathVariable UUID id){
        return pagamentoRepository.findPagamentoById(id);
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<String> deletarPagamento(@PathVariable UUID id) {
        pagamentoService.deletePagamento(id);
        return ResponseEntity.ok("Pagamento com ID '" + id + "' foi removido com sucesso.");
    }

    @PatchMapping(path="/update/{id}")
    public Pagamento updatePagamento(@PathVariable UUID id) {
        Pagamento pagamento = pagamentoRepository.findPagamentoById(id).orElseThrow(ResourceNotFoundException::new);
        return pagamentoService.updatePagamento(pagamento);
    }

    @PatchMapping(path="/cancelar/{id}")
    public ResponseEntity<String> cancelarPagamento(@PathVariable UUID id) {
        pagamentoService.cancelarPagamento(id);
        return ResponseEntity.ok("Pagamento com ID " +id+ " cancelado com sucesso.");
    }
}
