package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.DB_Connection;
import application.exceptions.ErreurCreationCompteException;
import application.models.Client;
import application.models.ClientSalarie;
import application.models.ClientVIP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CreeCompteController implements Initializable {

	
	private Client client;
	
    @FXML
    private ChoiceBox<String> typeText;

    @FXML
    private TextField ribText;

    @FXML
    private Button exitBtn;

    @FXML
    private TextField montantText;
    
    ObservableList<String> liste=FXCollections.observableArrayList("Normal","Epargne","Vip");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		typeText.setValue("Normal");
		typeText.setItems(liste);

	}
	
	
	
    public void initCompte(Client client) {
    	this.client=client;
    //	System.out.println(this.client);
    }
    
    



    @FXML
    void exit(MouseEvent event) {
     	((Stage)exitBtn.getScene().getWindow()).close();
    }
    

    @FXML
    void ouvrirCompte(MouseEvent event) throws SQLException, ErreurCreationCompteException {
		DB_Connection c=new DB_Connection();
		Connection connexion=c.getConnection();
		ResultSet resultat=null;
		PreparedStatement ps = null;
		
		
		try {
	
			//Verification compte
				if(this.client instanceof ClientSalarie) {
			
					if(typeText.getValue().toString().equals("Vip")) throw new ErreurCreationCompteException("Ce type de client n'a pas le droit de créé un compte Vip");
					else {
						if(typeText.getValue().toString().equals("Epargne"))
							ps=connexion.prepareStatement("SELECT rib FROM compteEpargne,clientsalarie WHERE compteEpargne.ID_CLIENT=clientsalarie.ID_CLIENT AND CIN=?");
						else
							ps=connexion.prepareStatement("SELECT rib FROM compte,clientsalarie WHERE compte.ID_CLIENT=clientsalarie.ID_CLIENT AND CIN=?");
						
						ps.setString(1, this.client.getCin());
						
						
					}
					
				}else if(this.client instanceof ClientVIP) {
					
					if(typeText.getValue().toString().equals("Normal")) throw new ErreurCreationCompteException("Ce type de client n'a pas le droit de créé un compte Normal");
					else {
						if(typeText.getValue().toString().equals("Epargne"))
							ps=connexion.prepareStatement("SELECT rib FROM compteEpargne,clientVip WHERE compteEpargne.ID_CLIENT=clientVip.ID_CLIENT AND CIN=?");
						else
							ps=connexion.prepareStatement("SELECT rib FROM compteVip,clientVip WHERE compteVip.ID_CLIENT=clientVip.ID_CLIENT AND CIN=?");
						
						ps.setString(1, this.client.getCin());
						
					}
				}
				
				
				resultat=ps.executeQuery();
				
				if(resultat.next()) throw new ErreurCreationCompteException("Ce client possede déja un compte de ce type !");
				
				if(ribText.getText().isEmpty()) throw new ErreurCreationCompteException("Veuillez remplir le champs Rib");
				if(typeText.getValue().toString().equals("Epargne") && montantText.getText().isEmpty())  throw new ErreurCreationCompteException("Veuillez remplir le champs montant");
				ps=null;
				
				//Verification rib
				
			
						
							ps=connexion.prepareStatement("SELECT rib FROM compteEpargne WHERE rib=? UNION SELECT rib FROM compte WHERE  rib=? UNION  SELECT rib FROM compteVip WHERE  rib=? ");
					
							
						
						ps.setString(1, ribText.getText());
						ps.setString(2, ribText.getText());
						ps.setString(3, ribText.getText());
				
				
				
				resultat=ps.executeQuery();
				
				if(resultat.next()) throw new ErreurCreationCompteException("Le rib que vous avez saissi existe déja !");
				
				
				
				
				
				ps = null;
				
				if(this.client instanceof ClientSalarie) 
					ps=connexion.prepareStatement("SELECT clientSalarie.ID_CLIENT,Agence.ID_AGENCE FROM clientSalarie,agence WHERE clientSalarie.ID_Agence=agence.ID_Agence AND cin=?");
				else
					ps=connexion.prepareStatement("SELECT ClientVIP.ID_CLIENT,Agence.ID_AGENCE FROM ClientVIP,agence WHERE ClientVIP.ID_Agence=agence.ID_Agence AND cin=?");
				
				ps.setString(1,this.client.getCin());
				
				resultat=ps.executeQuery();
				
				if(resultat.next()) {
				
					int id_client= resultat.getInt("ID_CLIENT");
					int id_agence=resultat.getInt("ID_AGENCE");
					
					ps=null;
					
					if(this.client instanceof ClientSalarie) {
						if(typeText.getValue().toString().equals("Epargne")) {
							ps=connexion.prepareStatement("INSERT INTO compteEpargne(ID_CLIENT,ID_AGENCE,RIB,SOLDE,DATE_CREATION,MONTANT,DATE_VERSEMENT)  VALUES (?,?,?,?,?,?,?)");
							
						}else {
							ps=connexion.prepareStatement("INSERT INTO Compte (ID_CLIENT,ID_AGENCE,RIB,SOLDE,DATE_CREATION) VALUES (?,?,?,?,?)");
						}
						
					}else if(this.client instanceof ClientVIP) {
						if(typeText.getValue().toString().equals("Epargne")) {
							ps=connexion.prepareStatement("INSERT INTO compteEpargne (ID_CLIENT,ID_AGENCE,RIB,SOLDE,DATE_CREATION,MONTANT,DATE_VERSEMENT)  VALUES (?,?,?,?,?,?,?)");
							
						}else {
							ps=connexion.prepareStatement("INSERT INTO CompteVip (ID_CLIENT,ID_AGENCE,RIB,SOLDE,DATE_CREATION) VALUES (?,?,?,?,?)");
						}
					}
					ps.setInt(1,id_client);
					ps.setInt(2, id_agence );
					ps.setString(3, ribText.getText());
					ps.setDouble(4, 0);
					ps.setDate(5, Date.valueOf(LocalDate.now()));
					
					if(typeText.getValue().toString().equals("Epargne")) {
						ps.setDouble(6, Double.parseDouble(montantText.getText()));
						ps.setDate(7, Date.valueOf(LocalDate.now()));
					}
					
					ps.executeUpdate();
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
		    		alert.setTitle("Création du compte réussie");
		    		alert.setContentText("Compte créé avec succés");
		    		alert.setHeaderText("Information");
		    		alert.showAndWait();
				}
				
				connexion.close();
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Message");
		        alert.setHeaderText(" Succés ");
		        alert.setContentText("Le compte a été bien créé ! ");
		        alert.showAndWait();
			}catch(ErreurCreationCompteException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
	    		alert.setTitle("Erreur Création du compte");
	    		alert.setContentText(e.getMessage());
	    		alert.setHeaderText("Erreur");
	    		alert.showAndWait();
			}
		}
		
		
    
}
