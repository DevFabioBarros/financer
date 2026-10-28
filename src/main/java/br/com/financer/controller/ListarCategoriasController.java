package br.com.financer.controller;

import br.com.financer.service.CategoriaService;

import java.util.List;

public class ListarCategoriasController {
    private final CategoriaService service = new CategoriaService();

    public List<String> listar() {
        return service.listar();
    }
}
