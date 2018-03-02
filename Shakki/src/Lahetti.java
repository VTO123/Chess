
public class Lahetti extends Nappula{

	public Lahetti(Vari vari, int[] sijainti, Lauta lauta){
		super(vari, sijainti, lauta);
	}
	
	
	/*
	 * Tarkistaa, voiko lähetti siirtyä omasta ruudustaan annettuun ruutuun.
	 * @param ruutu
	 */
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu) {
		
		//Tarkistetaan että annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			throw new IllegalArgumentException("Kohderuutu laudan ulkopuolella!");
		}
		
		if(lauta.annaNappula(ruutu) != null && lauta.annaNappula(ruutu).vari == vari) {
			return false; // omaa ei voi syödä
		}
		
		//Lähetti voi liikkua vain vinottain.
		//Siten vaakasuuntaisen muutoksen itseisarvon tulee
		//vastata pystysuuntaisen muutoksen itseisarvoa.
		int[] siirtyma = new int[2];
		
		siirtyma[0] = (int) Math.abs(sijainti[0] - ruutu[0]);
		siirtyma[1] = (int) Math.abs(sijainti[1] - ruutu[1]);
		
		//Siirtymä ei saa olla myöskään nolla
		if(siirtyma[0] == siirtyma[1] && siirtyma[0] != 0) {
			
			if(! (lauta.tarkistaSiirtolinja(sijainti, ruutu))) {
				return false; //nappuloiden yli ei voi hypätä
			}
			return true;
		}
		
		return false;
		
	}
	
	@Override
	public String toString(){
		String palautus = "L";
		
		palautus = ((vari == Vari.VALKOINEN) ? "v" : "m") + palautus;
		
		return palautus;
		
	}
	
}
