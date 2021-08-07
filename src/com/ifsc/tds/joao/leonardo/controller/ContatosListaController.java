package com.ifsc.tds.joao.leonardo.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.ifsc.tds.joao.leonardo.dao.ContatoDAO;
import com.ifsc.tds.joao.leonardo.entity.Contato;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ContatosListaController implements Initializable {

	@FXML
	private TableView<Contato> viewContatos;

	@FXML
	private TableColumn<Contato, Long> colunaCodigo;

	@FXML
	private TableColumn<Contato, String> colunaNome;

	@FXML
	private Label lblDescricao;

	@FXML
	private GridPane pnlDetalhes;

	@FXML
	private Label lblNome;

	@FXML
	private Label lblTelefone;

	@FXML
	private Label lblNomeValor;

	@FXML
	private Label lblTelefoneValor;

	@FXML
	private Button btnIncluir;

	@FXML
	private Tooltip tlpIncluir;

	@FXML
	private Button btnEditar;

	@FXML
	private Tooltip tlpEditar;

	@FXML
	private Button btnExcluir;

	@FXML
	private Tooltip tlpExcluir;

	private List<Contato> listaContatos;
	private ObservableList<Contato> observableListaContatos = FXCollections.observableArrayList();
	private ContatoDAO contatoDAO;

	public static final String CONTATO_EDITAR = " - Editar";
	public static final String CONTATO_INCLUIR = " - Incluir";

	private Stage stage;

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	void onClickEditar(ActionEvent event) {
		Contato contato = this.viewContatos.getSelectionModel().getSelectedItem();

		if (contato != null) {
			boolean btnConfirmarClick = this.onShowTelaContatoEditar(contato, ContatosListaController.CONTATO_EDITAR);

			if (btnConfirmarClick) {
				this.getContatoDAO().update(contato, null);
				this.carregarTableViewContatos();
			}
		} else {
			Alert alerta = new Alert(AlertType.ERROR);
			alerta.setContentText("Por favor, escolha um contato na tabela para editar!");
		}
	}

	@FXML
	void onClickExcluir(ActionEvent event) {
		Contato contato = this.viewContatos.getSelectionModel().getSelectedItem();

		if (contato != null) {
			Alert alerta = new Alert(AlertType.CONFIRMATION);
			alerta.setTitle("Pergunta");
			alerta.setHeaderText("Confirma a exclusão do contato?\n" + contato.getNome());
			
			ButtonType buttonTypeNO = ButtonType.NO;
			ButtonType buttonTypeYES = ButtonType.YES;
			alerta.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);
			Optional<ButtonType> result = alerta.showAndWait();
			
			if(result.get() == buttonTypeYES) {
				this.getContatoDAO().delete(contato);
				this.carregarTableViewContatos();
			}
		}else {
			Alert alerta = new Alert(AlertType.ERROR);
			alerta.setTitle("Erro");
			alerta.setContentText("Por favor, escolha um contato na tabela para excluir!");
		}
	}

	@FXML
	void onClickIncluir(ActionEvent event) {
		Contato contato = new Contato();

		boolean btnConfirmarClick = this.onShowTelaContatoEditar(contato, ContatosListaController.CONTATO_INCLUIR);

		if (btnConfirmarClick) {
			this.contatoDAO.save(contato);
			this.carregarTableViewContatos();
		}
	}

	public ContatoDAO getContatoDAO() {
		return contatoDAO;
	}

	public void setContatoDAO(ContatoDAO contatoDAO) {
		this.contatoDAO = contatoDAO;
	}

	public List<Contato> getListaContatos() {
		return listaContatos;
	}

	public void setListaContatos(List<Contato> listaContatos) {
		this.listaContatos = listaContatos;
	}

	public ObservableList<Contato> getObservableListaContatos() {
		return observableListaContatos;
	}

	public void setObservableListaContatos(ObservableList<Contato> observableListaContatos) {
		this.observableListaContatos = observableListaContatos;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.setContatoDAO(new ContatoDAO());
		this.carregarTableViewContatos();
		this.selecionarItemTableViewContatos(null);

		this.viewContatos.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionarItemTableViewContatos(newValue));
		this.configuraStage();
	}

	public void carregarTableViewContatos() {
		this.colunaCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		this.setListaContatos(this.getContatoDAO().getAll());
		this.setObservableListaContatos(FXCollections.observableArrayList(this.getListaContatos()));
		this.viewContatos.setItems(this.getObservableListaContatos());
	}

	public void selecionarItemTableViewContatos(Contato contato) {
		if (contato != null) {
			this.lblNomeValor.setText(contato.getNome());
			this.lblTelefoneValor.setText(contato.getTelefone());
		} else {
			this.lblNomeValor.setText("");
			this.lblTelefoneValor.setText("");
		}
	}

	public boolean onCloseQuery() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Pergunta");
		alert.setHeaderText("Deseja sair do sistema?");
		ButtonType buttonTypeNO = ButtonType.NO;
		ButtonType buttonTypeYES = ButtonType.YES;
		alert.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);
		Optional<ButtonType> result = alert.showAndWait();
		return result.get() == buttonTypeYES ? true : false;
	}

	public boolean onShowTelaContatoEditar(Contato contato, String operacao) {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/com/ifsc/tds/joao/leonardo/view/ContatoEdit.fxml"));
			Parent contatoEditXML = loader.load();

			Stage janelaContatoEditar = new Stage();
			janelaContatoEditar.setTitle("Cadastro de Contato" + operacao);
			janelaContatoEditar.initModality(Modality.APPLICATION_MODAL);
			janelaContatoEditar.resizableProperty().setValue(Boolean.FALSE);

			Scene contatoEditLayout = new Scene(contatoEditXML);
			janelaContatoEditar.setScene(contatoEditLayout);

			ContatoEditController contatoEditController = loader.getController();
			contatoEditController.setJanelaContatoEdit(janelaContatoEditar);
			contatoEditController.populaTela(contato);

			janelaContatoEditar.showAndWait();

			return contatoEditController.isOkClick();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public void configuraStage() {
		this.setStage(new Stage());
		this.getStage().initModality(Modality.APPLICATION_MODAL);
		this.getStage().resizableProperty().setValue(Boolean.FALSE);
	}
}
