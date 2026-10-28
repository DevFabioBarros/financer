package br.com.financer.controller;

import br.com.financer.service.LancamentoService;

public class RemoverLancamentoController {
    private final LancamentoService service = new LancamentoService();

    public void remover(Long id) {
        service.remover(id);
    }
}
