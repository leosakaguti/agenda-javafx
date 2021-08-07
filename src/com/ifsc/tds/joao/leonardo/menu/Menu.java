package com.ifsc.tds.joao.leonardo.menu;
	
import com.ifsc.tds.joao.leonardo.controller.ContatosListaController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Menu extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ifsc/tds/joao/leonardo/view/ContatosLista.fxml"));
			Parent menuXML = loader.load();
			
			ContatosListaController contatosListaController = loader.getController();
			Scene menuLayout = new Scene(menuXML);
			
			Stage menuJanela = new Stage();
			menuJanela.initModality(Modality.APPLICATION_MODAL);
			menuJanela.resizableProperty().setValue(Boolean.FALSE);
			menuJanela.setScene(menuLayout);
			menuJanela.setTitle("ContatosLista");
			
			menuJanela.setOnCloseRequest(e-> {
				if(contatosListaController.onCloseQuery()) {
					System.exit(0);
				}else {
					e.consume();
				}
			});
			
			menuJanela.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
