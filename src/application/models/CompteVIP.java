package application.models;

import application.exceptions.ErrNegatif;
import application.exceptions.ErrSoldeRouge;

public class CompteVIP extends CompteAbstrait {
	
	

	public CompteVIP(long rib, double solde, Agence agence, ClientVIP c) throws ErrNegatif {
		super(rib, solde, agence, c);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean versement(double montant) {
		// TODO Auto-generated method stub
		setSolde(getSolde()+montant);
		agence.crediter(montant); 
		return true;
		
	}

	@Override
	public String toString() {
		return "CompteVIP [chiffre_affaire=" + ((ClientVIP)(client)).chiffre_affaire + ", rib=" + rib + ", solde=" + solde + ", agence="
				+ agence + ", client=" + client + "]";
	}

	@Override
	public boolean retrait(double montant) throws ErrSoldeRouge{
		// TODO Auto-generated method stub
		
		double rest=solde-montant;
		if(solde-montant>= - (0.05*((ClientVIP)(client)).chiffre_affaire) )
		{
			setSolde(getSolde()-montant);
			 agence.debiter(montant );
			 return true;
		}
		
		
		throw new ErrSoldeRouge();
	}

	@Override
	public boolean virement(CompteAbstrait c, double montant) throws ErrSoldeRouge {
		// TODO Auto-generated method stub
		if(retrait(montant))
		{
			c.versement(montant);
			return true;
		}
		return false;

	}

}
