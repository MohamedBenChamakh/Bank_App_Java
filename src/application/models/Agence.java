package application.models;

public class Agence {
	double debit_agence_global;
	double credit_agence_global;
	static double debit_banque_global;
	static double credit_banque_global;
	String nom_agence;
	
	public Agence(String nom_agence, double debit_agence_global, double credit_agence_global) {
		super();
		this.debit_agence_global = debit_agence_global;
		this.credit_agence_global = credit_agence_global;
		this.nom_agence = nom_agence;
	}
	public void crediter(double montant) {
		// TODO Auto-generated method stub
		
		credit_agence_global+=montant;
		credit_banque_global+=montant;
	}
	public void debiter(double montant) {
		// TODO Auto-generated method stub
		debit_agence_global+=montant;
		debit_banque_global+=montant;
	}
	
	public double getDebit_agence_global() {
		return debit_agence_global;
	}
	public void setDebit_agence_global(double debit_agence_global) {
		this.debit_agence_global = debit_agence_global;
	}
	
	public double getCredit_agence_global() {
		return credit_agence_global;
	}
	public void setCredit_agence_global(double credit_agence_global) {
		this.credit_agence_global = credit_agence_global;
	}

	
	public String getNom_agence() {
		return nom_agence;
	}
	public void setNom_agence(String nom_agence) {
		this.nom_agence = nom_agence;
	}
	@Override
	public String toString() {
		return "Agence [debit_agence_global=" + debit_agence_global + ", credit_agence_global=" + credit_agence_global
				+ ", nom_agence=" + nom_agence + "]";
	}
	
	
	

}
