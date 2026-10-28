package br.com.financer.controller;

import br.com.financer.model.Categoria;
import br.com.financer.service.CategoriaService;

public class CadastrarCategoriaController {
    private final CategoriaService service = new CategoriaService();

    public Categoria cadastrar(String nome) {
        return service.cadastrar(nome);
    }
}
