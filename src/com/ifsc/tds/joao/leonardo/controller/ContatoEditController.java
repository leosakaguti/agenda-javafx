package com.ifsc.tds.joao.leonardo.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.ifsc.tds.joao.leonardo.entity.Contato;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ContatoEditController implements Initializable {

	@FXML
	private AnchorPane pnlPrincipal;

	@FXML
	private GridPane pnlDados;

	@FXML
	private Label lblNome;

	@FXML
	private Label lblTelefone;

	@FXML
	private Label lblEmail;

	@FXML
	private TextField fieldNome;

	@FXML
	private TextField fieldTelefone;

	@FXML
	private TextField fieldEmail;

	@FXML
	private HBox pnlBotoes;

	@FXML
	private Button btnOK;

	@FXML
	private Button btnCancela;

	private Stage janelaContatoEdit;

	private Contato contato;

	private boolean okClick = false;

	@FXML
	void onClickBtnCancela(ActionEvent event) {
		this.getJanelaContatoEdit().close();
	}

	@FXML
	void onClickBtnOK(ActionEvent event) {
		if (validarCampos()) {
			this.contato.setNome(this.fieldNome.getText());
			this.contato.setTelefone(this.fieldTelefone.getText());
			this.contato.setEmail(this.fieldEmail.getText());

			this.okClick = true;
			this.getJanelaContatoEdit().close();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public Stage getJanelaContatoEdit() {
		return janelaContatoEdit;
	}

	public void setJanelaContatoEdit(Stage janelaContatoEdit) {
		this.janelaContatoEdit = janelaContatoEdit;
	}

	public void populaTela(Contato contato) {
		this.contato = contato;

		this.fieldNome.setText(contato.getNome());
		this.fieldTelefone.setText(contato.getTelefone());
		this.fieldEmail.setText(contato.getEmail());
	}

	public boolean isOkClick() {
		return okClick;
	}

	private boolean validarCampos() {
		String mensagemErros = new String();

		if (this.fieldNome.getText() == null || this.fieldNome.getText().trim().length() == 0) {
			mensagemErros += "Informe o nome!\n";
		}

		if (mensagemErros.length() == 0) {
			return true;
		} else {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.initOwner(this.janelaContatoEdit);
			alerta.setTitle("Dados Inválidos!");
			alerta.setHeaderText("Favor corrigir as seguintes informações");
			alerta.setContentText(mensagemErros);
			alerta.showAndWait();

			return false;
		}
	}
	
	public boolean onCloseQuery() {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Pergunta");
		alerta.setHeaderText("Deseja sair da tela de edição?");
		ButtonType buttonTypeNO = ButtonType.NO;
		ButtonType buttonTypeYES = ButtonType.YES;
		alerta.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);
		Optional<ButtonType> result = alerta.showAndWait();
		return result.get() == buttonTypeYES ? true : false;
	}

}
