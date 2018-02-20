
public class Sotilas extends Nappula{
	
	public Sotilas(Vari vari, int[] sijainti) {
		super(vari, sijainti);
	}
	
	
	/*
	 * Tarkistaa, voiko sotilas normaalisti liikuttaessa
	 * siirtyä annettuun ruutuun.
	 * Erikoistapaukset (ensimmäinen liikkuminen, syönti, ohilyönti)
	 * täytyy katsoa erikseen lauta-luokassa jos tämä metodi palauttaa
	 * arvon false
	 * @param ruutu
	 */
	
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu) {

		//Tarkistetaan että annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			throw new IllegalArgumentException("Kohderuutu laudan ulkopuolella!");
		}
		
		//Valkoinen sotilas voi normaalisti liikkua yhden ruudun ylöspäin
		if(vari == Vari.VALKOINEN && ruutu[1] - sijainti[1] == 1 && ruutu[0] == sijainti[0]) {
			return true;
		}
		
		//Musta sotilas voi normaalisti liikkua yhden ruudun alaspäin
		else if(vari == Vari.MUSTA && sijainti[1] - ruutu[1] == 1 && ruutu[0] == sijainti[0]) {
			return true;
		}
		
		return false;
		
		
	}
	
	@Override
	public String toString(){
		String palautus = "S";
		
		palautus = (vari == Vari.VALKOINEN) ? "v" : "m" + palautus;
		
		return palautus;
		
	}
	
}
