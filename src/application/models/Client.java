package application.models;

public class Client {
	
	String nom, prenom, cin, profession;

	public Client(String nom, String prenom, String cin, String profession) {
		
		this.nom = nom;
		this.prenom = prenom;
		this.cin = cin;
		this.profession = profession;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}
	

	
	
	
	

}
