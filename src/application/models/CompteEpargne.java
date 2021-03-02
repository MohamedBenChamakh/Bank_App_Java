package application.models;


import java.time.LocalDate;
import java.time.Period;


import application.exceptions.ErrNegatif;


public class CompteEpargne extends CompteAbstrait{
	  LocalDate date_dernier_versement;
	  double montant_dernier_versement;
	  boolean actif=true;
	  LocalDate date_creation;
	
	
	public CompteEpargne(long rib, double solde, Agence agence, double montant, Client client,LocalDate date_dernier_versement,LocalDate date_creation) throws ErrNegatif {
		super(rib, solde, agence, client);
		this.date_creation=date_creation;
		montant_dernier_versement=montant;
		this.date_dernier_versement=date_dernier_versement;
	}

	
	
	  public LocalDate getDate_dernier_versement() {
		return date_dernier_versement;
	}

	public void setDate_dernier_versement(LocalDate date_dernier_versement) {
		this.date_dernier_versement = date_dernier_versement;
	}

	public double getMontant_dernier_versement() {
		return montant_dernier_versement;
	}

	public void setMontant_dernier_versement(double montant_dernier_versement) {
		this.montant_dernier_versement = montant_dernier_versement;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	public LocalDate getDate_creation() {
		return date_creation;
	}

	public void setDate_creation(LocalDate date_creation) {
		this.date_creation = date_creation;
	}


	  //double somme_montants_mois;
	


	@Override
	public boolean versement(double montant)  {
		
		
		if(actif) {
			LocalDate date_jour= LocalDate.now();
			Period p= Period.between(date_dernier_versement, date_jour);
			
			if(p.getYears()==0 && p.getMonths()==0 ) {
				montant_dernier_versement+=montant;
				setSolde(getSolde()+montant);
				agence.crediter(montant); 
				date_dernier_versement=date_jour;
				return true;
				
			}
			if(p.getYears()==0 && p.getMonths()==1) {
				
				if(montant_dernier_versement>=20) {
				// verser le montant
					setSolde(getSolde()+montant);
					agence.crediter(montant); 
					date_dernier_versement=date_jour;
					montant_dernier_versement=montant;
					return true;
				}
				else {
				// bloquer le compte
					actif=false;
					System.out.println("Compte épargne bloqué ");
					return false;
				}
			
			} 
			else {
				actif=false;
				System.out.println("Compte épargne bloqué ");
				return false;
			}
			
		}
		else {System.out.println("Compte épargne déjà bloqué ");
			return false;
		}
		
		
		
		
		
		
	}

	@Override
	public boolean retrait(double montant)  throws ErrNegatif{
		// TODO Auto-generated method stub
		LocalDate date_jour= LocalDate.now();
		Period p= Period.between(date_creation, date_jour);
		if((p.getYears()>=1)&& getSolde()>=2000) {
			if(getSolde()>= montant)
			{setSolde(getSolde()-montant);
			 agence.debiter(montant );
			 return true;
			}
			
			
			if(getSolde()<0) throw new ErrNegatif();
			return false;
		}
		
		return false;
	}






	@Override
	public String toString() {
		return "CompteEpargne [date_dernier_versement=" + date_dernier_versement + ", montant_dernier_versement="
				+ montant_dernier_versement + ", actif=" + actif + ", date_creation=" + date_creation + ", rib=" + rib
				+ ", solde=" + solde + ", agence=" + agence + ", client=" + client + "]";
	}



	@Override
	public boolean virement(CompteAbstrait c, double montant) throws ErrNegatif {
		// TODO Auto-generated method stub
		if(c.retrait(montant))
		return versement(montant);
		return false;
		
	}

}
