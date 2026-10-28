package br.com.financer.controller;

import br.com.financer.model.Lancamento;
import br.com.financer.model.TipoLancamento;
import br.com.financer.service.LancamentoService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EditarLancamentoController {
    private final LancamentoService service = new LancamentoService();

    public Lancamento editar(Long id, TipoLancamento tipo, String descricao, BigDecimal valor, LocalDate data, String categoria, String comentario) {
        return service.editar(id, tipo, descricao, valor, data, categoria, comentario);
    }
}
