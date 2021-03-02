package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;



import application.DB_Connection;
import application.Main;
import application.exceptions.ErrNegatif;
import application.models.Client;
import application.models.ClientSalarie;
import application.models.ClientVIP;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;

import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;


public class ListeComptesController implements Initializable{

	private Client client;

    @FXML
    private TableView<List<String>> table;
    

    
    @FXML
    private TableColumn<List<String>, String> ribCol;

    @FXML
    private TableColumn<List<String>, String> soldeCol;

    @FXML
    private TableColumn<List<String>, String> typeCol;
    
    @FXML
    private TableColumn<List<String>, String> dateCol;

    @FXML
    private Button deleteAccount;

    @FXML
    private Label salaireLabel;

    @FXML
    private Text salaireText;

    @FXML
    private Label nomEntrepriseLabel;

    @FXML
    private Label chiffreAffaireLabel;

    @FXML
    private Label nbEmployesLabel;

    @FXML
    private Text agenceText;

    @FXML
    private Label nomText;

    @FXML
    private Text nomEntrepriseText;

    @FXML
    private Text chiffreAffaireText;

    @FXML
    private Text nbEmployesText;
    
    
    @FXML
    private Button deconnexion;


    
    
    
    @FXML
    void afficheCompte(MouseEvent event) throws IOException, SQLException, NumberFormatException, ErrNegatif {
    	for(List<String> element: table.getSelectionModel().getSelectedItems()) {
    		//System.out.println(element);
    	
        	Main.showInfoCompte(this.client,element);
    	}

    }
    
    public void initClient(Client client) throws SQLException, NumberFormatException, ErrNegatif {
    	this.client=client;
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
    	DB_Connection c=new DB_Connection();
		Connection connexion=c.getConnection();
		ResultSet resultat=null;
		PreparedStatement ps;
		
    	if(client instanceof ClientSalarie) {
    		salaireText.setText(Double.toString(((ClientSalarie) this.client).getSalaire()));
    		nomEntrepriseLabel.setVisible(false);
    		nomEntrepriseText.setVisible(false);
    		chiffreAffaireLabel.setVisible(false);
    		chiffreAffaireText.setVisible(false);
    		nbEmployesLabel.setVisible(false);
    		nbEmployesText.setVisible(false);
    		ps =connexion.prepareStatement("SELECT * FROM compte,clientSalarie,agence WHERE agence.ID_AGENCE=compte.ID_AGENCE AND clientSalarie.ID_CLIENT=compte.ID_CLIENT  AND clientSalarie.cin=? ");
    	}
    	else {
    		this.nbEmployesText.setText(Integer.toString(((ClientVIP) this.client).getNbre_employes()));
    		this.chiffreAffaireText.setText(Double.toString(((ClientVIP) this.client).getChiffre_affaire()));
    		this.nomEntrepriseText.setText(((ClientVIP) this.client).getNom_entreprise());
    		salaireText.setVisible(false);
    		salaireLabel.setVisible(false);
    		ps =connexion.prepareStatement("SELECT * FROM comptevip,clientvip,agence WHERE agence.ID_AGENCE=comptevip.ID_AGENCE AND clientvip.ID_CLIENT=comptevip.ID_CLIENT  AND clientvip.cin=? ");
    	}
    	
    	ps.setString(1, this.client.getCin());
    	resultat=ps.executeQuery();

    
    	ObservableList<List<String>> data=FXCollections.observableArrayList();

	
		if(resultat.next()) {
			if(client instanceof ClientSalarie) 
				data.add(new ArrayList<>(Arrays.asList("Normal",resultat.getString("RIB"),Double.toString(resultat.getDouble("SOLDE")),dateFormat.format(resultat.getDate("DATE_CREATION")))));
			else
				data.add(new ArrayList<>(Arrays.asList("Vip",resultat.getString("RIB"),Double.toString(resultat.getDouble("SOLDE")),dateFormat.format(resultat.getDate("DATE_CREATION")))));
			

		}
		
		ps = null;

    	if(client instanceof ClientSalarie) {
    		ps =connexion.prepareStatement("SELECT * FROM compteepargne,clientSalarie,agence WHERE agence.ID_AGENCE=compteepargne.ID_AGENCE AND clientSalarie.ID_CLIENT=compteepargne.ID_CLIENT  AND clientSalarie.cin=? ");
    	}
    	else {
    		ps =connexion.prepareStatement("SELECT * FROM compteepargne,clientvip,agence WHERE agence.ID_AGENCE=compteepargne.ID_AGENCE AND clientvip.ID_CLIENT=compteepargne.ID_CLIENT  AND clientvip.cin=? ");
    	}
    	
    	ps.setString(1, this.client.getCin());
    	resultat=ps.executeQuery();
    	
    	if(resultat.next()) {
			data.add(new ArrayList<>(Arrays.asList("Epargne",resultat.getString("RIB"),Double.toString(resultat.getDouble("SOLDE")),dateFormat.format(resultat.getDate("DATE_CREATION")))));
			
    	}
    	connexion.close();
    	
		nomText.setText(this.client.getPrenom()+" "+this.client.getNom());
	 	agenceText.setText(Main.agence.getNom_agence());
	//	System.out.println(data.toString());
		table.setItems(data);

	
    }


    @FXML
    void addAccount(MouseEvent event) throws IOException {
    	Main.showCreeCompte(client);
    }
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
			ribCol.setCellValueFactory(
				new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
			    @Override
			    public ObservableValue<String> call(CellDataFeatures<List<String>, String> data) {
			        return new ReadOnlyStringWrapper(data.getValue().get(1)) ;
			    }
			}
			) ;
			soldeCol.setCellValueFactory(
					new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
					    @Override
					    public ObservableValue<String> call(CellDataFeatures<List<String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get(2)) ;
					    }
					}) ;
			typeCol.setCellValueFactory(
					new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
					    @Override
					    public ObservableValue<String> call(CellDataFeatures<List<String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get(0)) ;
					    }
					}
					) ;
			dateCol.setCellValueFactory(
					new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
					    @Override
					    public ObservableValue<String> call(CellDataFeatures<List<String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get(3)) ;
					    }
					}
					) ;
		
		
	}
	
	
    @FXML
    void LogOut(ActionEvent event) throws Exception {
    	((Stage)deconnexion.getScene().getWindow()).close();
    }
	

}
