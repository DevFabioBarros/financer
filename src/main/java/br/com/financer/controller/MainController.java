package br.com.financer.controller;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.financer.model.DesempenhoMensal;
import br.com.financer.model.Lancamento;
import br.com.financer.model.TipoLancamento;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

@SuppressWarnings("unchecked")
public class MainController {
    private static final String[] MESES = {
            "Janeiro", "Fevereiro", "Marco", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    private final CadastrarReceitaController cadastrarReceitaController = new CadastrarReceitaController();
    private final CadastrarDespesaController cadastrarDespesaController = new CadastrarDespesaController();
    private final CadastrarInvestimentoController cadastrarInvestimentoController = new CadastrarInvestimentoController();
    private final CadastrarCategoriaController cadastrarCategoriaController = new CadastrarCategoriaController();
    private final RemoverCategoriaController removerCategoriaController = new RemoverCategoriaController();
    private final ListarCategoriasController listarCategoriasController = new ListarCategoriasController();
    private final ListarLancamentosController listarLancamentosController = new ListarLancamentosController();
    private final FiltrarLancamentosPorPeriodoController filtrarLancamentosPorPeriodoController = new FiltrarLancamentosPorPeriodoController();
    private final RemoverLancamentoController removerLancamentoController = new RemoverLancamentoController();
    private final EditarLancamentoController editarLancamentoController = new EditarLancamentoController();
    private final VisualizarDesempenhoMensalController visualizarDesempenhoMensalController = new VisualizarDesempenhoMensalController();

    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(java.util.Locale.of("pt", "BR"));
    private boolean temaEscuro = true;
    private Long lancamentoEmEdicaoId;

    @FXML private ComboBox<String> cbMesFiltro;
    @FXML private ComboBox<Integer> cbAnoFiltro;
    @FXML private Label lblTotalReceitas;
    @FXML private Label lblTotalDespesas;
    @FXML private Label lblTotalInvestido;
    @FXML private Label lblSaldoAtual;
    @FXML private TableView<Lancamento> tabelaLancamentos;
    @FXML private TableColumn<Lancamento, LocalDate> colunaData;
    @FXML private TableColumn<Lancamento, String> colunaCategoria;
    @FXML private TableColumn<Lancamento, String> colunaTipo;
    @FXML private TableColumn<Lancamento, String> colunaValor;
    @FXML private TableColumn<Lancamento, String> colunaDescricao;
    @FXML private TableColumn<Lancamento, Void> colunaAcoes;
    @FXML private BorderPane raiz;
    @FXML private HBox conteudoPrincipal;
    @FXML private VBox painelEsquerdo;
    @FXML private VBox painelDireito;
    @FXML private Button botaoTema;
    @FXML private Label lblTituloFormulario;
    @FXML private RadioButton rbReceita;
    @FXML private RadioButton rbDespesa;
    @FXML private RadioButton rbInvestimento;
    @FXML private TextField txtValor;
    @FXML private DatePicker seletorData;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private TextArea txtDescricao;
    @FXML private Button botaoSalvar;
    @FXML private Button botaoCancelar;
    @FXML private Button botaoTabela;
    @FXML private Button botaoGraficos;
    @FXML private TabPane painelGraficos;
    @FXML private PieChart graficoDespesasPorCategoria;
    @FXML private PieChart graficoReceitasPorCategoria;
    @FXML private BarChart<String, Number> graficoFluxoAnual;
    @FXML private LineChart<String, Number> graficoDesempenhoMensal;

    @FXML
    public void initialize() {
        configurarTabela();
        configurarFiltros();
        configurarSelecaoDeCategorias();
        carregarCategorias();
        seletorData.setValue(LocalDate.now());
        botaoCancelar.setManaged(false);

        cbMesFiltro.getSelectionModel().selectedItemProperty().addListener((obs, valorAntigo, valorNovo) -> carregarDados());
        cbAnoFiltro.getSelectionModel().selectedItemProperty().addListener((obs, valorAntigo, valorNovo) -> carregarDados());
        raiz.widthProperty().addListener((obs, valorAntigo, valorNovo) -> atualizarLayoutResponsivo());
        raiz.heightProperty().addListener((obs, valorAntigo, valorNovo) -> atualizarLayoutResponsivo());

        carregarDados();
        exibirTabela();
        atualizarLayoutResponsivo();
    }

    private void configurarFiltros() {
        cbMesFiltro.getItems().setAll(MESES);
        int anoAtual = LocalDate.now().getYear();
        List<Integer> anos = new ArrayList<>();
        for (int ano = 2000; ano <= anoAtual + 10; ano++) {
            anos.add(ano);
        }
        cbAnoFiltro.getItems().setAll(anos);
        cbMesFiltro.setValue(MESES[LocalDate.now().getMonthValue() - 1]);
        cbAnoFiltro.setValue(anoAtual);
    }

    private void configurarTabela() {
        colunaData.setCellValueFactory(celula -> new SimpleObjectProperty<>(celula.getValue().getData()));
        colunaCategoria.setCellValueFactory(celula -> new SimpleStringProperty(celula.getValue().getNomeCategoria()));
        colunaTipo.setCellValueFactory(celula -> new SimpleStringProperty(rotuloTipo(celula.getValue().getTipo())));
        colunaValor.setCellValueFactory(celula -> new SimpleStringProperty(moeda.format(celula.getValue().getValor())));
        colunaDescricao.setCellValueFactory(celula -> new SimpleStringProperty(celula.getValue().getDescricao()));
        colunaAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button botaoEditar = new Button("Editar");
            private final Button botaoExcluir = new Button("Excluir");
            private final HBox container = new HBox(8, botaoEditar, botaoExcluir);

            {
                botaoEditar.getStyleClass().add("table-action-button");
                botaoExcluir.getStyleClass().add("table-danger-button");
                container.setAlignment(Pos.CENTER);

                botaoEditar.setOnAction(evento -> {
                    Lancamento lancamento = obterLancamentoAtual();
                    if (lancamento != null) {
                        iniciarEdicao(lancamento);
                    }
                });

                botaoExcluir.setOnAction(evento -> {
                    Lancamento lancamento = obterLancamentoAtual();
                    if (lancamento != null) {
                        removerLancamento(lancamento);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);
                setGraphic(vazio ? null : container);
            }

            private Lancamento obterLancamentoAtual() {
                int indice = getIndex();
                if (indice < 0 || indice >= getTableView().getItems().size()) {
                    return null;
                }
                return getTableView().getItems().get(indice);
            }
        });
    }

    private void carregarDados() {
        Integer ano = cbAnoFiltro.getValue();
        if (ano == null || cbMesFiltro.getValue() == null) {
            return;
        }

        int mes = obterMesSelecionado();
        List<Lancamento> lancamentos = filtrarLancamentosPorPeriodoController.filtrar(mes, ano);
        atualizarTabela(lancamentos);
        atualizarTotais(lancamentos, mes, ano);
        atualizarGraficos(lancamentos, ano);
    }

    private void atualizarTabela(List<Lancamento> lancamentos) {
        tabelaLancamentos.setItems(FXCollections.observableArrayList(lancamentos));
    }

    private void atualizarTotais(List<Lancamento> lancamentosPeriodo, int mes, int ano) {
        BigDecimal receitasPeriodo = somar(lancamentosPeriodo, TipoLancamento.RECEITA);
        BigDecimal despesasPeriodo = somar(lancamentosPeriodo, TipoLancamento.DESPESA);
        BigDecimal investimentosPeriodo = somar(lancamentosPeriodo, TipoLancamento.INVESTIMENTO);
        BigDecimal saldoAtePeriodo = calcularSaldoAtePeriodo(mes, ano);

        lblTotalReceitas.setText(moeda.format(receitasPeriodo));
        lblTotalDespesas.setText(moeda.format(despesasPeriodo));
        lblTotalInvestido.setText(moeda.format(investimentosPeriodo));
        lblSaldoAtual.setText(moeda.format(saldoAtePeriodo));
    }

    private BigDecimal calcularSaldoAtePeriodo(int mes, int ano) {
        LocalDate primeiroDia = LocalDate.of(ano, mes, 1);
        LocalDate limite = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());
        return listarLancamentosController.listar().stream()
                .filter(lancamento -> !lancamento.getData().isAfter(limite))
                .map(lancamento -> lancamento.getTipo() == TipoLancamento.RECEITA
                        ? lancamento.getValor()
                        : lancamento.getValor().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void atualizarGraficos(List<Lancamento> lancamentosPeriodo, int ano) {
        atualizarGraficoPizza(graficoDespesasPorCategoria, lancamentosPeriodo, TipoLancamento.DESPESA, "Sem despesas");
        atualizarGraficoPizza(graficoReceitasPorCategoria, lancamentosPeriodo, TipoLancamento.RECEITA, "Sem receitas");
        atualizarGraficoFluxoAnual(ano);
        atualizarGraficoDesempenhoMensal();
    }

    private void atualizarGraficoPizza(PieChart grafico, List<Lancamento> lancamentos, TipoLancamento tipo, String vazio) {
        if (grafico == null) {
            return;
        }

        Map<String, BigDecimal> valores = new LinkedHashMap<>();
        for (Lancamento lancamento : lancamentos) {
            if (lancamento.getTipo() == tipo) {
                valores.merge(lancamento.getNomeCategoria(), lancamento.getValor(), BigDecimal::add);
            }
        }

        if (valores.isEmpty()) {
            grafico.setData(FXCollections.observableArrayList(new PieChart.Data(vazio, 1)));
        } else {
            grafico.setData(FXCollections.observableArrayList(
                    valores.entrySet().stream()
                            .map(entrada -> new PieChart.Data(entrada.getKey(), entrada.getValue().doubleValue()))
                            .toList()
            ));
        }

        grafico.setLabelsVisible(false);
        grafico.setLegendVisible(true);
        grafico.setLegendSide(Side.TOP);
        grafico.setMinHeight(430);
        grafico.setPrefHeight(430);
        Platform.runLater(() -> {
            var legenda = grafico.lookup(".chart-legend");
            if (legenda != null) {
                legenda.setStyle("-fx-orientation: horizontal; -fx-hgap: 16px; -fx-vgap: 6px; -fx-padding: 10px 0 14px 0;");
            }
            grafico.getData().forEach(dado -> {
                BigDecimal valor = dado.getName().startsWith("Sem ") ? BigDecimal.ZERO : BigDecimal.valueOf(dado.getPieValue());
                Tooltip.install(dado.getNode(), new Tooltip(dado.getName() + ": " + moeda.format(valor)));
            });
        });
    }

    private void atualizarGraficoFluxoAnual(int ano) {
        if (graficoFluxoAnual == null) {
            return;
        }

        Map<Integer, BigDecimal> receitas = inicializarMeses();
        Map<Integer, BigDecimal> despesas = inicializarMeses();
        Map<Integer, BigDecimal> investimentos = inicializarMeses();

        for (Lancamento lancamento : listarLancamentosController.listar()) {
            if (lancamento.getData().getYear() != ano) {
                continue;
            }
            int mes = lancamento.getData().getMonthValue();
            switch (lancamento.getTipo()) {
                case RECEITA -> receitas.merge(mes, lancamento.getValor(), BigDecimal::add);
                case DESPESA -> despesas.merge(mes, lancamento.getValor(), BigDecimal::add);
                case INVESTIMENTO -> investimentos.merge(mes, lancamento.getValor(), BigDecimal::add);
            }
        }

        XYChart.Series<String, Number> serieReceitas = criarSerie("Receitas", receitas);
        XYChart.Series<String, Number> serieDespesas = criarSerie("Despesas", despesas);
        XYChart.Series<String, Number> serieInvestimentos = criarSerie("Investimentos", investimentos);
        graficoFluxoAnual.setLegendSide(Side.TOP);
        graficoFluxoAnual.getData().setAll(FXCollections.observableArrayList(serieReceitas, serieDespesas, serieInvestimentos));
    }

    private void atualizarGraficoDesempenhoMensal() {
        if (graficoDesempenhoMensal == null) {
            return;
        }

        XYChart.Series<String, Number> receitas = new XYChart.Series<>();
        receitas.setName("Receitas");
        XYChart.Series<String, Number> despesas = new XYChart.Series<>();
        despesas.setName("Despesas");
        XYChart.Series<String, Number> investimentos = new XYChart.Series<>();
        investimentos.setName("Investimentos");
        XYChart.Series<String, Number> saldo = new XYChart.Series<>();
        saldo.setName("Saldo");

        BigDecimal saldoAcumulado = BigDecimal.ZERO;
        for (DesempenhoMensal desempenho : visualizarDesempenhoMensalController.visualizar()) {
            String rotulo = String.format("%02d/%d", desempenho.getMes().getMonthValue(), desempenho.getMes().getYear());
            saldoAcumulado = saldoAcumulado
                    .add(desempenho.getTotalReceitas())
                    .subtract(desempenho.getTotalDespesas())
                    .subtract(desempenho.getTotalInvestimentos());

            receitas.getData().add(new XYChart.Data<>(rotulo, desempenho.getTotalReceitas()));
            despesas.getData().add(new XYChart.Data<>(rotulo, desempenho.getTotalDespesas()));
            investimentos.getData().add(new XYChart.Data<>(rotulo, desempenho.getTotalInvestimentos()));
            saldo.getData().add(new XYChart.Data<>(rotulo, saldoAcumulado));
        }

        graficoDesempenhoMensal.setLegendSide(Side.TOP);
        graficoDesempenhoMensal.setAnimated(false);
        graficoDesempenhoMensal.getData().setAll(FXCollections.observableArrayList(receitas, despesas, investimentos, saldo));
    }

    private Map<Integer, BigDecimal> inicializarMeses() {
        Map<Integer, BigDecimal> valores = new LinkedHashMap<>();
        for (int mes = 1; mes <= 12; mes++) {
            valores.put(mes, BigDecimal.ZERO);
        }
        return valores;
    }

    private XYChart.Series<String, Number> criarSerie(String nome, Map<Integer, BigDecimal> valores) {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(nome);
        for (int mes = 1; mes <= 12; mes++) {
            serie.getData().add(new XYChart.Data<>(MESES[mes - 1].substring(0, 3), valores.get(mes)));
        }
        return serie;
    }

    @FXML
    public void salvarLancamento() {
        executarAcao(() -> {
            TipoLancamento tipo = obterTipoLancamento();
            String categoria = obterCategoria();
            String descricao = obterDescricao(tipo, categoria);
            BigDecimal valor = obterValor();
            LocalDate data = obterData();

            cadastrarCategoriaController.cadastrar(categoria);

            if (lancamentoEmEdicaoId != null) {
                editarLancamentoController.editar(lancamentoEmEdicaoId, tipo, descricao, valor, data, categoria, "");
            } else if (tipo == TipoLancamento.RECEITA) {
                cadastrarReceitaController.cadastrar(descricao, valor, data, categoria, "");
            } else if (tipo == TipoLancamento.DESPESA) {
                cadastrarDespesaController.cadastrar(descricao, valor, data, categoria, "");
            } else {
                cadastrarInvestimentoController.cadastrar(descricao, valor, data, categoria, "");
            }

            limparFormulario();
            carregarCategorias();
            carregarDados();
        });
    }

    @FXML
    public void cancelarEdicao() {
        limparFormulario();
    }

    @FXML
    public void prepararNovaReceita() {
        limparFormulario();
        rbReceita.setSelected(true);
        txtValor.requestFocus();
    }

    @FXML
    public void prepararNovaDespesa() {
        limparFormulario();
        rbDespesa.setSelected(true);
        txtValor.requestFocus();
    }

    @FXML
    public void prepararNovoInvestimento() {
        limparFormulario();
        rbInvestimento.setSelected(true);
        txtValor.requestFocus();
    }

    @FXML
    public void alternarTema() {
        temaEscuro = !temaEscuro;
        if (temaEscuro) {
            raiz.getStyleClass().remove("light-theme");
            botaoTema.setText("Tema claro");
        } else if (!raiz.getStyleClass().contains("light-theme")) {
            raiz.getStyleClass().add("light-theme");
            botaoTema.setText("Tema escuro");
        }
    }

    @FXML
    public void exibirTabela() {
        ativarBotaoSegmentado(botaoTabela, botaoGraficos);
        tabelaLancamentos.setVisible(true);
        tabelaLancamentos.setManaged(true);
        painelGraficos.setVisible(false);
        painelGraficos.setManaged(false);
    }

    @FXML
    public void exibirGraficos() {
        ativarBotaoSegmentado(botaoGraficos, botaoTabela);
        tabelaLancamentos.setVisible(false);
        tabelaLancamentos.setManaged(false);
        painelGraficos.setVisible(true);
        painelGraficos.setManaged(true);
        carregarDados();
    }

    @FXML
    public void preencherFormularioComSelecionado() {
        Lancamento selecionado = tabelaLancamentos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            iniciarEdicao(selecionado);
        }
    }

    @FXML
    public void cadastrarNovaCategoria() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Nova categoria");
        dialogo.setHeaderText("Cadastrar nova categoria");
        dialogo.setContentText("Nome da categoria:");

        Optional<String> resultado = dialogo.showAndWait();
        resultado.ifPresent(nome -> executarAcao(() -> {
            String categoria = nome.trim();
            if (!categoria.isBlank()) {
                cadastrarCategoriaController.cadastrar(categoria);
                carregarCategorias();
                cbCategoria.setValue(categoria);
                cbCategoria.getEditor().setText(categoria);
            }
        }));
    }

