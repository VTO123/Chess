
public class Kuningatar extends Nappula{
	
	public Kuningatar(Vari vari, int[] sijainti, Lauta lauta) {
		super(vari, sijainti, lauta);
	}
	
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu) {
		
		//Tarkistetaan että annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			return false;
		}
		
		//Kuningatar voi liikkua vinottain tai suoraan.
		int[] siirtyma = new int[2];
		
		siirtyma[0] = (int) Math.abs(sijainti[0] - ruutu[0]);
		siirtyma[1] = (int) Math.abs(sijainti[1] - ruutu[1]);
		
		if(lauta.annaNappula(ruutu) != null && lauta.annaNappula(ruutu).vari == vari) {
			return false; // omaa ei voi syödä
		}
		
		
		//Siirrytään vinottain, mutta omaan ruutuun ei voi siirtyä
		if(siirtyma[0] == siirtyma[1] && siirtyma[0] != 0) {
			if(! (lauta.tarkistaSiirtolinja(sijainti, ruutu))) {
				return false; //nappuloiden yli ei voi hypätä
			}
			return true;
		}
		
		//Suoraan siirtymiset
		else if(siirtyma[1] != 0 && siirtyma[0] == 0) {
			if(! (lauta.tarkistaSiirtolinja(sijainti, ruutu))) {
				return false; //nappuloiden yli ei voi hypätä
			}
			return true;
		}
		else if(siirtyma[0] != 0 && siirtyma[1] == 0) {
			if(! (lauta.tarkistaSiirtolinja(sijainti, ruutu))) {
				return false; //nappuloiden yli ei voi hypätä
			}
			return true;
		}
		
		
		return false;
		
	}
	
	@Override
	public String toString(){
		String palautus = "D";
		
		palautus = ((vari == Vari.VALKOINEN) ? "v" : "m") + palautus;
		
		return palautus;
		
	}
	
}
