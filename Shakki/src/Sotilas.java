
public class Sotilas extends Nappula{
	
	public Sotilas(Vari vari, int[] sijainti) {
		super(vari, sijainti);
	}
	
	
	/*
	 * Tarkistaa, voiko sotilas normaalisti liikuttaessa
	 * siirty� annettuun ruutuun.
	 * Erikoistapaukset (ensimm�inen liikkuminen, sy�nti, ohily�nti)
	 * t�ytyy katsoa erikseen lauta-luokassa jos t�m� metodi palauttaa
	 * arvon false
	 * @param ruutu
	 */
	
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu) {

		//Tarkistetaan ett� annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			throw new IllegalArgumentException("Kohderuutu laudan ulkopuolella!");
		}
		
		//Valkoinen sotilas voi normaalisti liikkua yhden ruudun yl�sp�in
		if(vari == Vari.VALKOINEN && ruutu[1] - sijainti[1] == 1 && ruutu[0] == sijainti[0]) {
			return true;
		}
		
		//Musta sotilas voi normaalisti liikkua yhden ruudun alasp�in
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
