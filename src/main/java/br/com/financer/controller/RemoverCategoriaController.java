package br.com.financer.controller;

import br.com.financer.service.CategoriaService;

public class RemoverCategoriaController {
    private final CategoriaService service = new CategoriaService();

    public void remover(String nome) {
        service.remover(nome);
    }
}
