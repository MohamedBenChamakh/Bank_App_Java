package application.models;

import java.util.ArrayList;
import java.util.Comparator;

import application.exceptions.ErrNegatif;
import application.exceptions.ErrSoldeRouge;

public class Compte extends CompteAbstrait {
   

	public Compte(long rib, double solde, Agence agence, ClientSalarie c) throws ErrNegatif {
		super(rib, solde, agence, c);
		// TODO Auto-generated constructor stub
	}


	
	public static boolean compare(Compte c1, Compte c2) {
		return (c1.solde<= c2.solde);
	}
	
	
	public boolean compare (Compte c) {
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

	public boolean versement(double montant) {
		
		setSolde(getSolde()+montant);
		agence.crediter(montant); 
		return true;
	}
	public boolean retrait(double montant) throws ErrSoldeRouge
	{
		if((getSolde()-montant)> - 0.05* ((ClientSalarie)(this.client)).salaire) 
		{setSolde(getSolde()-montant);
		 agence.debiter(montant );
		 
		 return true;
		}
		
		throw new ErrSoldeRouge();	
	

	}
	public boolean virement(CompteAbstrait c, double montant)throws ErrSoldeRouge {
		if(retrait(montant))
		{c.versement(montant); return true;}
		return false;

	
	}
	
	
	public static void tri_comptes(CompteAbstrait[] tab, int n) {
		
		
		boolean tri=false;
		CompteAbstrait Inter;
		while ( tri==false) {
			    tri=true;
			for( int i=0; i<n -1; i++)
			{
				if(compare(tab[i], tab[i+1])==false) {
					tri=false;
					
					Inter= tab[i];
					tab[i]=tab[i+1];
					tab[i+1]= Inter;
				}
			}
			
		}
		
		
		
		
	}
	
	
	
	
	public static void Afficher_tab_comptes(CompteAbstrait[] tab, int n) {
	
		for (int i=0; i< n; i++ ) {
			//double sold=tab[i].solde;
		   //System.out.format("%.2f  \n", Double.valueOf(tab[i].toString()));	
			System.out.println(tab[i]);
			
		}
}

	public static void Afficher_tab_comptes( ArrayList <CompteAbstrait> list) {
		
		for (CompteAbstrait compte : list ) {
			//double sold=tab[i].solde;
		   System.out.format("%.2f  \n", Double.valueOf(compte.toString()));	
			//System.out.println(tab[i]);
			
		}
}
	
	public static void tri_comptes(ArrayList<Compte> liste) {
		// TODO Auto-generated method stub
		
		boolean tri=false;
		Compte Inter;
		while ( tri==false) {
			    tri=true;
			for( int i=0; i< liste.size() -1; i++)
			{
				if(compare(liste.get(i), liste.get(i+1))==false) {
					tri=false;
					
					Inter= liste.get(i);
					liste.set(i, liste.get(i+1));
					liste.set(i+1,Inter);
				}
			}
			
		}
		
		
		
		
	}

	
	public static void tri_comptes2(ArrayList<Compte> liste) {
		// TODO Auto-generated method stub
		
		Comparator<? super Compte> c= new Comparator< Compte>() {

			@Override
			public int compare(Compte c1, Compte c2) {
				if(c1.solde>=c2.solde)
				return 1;
				return -1;
			}
		};
		liste.sort(c);
		
		
	}


	@Override
	public String toString() {
		return "Compte [rib=" + rib +  ", solde=" + solde + ", agence=" + agence + ", client=" + client
				+ "]";
	}

	

	/*public ResultatRecherche TotalSoldes(Compte[] tab , int n) {
		boolean trouve=false;
		double somme=0;
	for( int i=0; i< n; i++ )
	{
		if(tab[i].cin==this.cin) {
			somme+=tab[i].solde;
			trouve=true;
		}
	}
	
	
	ResultatRecherche r=new ResultatRecherche(trouve, somme);
	
	return r;
		
		
	}*/					
	
	
	
}
