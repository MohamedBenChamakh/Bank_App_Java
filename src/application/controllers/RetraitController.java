package application.controllers;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DB_Connection;
import application.Main;
import application.exceptions.CompteEpargneException;
import application.exceptions.ErrNegatif;
import application.models.Client;
import application.models.Compte;
import application.models.CompteAbstrait;
import application.models.CompteEpargne;
import application.models.CompteVIP;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RetraitController {
	private Client client;
	
	private  CompteAbstrait compte;
	
    @FXML
    private TextField montantText;

    @FXML 
    private Button exitBtn;
    
    @FXML
    private Text soldeText;

    @FXML
    void confirmerRetrait(MouseEvent event) throws  ErrNegatif, SQLException, CompteEpargneException {
    	

    	
    	try {
        	if(montantText.getText().isEmpty()) throw new CompteEpargneException("Veuillez remplir tous les champs !");
    		System.out.println(Main.agence);
			if(this.compte instanceof Compte) {
				if(!((Compte)this.compte).retrait(Double.parseDouble(this.montantText.getText())))
					throw new ErrNegatif("Seuil dépassé");
			}else if(this.compte instanceof CompteEpargne) {
				if(!((CompteEpargne)this.compte).retrait(Double.parseDouble(this.montantText.getText())))
					throw new CompteEpargneException("Le client ne peut pas retirer de l'argent de ce compte en raison de : date de création du compte < année ou somme bloqué < 2000 DT");
	
			}else {
				if(!((CompteVIP)this.compte).retrait(Double.parseDouble(this.montantText.getText())))
					throw new ErrNegatif("Seuil dépassé");
			}
			System.out.println(String.valueOf(this.compte.getSolde()));
			soldeText.setText(String.valueOf(this.compte.getSolde()));
			System.out.println(Main.agence);
			
			DB_Connection c=new DB_Connection();
			Connection connexion=c.getConnection();
			PreparedStatement ps = null;
			if(this.compte instanceof Compte) {
				ps=connexion.prepareStatement("UPDATE Compte SET SOLDE=? WHERE rib=? ");
			}else if(this.compte instanceof CompteEpargne) {
				ps=connexion.prepareStatement("UPDATE CompteEpargne SET SOLDE=? WHERE rib=? ");
			}else {
				ps=connexion.prepareStatement("UPDATE CompteVIP SET SOLDE=? WHERE rib=? ");
			}
			
			ps.setDouble(1, this.compte.getSolde());
			ps.setString(2,String.valueOf( this.compte.getRib()));
			ps.executeUpdate();
			
			ps=null;
			
			ps=connexion.prepareStatement("UPDATE AGENCE SET debit_agence_global=? WHERE NOM_AGENCE=?");
			ps.setDouble(1, Main.agence.getDebit_agence_global());
			ps.setString(2,  Main.agence.getNom_agence());
			
			ps.executeUpdate();
			
			
			connexion.close();
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Message");
	        alert.setHeaderText(" Succés ");
	        alert.setContentText("Le retrait a été bien effectué ! ");
	        alert.showAndWait();
    	}
    	catch(ErrNegatif e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("Erreur montant invalide");
    		alert.setContentText(e.getMessage());
    		alert.setHeaderText(null);
    		alert.showAndWait();
		}catch(CompteEpargneException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("Erreur Compte Epargne");
    		alert.setContentText(e.getMessage());
    		alert.setHeaderText(null);
    		alert.showAndWait();
		}
    }


    @FXML
    void exit(MouseEvent event) {
    	((Stage)exitBtn.getScene().getWindow()).close();
    }


	public void initClient(Client client, CompteAbstrait compte) {
		// TODO Auto-generated method stub
		this.client=client;	
		this.compte=compte;
		soldeText.setText(String.valueOf(this.compte.getSolde()));
		System.out.println(compte);

	}
}
