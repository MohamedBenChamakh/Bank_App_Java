package application.controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DB_Connection;
import application.Main;
import application.exceptions.ErrNegatif;
import application.exceptions.ErrSoldeRouge;
import application.exceptions.ErreurCreationCompteException;
import application.exceptions.VirementException;
import application.models.Client;
import application.models.ClientSalarie;
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

public class VirementController {

    @FXML
    private TextField montantText;

    @FXML
    private Button exitBtn;

    @FXML
    private TextField compteText;

    @FXML
    private Text soldeText;

	private Client client;

	private CompteAbstrait compte;

    @FXML
    void confirmerVirement(MouseEvent event) throws SQLException, ErrNegatif, VirementException {
    	try {
			DB_Connection c=new DB_Connection();
			Connection connexion=c.getConnection();
			ResultSet resultat=null;
			PreparedStatement ps;
	    	CompteAbstrait compteRecepteur;
			
	    	if(compteText.getText().isEmpty() || montantText.getText().isEmpty()) throw new VirementException("Veuillez remplir tous les champs !");
	    	
	    	
			ps=connexion.prepareStatement("SELECT * FROM compteEpargne WHERE rib=?  ");
			ps.setString(1, compteText.getText());
			
			resultat=ps.executeQuery();
			
			if(resultat.next()) {
				compteRecepteur=new CompteEpargne(Long.parseLong(resultat.getString("RIB")),resultat.getDouble("Solde"),Main.agence,resultat.getDouble("Montant"),null,Date.valueOf(String.valueOf(resultat.getDate("DATE_VERSEMENT"))).toLocalDate(),Date.valueOf(String.valueOf(resultat.getDate("DATE_CREATION"))).toLocalDate() );
			}else {
				ps=connexion.prepareStatement(" SELECT rib,solde FROM compte WHERE  rib=? ");
				ps.setString(1, compteText.getText());
				
				resultat=ps.executeQuery();
				if(resultat.next()) {
					compteRecepteur=new Compte(Long.parseLong(resultat.getString("RIB")),resultat.getDouble("Solde"),Main.agence,null);
					
				}else {
					ps=connexion.prepareStatement(" SELECT rib,solde FROM compteVip WHERE  rib=? ");
					ps.setString(1, compteText.getText());
					
					resultat=ps.executeQuery();
					if(resultat.next()) {
						compteRecepteur=new CompteVIP(Long.parseLong(resultat.getString("RIB")),resultat.getDouble("Solde"),Main.agence,null);
						
						
					}else throw new VirementException("Le rib que vous avez saissi n'existe pas !");
				}
			}
			
			
			

			
	    	if(this.client instanceof ClientSalarie) {
	    		if(!((Compte)this.compte).virement(compteRecepteur, Double.parseDouble(montantText.getText()))) throw new VirementException("Erreur Virement !");
	    	}else
	    		if(!((CompteVIP)this.compte).virement(compteRecepteur,  Double.parseDouble(montantText.getText()))) throw new VirementException("Erreur Virement !");
	    	
	    	System.out.println(compteRecepteur);
	    	System.out.println(this.compte);
	  
	    	soldeText.setText(String.valueOf(this.compte.getSolde()));
	    	
	    	ps=null;
	    	
	    	if(compteRecepteur instanceof Compte) {
	    		ps=connexion.prepareStatement("UPDATE Compte SET solde=? WHERE rib=?");
	    	}else if(compteRecepteur instanceof CompteVIP){
	    		ps=connexion.prepareStatement("UPDATE CompteVIP SET solde=? WHERE rib=?");
	    	}else
	    		ps=connexion.prepareStatement("UPDATE CompteEpargne SET solde=? WHERE rib=?");
	    	
	    	ps.setDouble(1,compteRecepteur.getSolde());
	    	ps.setString(2, String.valueOf(compteRecepteur.getRib()));
	    	
	    	ps.executeUpdate();
	    	
	    	
	    	ps=connexion.prepareStatement("UPDATE Agence SET DEBIT_AGENCE_GLOBAL=? , CREDIT_AGENCE_GLOBAL=? WHERE Nom_AGENCE=?");
	    	ps.setDouble(1,Main.agence.getDebit_agence_global());
	    	ps.setDouble(2,Main.agence.getCredit_agence_global());
	    	ps.setString(3, Main.agence.getNom_agence());
	    	
	    	ps.executeUpdate();
	    	
	    	ps=null;
	    	
	    	if(this.compte instanceof Compte) {
	    		ps=connexion.prepareStatement("UPDATE Compte SET solde=? WHERE rib=?");
	    	}else
	    		ps=connexion.prepareStatement("UPDATE CompteVIP SET solde=? WHERE rib=?");
	    	
	    	ps.setDouble(1, this.compte.getSolde());
	    	ps.setString(2, String.valueOf(this.compte.getRib()));
	    	
	    	ps.executeUpdate();
	    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Message");
	        alert.setHeaderText(" Succés ");
	        alert.setContentText("Le virement a été bien effectué ! ");
	        alert.showAndWait();
    	}catch(VirementException | ErrSoldeRouge e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("Erreur Virement");
    		alert.setContentText(e.getMessage());
    		alert.setHeaderText("Erreur Virement");
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