    private void iniciarEdicao(Lancamento lancamento) {
        lancamentoEmEdicaoId = lancamento.getId();
        lblTituloFormulario.setText("Editar movimentacao");
        txtValor.setText(lancamento.getValor().toPlainString().replace(".", ","));
        seletorData.setValue(lancamento.getData());
        cbCategoria.setValue(lancamento.getNomeCategoria());
        cbCategoria.getEditor().setText(lancamento.getNomeCategoria());
        txtDescricao.setText(lancamento.getDescricao());
        rbReceita.setSelected(lancamento.getTipo() == TipoLancamento.RECEITA);
        rbDespesa.setSelected(lancamento.getTipo() == TipoLancamento.DESPESA);
        rbInvestimento.setSelected(lancamento.getTipo() == TipoLancamento.INVESTIMENTO);
        botaoCancelar.setVisible(true);
        botaoCancelar.setManaged(true);
        botaoSalvar.setText("Salvar alteracoes");
    }

    private void removerLancamento(Lancamento lancamento) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Excluir lancamento");
        confirmacao.setHeaderText("Excluir movimentacao?");
        confirmacao.setContentText("Deseja excluir " + lancamento.getDescricao() + "?");
        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            executarAcao(() -> {
                removerLancamentoController.remover(lancamento.getId());
                limparFormulario();
                carregarDados();
            });
        }
    }

    private void limparFormulario() {
        lancamentoEmEdicaoId = null;
        lblTituloFormulario.setText("Nova movimentacao");
        txtValor.clear();
        txtDescricao.clear();
        cbCategoria.setValue(null);
        cbCategoria.getEditor().clear();
        seletorData.setValue(LocalDate.now());
        rbReceita.setSelected(true);
        botaoCancelar.setVisible(false);
        botaoCancelar.setManaged(false);
        botaoSalvar.setText("Salvar");
        tabelaLancamentos.getSelectionModel().clearSelection();
    }

    private void configurarSelecaoDeCategorias() {
        cbCategoria.setEditable(false);
        cbCategoria.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String categoria, boolean vazio) {
                super.updateItem(categoria, vazio);
                setText(vazio || categoria == null ? null : categoria);
                setGraphic(null);
            }
        });
        cbCategoria.setCellFactory(lista -> new ListCell<>() {
            private final Label nome = new Label();
            private final Button remover = new Button("x");
            private final HBox linha = new HBox(8, nome, remover);

            {
                linha.setAlignment(Pos.CENTER_LEFT);
                nome.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(nome, Priority.ALWAYS);
                remover.getStyleClass().add("category-delete-button");
                remover.setTooltip(new Tooltip("Remover categoria"));
                remover.setOnAction(evento -> {
                    String categoria = getItem();
                    if (categoria != null && !categoria.isBlank()) {
                        removerCategoria(categoria);
                    }
                    evento.consume();
                });
            }

            @Override
            protected void updateItem(String categoria, boolean vazio) {
                super.updateItem(categoria, vazio);
                if (vazio || categoria == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    nome.setText(categoria);
                    setText(null);
                    setGraphic(linha);
                }
            }
        });
    }

    private void carregarCategorias() {
        String atual = cbCategoria.getValue();
        List<String> categorias = new ArrayList<>(listarCategoriasController.listar());
        cbCategoria.getItems().setAll(categorias);
        if (atual != null && categorias.contains(atual)) {
            cbCategoria.setValue(atual);
        } else if (atual == null || !categorias.contains(atual)) {
            cbCategoria.setValue(null);
        }
    }

    private void removerCategoria(String categoria) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Remover categoria");
        confirmacao.setHeaderText("Remover categoria?");
        confirmacao.setContentText("A categoria sera removida da lista, mas os lancamentos existentes continuarao com esse nome.");
        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            executarAcao(() -> {
                removerCategoriaController.remover(categoria);
                if (categoria.equals(cbCategoria.getValue())) {
                    cbCategoria.setValue(null);
                }
                carregarCategorias();
                carregarDados();
            });
        }
    }

    private TipoLancamento obterTipoLancamento() {
        if (rbReceita.isSelected()) {
            return TipoLancamento.RECEITA;
        }
        if (rbDespesa.isSelected()) {
            return TipoLancamento.DESPESA;
        }
        return TipoLancamento.INVESTIMENTO;
    }

    private BigDecimal obterValor() {
        String texto = txtValor.getText();
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Informe o valor.");
        }
        String normalizado = texto.trim().replace("R$", "").replace(" ", "");
        if (normalizado.contains(",") && normalizado.contains(".")) {
            normalizado = normalizado.replace(".", "").replace(",", ".");
        } else {
            normalizado = normalizado.replace(",", ".");
        }
        return new BigDecimal(normalizado);
    }

    private LocalDate obterData() {
        if (seletorData.getValue() == null) {
            throw new IllegalArgumentException("Informe a data.");
        }
        return seletorData.getValue();
    }

    private String obterCategoria() {
        String categoria = cbCategoria.getValue();
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("Selecione uma categoria.");
        }
        return categoria.trim();
    }

    private String obterDescricao(TipoLancamento tipo, String categoria) {
        String descricao = txtDescricao.getText();
        if (descricao == null || descricao.isBlank()) {
            return rotuloTipo(tipo) + " - " + categoria;
        }
        return descricao.trim();
    }

    private BigDecimal somar(List<Lancamento> lancamentos, TipoLancamento tipo) {
        return lancamentos.stream()
                .filter(lancamento -> lancamento.getTipo() == tipo)
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String rotuloTipo(TipoLancamento tipo) {
        return switch (tipo) {
            case RECEITA -> "Receita";
            case DESPESA -> "Despesa";
            case INVESTIMENTO -> "Investimento";
        };
    }

    private int obterMesSelecionado() {
        String mesSelecionado = cbMesFiltro.getValue();
        for (int i = 0; i < MESES.length; i++) {
            if (MESES[i].equals(mesSelecionado)) {
                return i + 1;
            }
        }
        return LocalDate.now().getMonthValue();
    }

    private void ativarBotaoSegmentado(Button ativo, Button inativo) {
        ativo.getStyleClass().remove("btn-segmented-active");
        inativo.getStyleClass().remove("btn-segmented-active");
        ativo.getStyleClass().add("btn-segmented-active");
    }

    private void atualizarLayoutResponsivo() {
        double largura = raiz.getWidth();
        double altura = raiz.getHeight();
        boolean compacto = largura > 0 && (largura < 1120 || altura < 700);
        if (compacto && !raiz.getStyleClass().contains("compact")) {
            raiz.getStyleClass().add("compact");
        } else if (!compacto) {
            raiz.getStyleClass().remove("compact");
        }

        if (conteudoPrincipal != null) {
            conteudoPrincipal.setSpacing(compacto ? 10 : 18);
        }
        if (painelDireito != null) {
            painelDireito.setPrefWidth(compacto ? 330 : 390);
            HBox.setHgrow(painelDireito, Priority.NEVER);
        }
        if (painelEsquerdo != null) {
            HBox.setHgrow(painelEsquerdo, Priority.ALWAYS);
        }
    }

    private void executarAcao(Runnable acao) {
        try {
            acao.run();
        } catch (Exception exception) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Financer");
            alerta.setHeaderText("Nao foi possivel concluir a operacao");
            alerta.setContentText(exception.getMessage());
            alerta.showAndWait();
        }
    }
}