
public class Sotilas extends Nappula{
	
	public Sotilas(Vari vari, int[] sijainti, Lauta lauta) {
		super(vari, sijainti, lauta);
	}
	
	
	/*
	 * Tarkistaa, voiko sotilas
	 * siirty� annettuun ruutuun.
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
		
		int aloitusRivi = (vari == Vari.VALKOINEN) ? 2 : 7;
		
		//Ensimm�inen siirto voi olla kaksi ruutua
		if(annaSijainti()[1] == aloitusRivi && sijainti[0] == ruutu[0] && Math.abs(ruutu[1] - sijainti[1]) == 2  && lauta.tarkistaSiirtolinja(sijainti, ruutu)){
			
			//kohderuudussa ei saa olla nappulaa
			if(lauta.annaNappula(ruutu) != null) {
				return false;
			}
			
			return true;
			
		}
		
		//Sy�nti tapahtuu vinoon yhden ruudun verran
		else if((ruutu[0] == sijainti[0] + 1 || ruutu[0] == sijainti[0] - 1) && ruutu[1] == sijainti[1] + ((lauta.vuoro == Vari.VALKOINEN) ? 1 : -1)){
			
			//Sy�nti onnistuu vain, jos kohderuudussa on vastustajan nappula
			Nappula kohdeNappula = lauta.annaNappula(ruutu);
			if(kohdeNappula == null) {
				return false;
			}
			else if(kohdeNappula.vari != lauta.vuoro) {
				return true;
			}
			return false;
		}
		
		//Ei voida siirty� ruutuun, palautetaan false.
		return false;
		
		
	}
	
	@Override
	public String toString(){
		String palautus = "S";
		
		palautus = ((vari == Vari.VALKOINEN) ? "v" : "m") + palautus;
		
		return palautus;
		
	}
	
}
