
public class Kuningas extends Nappula{
	
	public Kuningas(Vari vari, int[] sijainti) {
		super(vari, sijainti);
	}
	
	/*
	 * Tarkistaa, voiko kuningas siirty‰ omasta ruudustaan annettuun ruutuun.
	 * @param  ruutu
	 */
	
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu) {
		
		//Tarkistetaan ett‰ annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			throw new IllegalArgumentException("Kohderuutu laudan ulkopuolella!");
		}
		
		//Kuningas voi liikkua mihin tahansa suuntaan yhden ruudun verran.
		int[] siirtyma = new int[2];
		
		siirtyma[0] = (int) Math.abs(sijainti[0] - ruutu[0]);
		siirtyma[1] = (int) Math.abs(sijainti[1] - ruutu[1]);
		
		//Siirryt‰‰n vinottain yksi ruutu
		if(siirtyma[0] == 1 && siirtyma[1] == 1) {
			return true;
		}
		
		//Siirryt‰‰n vaakarivilt‰ toiselle 
		else if(siirtyma[1] == 1 && siirtyma[0] == 0) {
			return true;
		}
		//Pystysarakkeelta toiselle
		else if(siirtyma[0] == 1 && siirtyma[1] == 0) {
			return true;
		}
		
		return false;
		
	}
	
}
