
public abstract class Nappula {
	
	public final Vari vari;
	
	private int[] sijainti = new int[2];
	
	public Nappula(Vari vari, int[] sijainti){
		this.vari = vari;
		this.sijainti = sijainti;
		
	}
	
	
	
	public abstract boolean voikoLiikkuaRuutuun();
	
	public int[] annaSijainti(){
		return sijainti;
	}
	
	public void asetaSijainti(int[] sijainti){
		this.sijainti = sijainti;
	}
	
	
}
