package br.com.ramon.pix.controllers;

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

    @PostMapping
    public ResponseEntity<Pagamento> criarPagamento(@RequestBody Pagamento pagamento) {
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

    @PatchMapping(path="/update")
    public Pagamento updatePagamento(@RequestBody Pagamento pagamento){
        return pagamentoService.updatePagamento(pagamento);
    }
}
