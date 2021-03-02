package application.models;

public class ClientVIP extends Client {
	double chiffre_affaire;
	String nom_entreprise;
	int nbre_employes;
	
	public ClientVIP(String nom, String prenom, String cin, String profession,
			double chiffre_affaire,String nom_entreprise,int nbre_employes ) {
		super(nom, prenom, cin, profession);
		this.chiffre_affaire=chiffre_affaire;
		this.nom_entreprise=nom_entreprise;
		this.nbre_employes=nbre_employes;
	}

	
	public double getChiffre_affaire() {
		return chiffre_affaire;
	}


	public void setChiffre_affaire(double chiffre_affaire) {
		this.chiffre_affaire = chiffre_affaire;
	}


	public String getNom_entreprise() {
		return nom_entreprise;
	}


	public void setNom_entreprise(String nom_entreprise) {
		this.nom_entreprise = nom_entreprise;
	}


	public int getNbre_employes() {
		return nbre_employes;
	}


	public void setNbre_employes(int nbre_employes) {
		this.nbre_employes = nbre_employes;
	}


	@Override
	public String toString() {
		return "ClientVIP [chiffre_affaire=" + chiffre_affaire + ", nom_entreprise=" + nom_entreprise
				+ ", nbre_employes=" + nbre_employes + ", nom=" + nom + ", prenom=" + prenom + ", cin=" + cin
				+ ", profession=" + profession + "]";
	}

	
	
	

	
	

}
