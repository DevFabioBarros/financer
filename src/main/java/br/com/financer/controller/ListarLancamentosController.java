package br.com.financer.controller;

import br.com.financer.model.Lancamento;
import br.com.financer.service.LancamentoService;

import java.util.List;

public class ListarLancamentosController {
    private final LancamentoService service = new LancamentoService();

    public List<Lancamento> listar() {
        return service.listar();
    }
}
