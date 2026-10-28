package br.com.financer.service;

import br.com.financer.dao.CategoriaDAO;
import br.com.financer.model.Categoria;

import java.util.List;

public class CategoriaService {
    private final CategoriaDAO dao = new CategoriaDAO();

    public Categoria cadastrar(String nome) {
        Categoria categoria = new Categoria(nome);
        dao.salvar(categoria.getNome());
        return categoria;
    }

    public void remover(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Selecione uma categoria para remover.");
        }
        dao.remover(nome.trim());
    }

    public List<String> listar() {
        return dao.listar();
    }
}
