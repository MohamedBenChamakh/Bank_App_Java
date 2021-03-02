package application.controllers;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import application.DB_Connection;
import application.Main;
import application.exceptions.ClientNotFoundException;
import application.exceptions.ErrNegatif;
import application.models.Client;
import application.models.ClientSalarie;
import application.models.ClientVIP;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ListClientController implements Initializable{

    @FXML
    private Button exitBtn;

    @FXML
    private TableView<List<String>> table;

    @FXML
    private TableColumn<List<String>, String> nomCol;

    @FXML
    private TableColumn<List<String>, String> prenomCol;

    @FXML
    private TableColumn<List<String>, String> cinCol;

    @FXML
    private TableColumn<List<String>, String> typeCol;

    @FXML
    private TextField rechercheText;

    @FXML
    private Button rechercheBtn;

    @FXML
    void exit(MouseEvent event) throws Exception {
    	Main.listeClient.clear();
    	Main.agence=null;
    	Main.showLoginStage();
    }

    @FXML
    void rechercher(MouseEvent event) throws Exception {
    	try {
    		
	    	int i=findElementByCin(rechercheText.getText());
	    	//System.out.println(i);
	    	if(i!=-1) {
	    		
	    		Main.showListAccount(Main.listeClient.get(i));
	    		
	    	}else throw new ClientNotFoundException("Le Cin que vous avez saissi ne correspond à aucun compte");
    	 }catch(ClientNotFoundException e) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
        		alert.setTitle("Client introuvable");
        		alert.setContentText(e.getMessage());
        		alert.setHeaderText(null);
        		alert.showAndWait();
    	 }
    }
    
    public int findElementByCin(String element) {
    	int i=0;
    	for(Client client : Main.listeClient) {
    		//System.out.println(client.getCin().equals(element.get(2)));
    		if(client.getCin().equals(element)) {
    		//	System.out.println(client.getCin());
    			return i;
    		}else
    			i++;
    	}
    	
		return -1;
	
    	
    }

    @FXML
    void afficheClient(MouseEvent event) throws NumberFormatException, SQLException, ErrNegatif, Exception {
    	int i=0;
     	for(List<String> element: table.getSelectionModel().getSelectedItems()) {
    		System.out.println(element);
         	i=findElementByCin(element.get(2));
    		if(i!=-1) {
	    	    Main.showListAccount(Main.listeClient.get(i));
    		}
    	}
    }

    
    public void initList() throws SQLException {
    	DB_Connection c=new DB_Connection();
		Connection connexion=c.getConnection();
		ResultSet resultat=null;
		PreparedStatement ps;
		
		ps =connexion.prepareStatement("SELECT * FROM clientSalarie,agence WHERE agence.ID_AGENCE=clientSalarie.ID_AGENCE AND agence.NOM_AGENCE=? ");
		ps.setString(1, Main.agence.getNom_agence());
    	
		
		resultat=ps.executeQuery();
		
		 
    	ObservableList<List<String>> data=FXCollections.observableArrayList();

		
		while(resultat.next()) {
			data.add(new ArrayList<>(Arrays.asList(resultat.getString("NOM"),resultat.getString("PRENOM"),resultat.getString("CIN"),"Salarié")));
			Main.listeClient.add(new ClientSalarie(resultat.getString("NOM"),resultat.getString("PRENOM"),resultat.getString("CIN"),resultat.getString("PROFESSION"),resultat.getDouble("SALAIRE")));
		}
		
		
		ps =connexion.prepareStatement("SELECT * FROM clientVIP,agence WHERE agence.ID_AGENCE=clientVIP.ID_AGENCE AND agence.NOM_AGENCE=? ");
		ps.setString(1, Main.agence.getNom_agence());
    	
		
		resultat=ps.executeQuery();
		
		
		while(resultat.next()) {
			data.add(new ArrayList<>(Arrays.asList(resultat.getString("NOM"),resultat.getString("PRENOM"),resultat.getString("CIN"),"Vip")));
			Main.listeClient.add(new ClientVIP(resultat.getString("NOM"),resultat.getString("PRENOM"),resultat.getString("CIN"),resultat.getString("PROFESSION"),resultat.getDouble("CHIFFRE_AFFAIRE"), resultat.getString("NOM_ENTREPRISE") ,resultat.getInt("NBRE_EMPLOYES")));
			
		}
		
		table.setItems(data);

    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		nomCol.setCellValueFactory(
				new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			    @Override
			    public ObservableValue<String> call(CellDataFeatures<List<String>, String> data) {
			        return new ReadOnlyStringWrapper(data.getValue().get(0)) ;
			    }
			}
			) ;
		prenomCol.setCellValueFactory(
					new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
					    @Override
					    public ObservableValue<String> call(CellDataFeatures<List<String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get(1)) ;
					    }
					}) ;
		cinCol.setCellValueFactory(
					new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
					    @Override
					    public ObservableValue<String> call(CellDataFeatures<List<String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get(2)) ;
					    }
					}
					) ;
		typeCol.setCellValueFactory(
					new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
					    @Override
					    public ObservableValue<String> call(CellDataFeatures<List<String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get(3)) ;
					    }
					}
					) ;
	}
    
}
