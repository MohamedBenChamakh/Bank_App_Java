package application.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import application.DB_Connection;
import application.Main;
import application.exceptions.AuthentificationException;
import application.models.Agence;


public class LoginController {

	
	
    @FXML
	private BorderPane loginPane;
    
    @FXML
    private Button connexion;
    
    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private CheckBox checkVIP;
    
    @FXML
    void Authentification(ActionEvent event) throws AuthentificationException, Exception {
    	try {
		    	if(!login.getText().isEmpty() && !password.getText().isEmpty()) {
		
		    		DB_Connection c=new DB_Connection();
		    		Connection connexion=c.getConnection();
		    		ResultSet resultat=null;
		    		PreparedStatement ps;
		    		
		 
		    		ps =connexion.prepareStatement("SELECT * FROM Agent,Agence WHERE login=? AND password=? AND Agent.ID_AGENCE=Agence.ID_AGENCE ");
		    	
					ps.setString(1,login.getText());
					ps.setString(2,password.getText());
					
			
					
					resultat=ps.executeQuery();
			
					
					if(resultat.next()) {
							Main.agence= new Agence(resultat.getString("NOM_AGENCE"),resultat.getDouble("DEBIT_AGENCE_GLOBAL"),resultat.getDouble("CREDIT_AGENCE_GLOBAL"));
							Main.showListClient();
			
					}else throw new AuthentificationException("Login ou mot de passe invalide !");
			
		
					connexion.close();
		    
		    	} else throw new AuthentificationException("Champs invalide !");
		    	
    	}catch(AuthentificationException e) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("Erreur Authentification");
    		alert.setContentText(e.getMessage());
    		alert.setHeaderText(null);
    		alert.showAndWait();
    	}
    }
		
}
