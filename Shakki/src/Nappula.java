
public abstract class Nappula {
	
	public final Vari vari;
	
	private int[] sijainti = new int[2];
	
	public Nappula(Vari vari, int[] sijainti){
		this.vari = vari;
		if(sijainti[0] < 0 || sijainti[1] > 7
				|| sijainti[1] < 0 || sijainti[1] > 7){
			throw new IllegalArgumentException("Nappula ulkona laudalta!");
		}
		this.sijainti = sijainti;
		
	}
	
	
	
	public abstract boolean voikoLiikkuaRuutuun();
	
	public int[] annaSijainti(){
		return sijainti;
	}
	
	public void asetaSijainti(int[] sijainti){
		if(sijainti[0] < 0 || sijainti[1] > 7
				|| sijainti[1] < 0 || sijainti[1] > 7){
			throw new IllegalArgumentException("Nappula ulkona laudalta!");
		}
		this.sijainti = sijainti;
	}
	
	
}
