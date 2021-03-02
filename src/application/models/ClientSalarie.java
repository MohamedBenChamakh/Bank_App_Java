package application.models;

public class ClientSalarie extends Client{
	
	
	double salaire;
	public ClientSalarie(String nom, String prenom, String cin, String profession, double salaire) {
		super(nom, prenom, cin, profession);
		// TODO Auto-generated constructor stub
		this.salaire=salaire;
		
	}
	@Override
	public String toString() {
		return "ClientSalarie [salaire=" + salaire + ", nom=" + nom + ", prenom=" + prenom + ", cin=" + cin
				+ ", profession=" + profession + "]";
	}
	public double getSalaire() {
		return salaire;
	}
	public void setSalaire(double salaire) {
		this.salaire = salaire;
	}
	
	

	
	

}
