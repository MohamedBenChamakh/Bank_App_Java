package application.controllers;


import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import application.DB_Connection;
import application.Main;
import application.exceptions.ErrNegatif;
import application.exceptions.VirementException;
import application.models.Client;
import application.models.ClientSalarie;
import application.models.ClientVIP;
import application.models.Compte;
import application.models.CompteAbstrait;
import application.models.CompteEpargne;
import application.models.CompteVIP;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class InfoCompteController {

	private Client client;
	
	private CompteAbstrait compte;
	
    @FXML
    private Button exitBtn;

    @FXML
    private Button versementBtn;

    @FXML
    private Button retraitBtn;

    @FXML
    private Button virementBtn;

    @FXML
    private Label ribLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label soldeLabel;

    @FXML
    private Text ribText;

    @FXML
    private Text typeText;

    @FXML
    private Text soldeText;

    @FXML
    private Label montantLabel;

    @FXML
    private Text montantText;

    public void initInfoCompte(Client client,List<String> element) throws SQLException, NumberFormatException, ErrNegatif {

		    	this.client=client;
		    	System.out.println(element);
				DB_Connection c=new DB_Connection();
				Connection connexion=c.getConnection();
				ResultSet resultat=null;
				PreparedStatement ps = null;
				if(element.get(0).equals("Epargne")) {
					ps =connexion.prepareStatement("SELECT * FROM compteepargne WHERE rib=?  ");
					
				}
				else if(this.client instanceof ClientVIP) {
						ps =connexion.prepareStatement("SELECT * FROM compteVip WHERE rib=?  ");
						montantText.setVisible(false);
						montantLabel.setVisible(false);
					}
				else if(this.client instanceof Client) {
						ps =connexion.prepareStatement("SELECT * FROM compte WHERE rib=?  ");
						montantText.setVisible(false);
						montantLabel.setVisible(false);
				}
			
			
				ps.setInt(1, Integer.parseInt(element.get(1)));
				resultat=ps.executeQuery();
				
				if(resultat.next()) {
			
					if(element.get(0).equals("Epargne")) {
						montantText.setText(String.valueOf( resultat.getDouble("Montant")) );
					}
					
			    	typeText.setText(element.get(0));
			    	ribText.setText(element.get(1));
			    	soldeText.setText(String.valueOf( resultat.getDouble("SOLDE")));
			    	
			    	if(element.get(0).equals("Epargne")) {
						this.compte=new CompteEpargne(Long.parseLong(resultat.getString("rib")), resultat.getDouble("SOLDE"), Main.agence, resultat.getDouble("Montant"), client,Date.valueOf(String.valueOf(resultat.getDate("DATE_VERSEMENT"))).toLocalDate(),Date.valueOf(String.valueOf(resultat.getDate("DATE_CREATION"))).toLocalDate() );
					}else if(element.get(0).equals("Normal")) {
						this.compte=new Compte(Long.parseLong(resultat.getString("rib")), resultat.getDouble("SOLDE"), Main.agence,(ClientSalarie) client);
					}else {
						this.compte=new CompteVIP(Long.parseLong(resultat.getString("rib")), resultat.getDouble("SOLDE"), Main.agence,(ClientVIP) client);
					}
			    	
				}
    
		
		
    }
    
    @FXML
    void exit(MouseEvent event) {
  
    	((Stage)exitBtn.getScene().getWindow()).close();
    }

    @FXML
    void retrait(MouseEvent event) throws IOException {
    	Main.showRetrait(this.client,this.compte);
    }

    @FXML
    void versement(MouseEvent event) throws IOException {
    	Main.showVersement(this.client,this.compte);
    }

    @FXML
    void virement(MouseEvent event) throws IOException, VirementException {
    	try {
    	if(this.compte instanceof CompteEpargne) throw new VirementException("Vous n'avez pas le droit d'effectuer cette opération sur ce compte !");
    	Main.showVirement(this.client,this.compte);
    	}catch(VirementException e) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("Erreur Authentification");
    		alert.setContentText(e.getMessage());
    		alert.setHeaderText(null);
    		alert.showAndWait();
    	}
    }

}
