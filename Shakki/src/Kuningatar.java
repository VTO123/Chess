
public class Kuningatar extends Nappula{
	
	public Kuningatar(Vari vari, int[] sijainti) {
		super(vari, sijainti);
	}
	
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu) {
		
		//Tarkistetaan ett‰ annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			throw new IllegalArgumentException("Kohderuutu laudan ulkopuolella!");
		}
		
		//Kuningatar voi liikkua vinottain tai suoraan.
		int[] siirtyma = new int[2];
		
		siirtyma[0] = (int) Math.abs(sijainti[0] - ruutu[0]);
		siirtyma[1] = (int) Math.abs(sijainti[1] - ruutu[1]);
		
		//Siirryt‰‰n vinottain, mutta omaan ruutuun ei voi siirty‰
		if(siirtyma[0] == siirtyma[1] && siirtyma[0] != 0) {
			return true;
		}
		
		//Suoraan siirtymiset
		else if(siirtyma[1] != 0 && siirtyma[0] == 0) {
			return true;
		}
		else if(siirtyma[0] != 0 && siirtyma[1] == 0) {
			return true;
		}
		
		return false;
		
	}
	
}
