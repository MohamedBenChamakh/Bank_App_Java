package application.exceptions;

public class ErrNegatif extends Exception{
	
	public ErrNegatif() {
		super("le solde est negatif");
	}
	
	public ErrNegatif(String msg) {
		super(msg);
	}
}
