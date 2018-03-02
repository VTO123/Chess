
public class Torni extends Nappula{
	
	public Torni(Vari vari, int[] sijainti, Lauta lauta){
		super(vari, sijainti, lauta);
	}
	
	
	
	/*
	 * Tarkistaa voiko torni liikkua omasta sijainnistaan annettuun ruutuun.
	 * @param ruutu
	 * @return boolean
	 */
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu){
		//Torni voi liikkua vain suoraan riviä tai saraketta pitkin.
		
		//Tarkistetaan että annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			throw new IllegalArgumentException("Kohderuutu laudan ulkopuolella!");
		}
		
		if(lauta.annaNappula(ruutu) != null && lauta.annaNappula(ruutu).vari == vari) {
			return false; // omaa ei voi syödä
		}
		
		
		if(ruutu[0] == sijainti[0] && ruutu[1] != sijainti[1]){
			
			if(! (lauta.tarkistaSiirtolinja(sijainti, ruutu))) {
				return false; //nappuloiden yli ei voi hypätä
			}
			return true;
		}
		else if(ruutu[0] != sijainti[0] && ruutu[1] == sijainti[1]){
			
			if(! (lauta.tarkistaSiirtolinja(sijainti, ruutu))) {
				return false; //nappuloiden yli ei voi hypätä
			}
			return true;
			
		}
		return false;
		
	}
	
	@Override
	public String toString(){
		String palautus = "T";
		
		palautus = ((vari == Vari.VALKOINEN) ? "v" : "m") + palautus;
		
		return palautus;
		
	}
	
}
