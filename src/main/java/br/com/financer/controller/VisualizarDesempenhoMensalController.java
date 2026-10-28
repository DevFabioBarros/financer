package br.com.financer.controller;

import br.com.financer.model.DesempenhoMensal;
import br.com.financer.service.RelatorioService;

import java.util.List;

public class VisualizarDesempenhoMensalController {
    private final RelatorioService service = new RelatorioService();

    public List<DesempenhoMensal> visualizar() {
        return service.visualizarDesempenhoMensal();
    }
}
