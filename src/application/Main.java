package application;
	
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import application.controllers.VirementController;
import application.controllers.CreeCompteController;
import application.controllers.InfoCompteController;
import application.controllers.ListClientController;
import application.controllers.ListeComptesController;
import application.controllers.RetraitController;
import application.controllers.VersementController;
import application.exceptions.ErrNegatif;
import application.models.Agence;
import application.models.Client;
import application.models.CompteAbstrait;
import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	public static List<Client> listeClient=new ArrayList<>();
	public static Agence agence;
    static Stage primaryStage;
	static Stage secondStage;
    static BorderPane mainLayout ;
	static FXMLLoader loader;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
            System.setProperty("javafx.platform" , "Desktop");
			Main.primaryStage=primaryStage;
			showLoginStage();
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Message");
	        alert.setHeaderText("Bienvenue ! ");
	        alert.setContentText("Connectez-vous pour pouvoir utiliser l'application ");
	        alert.showAndWait();
	}
	
	public static void showLoginStage() throws Exception {
		loader=new FXMLLoader();
		loader.setLocation(Main.class.getResource("fxml/Login.fxml"));
		mainLayout = loader.load();
		Scene root=new Scene(mainLayout);
		primaryStage.setScene(root);
        primaryStage.setTitle("Application Bancaire");
		primaryStage.setResizable(false);
		primaryStage.show();
	
	}
	
	public static void showListClient() throws Exception, SQLException, ErrNegatif {
		loader=new FXMLLoader();
		loader.setLocation(Main.class.getResource("fxml/ListClient.fxml"));
		Parent listAccountLayout = loader.load();
		primaryStage.setTitle("Liste des clients");
		Scene scene2=new Scene(listAccountLayout);
		primaryStage.setScene(scene2);
		ListClientController controller= loader.getController();
		controller.initList();
		primaryStage.show();
	}
		
	public static void showListAccount(Client client) throws Exception, SQLException, NumberFormatException, ErrNegatif {
	
		 loader=new FXMLLoader();
		 loader.setLocation(Main.class.getResource("fxml/ListeComptes.fxml"));
		 Parent InfoAccountLayout = loader.load();
		 secondStage=new Stage();
		 Scene scene3=new Scene(InfoAccountLayout);
		 secondStage.setTitle("Gestionnaire des comptes");
		 ListeComptesController controller=loader.getController();
		 controller.initClient(client);
		 secondStage.setScene(scene3);
		 secondStage.setResizable(false);
		 secondStage.initModality(Modality.WINDOW_MODAL);
		 secondStage.initOwner(primaryStage);
		 secondStage.show();
		
	}
	
	public static void showInfoCompte(Client client,List<String> element) throws IOException, SQLException, NumberFormatException, ErrNegatif {
		 loader=new FXMLLoader();
		 loader.setLocation(Main.class.getResource("fxml/InfoCompte.fxml"));
		 Parent InfoAccountLayout = loader.load();
		 secondStage=new Stage();
		 Scene scene3=new Scene(InfoAccountLayout);
		 secondStage.setTitle("Information compte");
		 InfoCompteController controller=loader.getController();
		 controller.initInfoCompte(client,element);
		 secondStage.setScene(scene3);
		 secondStage.setResizable(false);
		 secondStage.initModality(Modality.WINDOW_MODAL);
		 secondStage.initOwner(primaryStage);
		 secondStage.show();
	}
	
	public static void showRetrait(Client client, CompteAbstrait compte) throws IOException {
	 		FXMLLoader loader=new FXMLLoader();
			loader.setLocation(Main.class.getResource("fxml/Retrait.fxml"));
			Parent InfoAccountLayout = loader.load();
			Stage newWindow = new Stage();
			Scene scene3=new Scene(InfoAccountLayout);
			RetraitController controller= loader.getController();
			controller.initClient(client,compte);
			System.out.println(compte);
			newWindow.setTitle("Opération de retrait");
			newWindow.setScene(scene3);
			newWindow.setResizable(false);
			newWindow.initModality(Modality.WINDOW_MODAL);
			newWindow.initOwner(Main.secondStage);
	        newWindow.show();
	}
	
	public static void showVirement(Client client, CompteAbstrait compte) throws IOException {
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(Main.class.getResource("fxml/Virement.fxml"));
		Parent InfoAccountLayout = loader.load();
		Stage newWindow = new Stage();
		Scene scene3=new Scene(InfoAccountLayout);
		VirementController controller= loader.getController();
		controller.initClient(client,compte);
		newWindow.setTitle("Opération de virement");
		newWindow.setScene(scene3);
		newWindow.setResizable(false);
		newWindow.initModality(Modality.WINDOW_MODAL);
		newWindow.initOwner(Main.secondStage);
        newWindow.show();
	}
	
	public static void showVersement(Client client, CompteAbstrait compte) throws IOException {
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(Main.class.getResource("fxml/Versement.fxml"));
		Parent InfoAccountLayout = loader.load();
		Stage newWindow = new Stage();
		Scene scene3=new Scene(InfoAccountLayout);
		VersementController controller= loader.getController();
		controller.initClient(client,compte);
		newWindow.setTitle("Opération de versement");
		newWindow.setScene(scene3);
		newWindow.setResizable(false);
		newWindow.initModality(Modality.WINDOW_MODAL);
		newWindow.initOwner(Main.secondStage);
        newWindow.show();
	}
		
	public static void showCreeCompte(Client client) throws IOException {
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(Main.class.getResource("fxml/CreeCompte.fxml"));
		Parent InfoAccountLayout = loader.load();
		Stage newWindow = new Stage();
		Scene scene3=new Scene(InfoAccountLayout);
		CreeCompteController controller=loader.getController();
		controller.initCompte(client);
		newWindow.setTitle("Ouverture de compte");
		newWindow.setScene(scene3);
		newWindow.setResizable(false);
		newWindow.initModality(Modality.WINDOW_MODAL);
		newWindow.initOwner(Main.secondStage);
        newWindow.show();
	}	
	
	public static void main(String[] args) {
		launch(args);
	}
}
