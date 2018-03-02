
public class Sotilas extends Nappula{
	
	public Sotilas(Vari vari, int[] sijainti, Lauta lauta) {
		super(vari, sijainti, lauta);
	}
	
	
	/*
	 * Tarkistaa, voiko sotilas
	 * siirtyä annettuun ruutuun.
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
		
		int aloitusRivi = (vari == Vari.VALKOINEN) ? 2 : 7;
		
		//Ensimmäinen siirto voi olla kaksi ruutua
		if(annaSijainti()[1] == aloitusRivi && sijainti[0] == ruutu[0] && Math.abs(ruutu[1] - sijainti[1]) == 2  && lauta.tarkistaSiirtolinja(sijainti, ruutu)){
			
			//kohderuudussa ei saa olla nappulaa
			if(lauta.annaNappula(ruutu) != null) {
				return false;
			}
			
			return true;
			
		}
		
		//Syönti tapahtuu vinoon yhden ruudun verran
		else if((ruutu[0] == sijainti[0] + 1 || ruutu[0] == sijainti[0] - 1) && ruutu[1] == sijainti[1] + ((lauta.vuoro == Vari.VALKOINEN) ? 1 : -1)){
			
			//Syönti onnistuu vain, jos kohderuudussa on vastustajan nappula
			Nappula kohdeNappula = lauta.annaNappula(ruutu);
			if(kohdeNappula == null) {
				return false;
			}
			else if(kohdeNappula.vari != lauta.vuoro) {
				return true;
			}
			return false;
		}
		
		//Ei voida siirtyä ruutuun, palautetaan false.
		return false;
		
		
	}
	
	@Override
	public String toString(){
		String palautus = "S";
		
		palautus = ((vari == Vari.VALKOINEN) ? "v" : "m") + palautus;
		
		return palautus;
		
	}
	
}
