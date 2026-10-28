package br.com.financer.controller;

import br.com.financer.model.Lancamento;
import br.com.financer.service.LancamentoService;

import java.util.List;

public class FiltrarLancamentosPorPeriodoController {
    private final LancamentoService service = new LancamentoService();

    public List<Lancamento> filtrar(int mes, int ano) {
        return service.filtrarPorPeriodo(mes, ano);
    }
}
