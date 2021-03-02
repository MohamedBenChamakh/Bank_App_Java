package application.controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.DB_Connection;
import application.Main;
import application.exceptions.CompteEpargneException;
import application.exceptions.ErrNegatif;
import application.exceptions.VersementException;
import application.models.Client;
import application.models.Compte;
import application.models.CompteAbstrait;
import application.models.CompteEpargne;
import application.models.CompteVIP;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class VersementController {
	
	private Client client;
	
	private  CompteAbstrait compte;
	
    @FXML
    private TextField montantText;

    @FXML
    private Button exitBtn;

    @FXML
    private Text soldeText;
    
    @FXML
    void confirmerVersement(MouseEvent event) throws SQLException, CompteEpargneException, VersementException {
		try {
				if(montantText.getText().isEmpty()) throw new VersementException("Veuillez remplir tous les champs !");
		    	System.out.println(Main.agence);
				if(this.compte instanceof Compte) {
					((Compte)this.compte).versement(Double.parseDouble(this.montantText.getText()));
				}else if(this.compte instanceof CompteEpargne) {
					if(!((CompteEpargne)this.compte).versement(Double.parseDouble(this.montantText.getText())))
					 throw new CompteEpargneException("Compte Bloqué !");
		
				}else {
					((CompteVIP)this.compte).versement(Double.parseDouble(this.montantText.getText()));
				}
				soldeText.setText(String.valueOf(this.compte.getSolde()));
				System.out.println(Main.agence);
				
				DB_Connection c=new DB_Connection();
				Connection connexion=c.getConnection();
				PreparedStatement ps = null;
				if(this.compte instanceof Compte) {
					ps=connexion.prepareStatement("UPDATE Compte SET SOLDE=? WHERE rib=? ");
					ps.setString(2,String.valueOf( this.compte.getRib()));
				}else if(this.compte instanceof CompteEpargne) {
					ps=connexion.prepareStatement("UPDATE CompteEpargne SET SOLDE=? ,DATE_VERSEMENT=?,MONTANT=?  WHERE rib=? ");
					ps.setDate(2,Date.valueOf(((CompteEpargne) this.compte).getDate_dernier_versement())   );
					ps.setDouble(3, ((CompteEpargne) this.compte).getMontant_dernier_versement());
					ps.setString(4,String.valueOf( this.compte.getRib()));
				}else {
					ps=connexion.prepareStatement("UPDATE CompteVIP SET SOLDE=? WHERE rib=? ");
					ps.setString(2,String.valueOf( this.compte.getRib()));
				}
				
				ps.setDouble(1, this.compte.getSolde());
				
				ps.executeUpdate();
				
				ps=null;
				
				ps=connexion.prepareStatement("UPDATE AGENCE SET credit_agence_global=? WHERE NOM_AGENCE=?");
				ps.setDouble(1, Main.agence.getCredit_agence_global());
				ps.setString(2,  Main.agence.getNom_agence());
				
				ps.executeUpdate();
				
				
				connexion.close();
				
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Message");
		        alert.setHeaderText(" Succés ");
		        alert.setContentText("Le versement a été bien effectué ! ");
		        alert.showAndWait();
		}catch(CompteEpargneException | VersementException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("Erreur Versement");
    		alert.setContentText(e.getMessage());
    		alert.setHeaderText("Erreur Versement");
    		alert.showAndWait();
		}
    }

	public void initClient(Client client, CompteAbstrait compte) {
		// TODO Auto-generated method stub
		this.client=client;	
		this.compte=compte;
		soldeText.setText(String.valueOf(this.compte.getSolde()));
		System.out.println(compte);

	}
    
    
    @FXML
    void exit(MouseEvent event) {
    	((Stage)exitBtn.getScene().getWindow()).close();
    }
}
