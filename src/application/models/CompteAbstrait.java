package application.models;


import java.time.LocalDate;

import application.exceptions.ErrNegatif;

public abstract class CompteAbstrait {
	
	 
	    protected long rib;
		
		protected double solde;
		protected Agence agence;
		protected Client client;
		
		public CompteAbstrait(long rib, double solde, Agence agence, Client c) throws ErrNegatif{
			
			this.solde = solde;
			this.rib = rib;
			client=c;
			this.agence = agence;
			
			
		}
		
		
		public static boolean compare(CompteAbstrait c1, CompteAbstrait c2) {
			return (c1.solde<= c2.solde);
		}
		
		
		public boolean compare (CompteAbstrait c) {
			return (this.solde<= c.solde);
		}
		
		public long getRib() {
			return rib;
		}

		public void setRib(long rib) {
			this.rib = rib;
		}

		

		public double getSolde() {
			return solde;
		}

		public void setSolde(double solde) {
			this.solde = solde;
		}

		public Agence getAgence() {
			return agence;
		}

		public void setAgence(Agence agence) {
			this.agence = agence;
		}
		public abstract boolean versement(double montant);
		public  abstract boolean retrait(double montant)throws ErrNegatif;
		public abstract boolean virement(CompteAbstrait c, double montant)throws ErrNegatif;
		
	

}
