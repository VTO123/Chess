
public class Sotilas extends Nappula{
	
	public Sotilas enPassantSotilas = null; //Sotilas, joka viittaa ohestalyönnin aikana lyötävään sotilaaseen.
	
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
			return false;
		}
		
		//suoraan liikuttaessa ei voi syödä
		if(lauta.annaNappula(ruutu) == null && ruutu[0] == sijainti[0]) {
			
			//Valkoinen sotilas voi normaalisti liikkua yhden ruudun ylöspäin
			if(vari == Vari.VALKOINEN && ruutu[1] - sijainti[1] == 1) {
				return true;
			}
			
			//Musta sotilas voi normaalisti liikkua yhden ruudun alaspäin
			else if(vari == Vari.MUSTA && sijainti[1] - ruutu[1] == 1) {
				return true;
			}
			
			int aloitusRivi = (vari == Vari.VALKOINEN) ? 2 : 7;
			
			//Ensimmäinen siirto voi olla kaksi ruutua
			if(annaSijainti()[1] == aloitusRivi && Math.abs(ruutu[1] - sijainti[1]) == 2  && lauta.tarkistaSiirtolinja(sijainti, ruutu)){
				
				//kohderuudussa ei saa olla nappulaa
				if(lauta.annaNappula(ruutu) != null) {
					return false;
				}
				
				return true;
				
			}
		}
		//Syönti tapahtuu vinoon yhden ruudun verran
		else if((ruutu[0] == sijainti[0] + 1 || ruutu[0] == sijainti[0] - 1) && ruutu[1] == sijainti[1] + ((lauta.vuoro == Vari.VALKOINEN) ? 1 : -1)){
			
			//Syönti onnistuu vain, jos kohderuudussa on vastustajan nappula
			Nappula kohdeNappula = lauta.annaNappula(ruutu);
			if(kohdeNappula != null && kohdeNappula.vari != lauta.vuoro) {
				return true;
			}
			
			/*
			 *Jos syöntiruudussa ei ole nappulaa, kyseessä voi olla ohestalyönti.
			 *Ohestalyönti tarkoittaa aloitusriviltään kaksi askelta liikkuneen sotilaan lyömistä omalla sotilaalla, jonka rinnalle
			 *lyötävä sotilas siirtyi. Lyönti tehdään lyötävän sotilaan takana olevaan ruutuun ikään kuin tämä olisi liikkunut vain yhden ruudun.
			 *Ohestalyönnin voi suorittaa vain välittömästi seuraavalla siirrolla lyötävän sotilaan siirtymisestä.
			 */
			if(kohdeNappula == null) {
				
				/*
				 *Vastustajan edellisen siirron tulee olla tapahtunut tämän sotilaan viereiseen ruutuun linjalle jolle ollaan lyömässä,
				 *vastustajan sotilaiden aloitusriviltä kahden ruudun verran
				 * lauta.edellinenSiirto[1] sisältää edellisen siirron kohderuudun ja [0] lähtöruudun
				 */
				if(lauta.edellinenSiirto[1][0] == ruutu[0] && lauta.annaNappula(lauta.edellinenSiirto[1]) instanceof Sotilas
						&& lauta.annaNappula(lauta.edellinenSiirto[1]).vari != vari) {
					
					//Lyötävän sotilaan tulee olla liikkunut kaksi askelta
					if(Math.abs(lauta.edellinenSiirto[0][1] - lauta.edellinenSiirto[1][1]) == 2) {
						enPassantSotilas = (Sotilas) lauta.annaNappula(lauta.edellinenSiirto[1]);
						return true;
						
					}
					
				}
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
