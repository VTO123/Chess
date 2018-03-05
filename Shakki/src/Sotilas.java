
public class Sotilas extends Nappula{
	
	public Sotilas enPassantSotilas = null; //Sotilas, joka viittaa ohestaly�nnin aikana ly�t�v��n sotilaaseen.
	
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
			return false;
		}
		
		//suoraan liikuttaessa ei voi sy�d�
		if(lauta.annaNappula(ruutu) == null && ruutu[0] == sijainti[0]) {
			
			//Valkoinen sotilas voi normaalisti liikkua yhden ruudun yl�sp�in
			if(vari == Vari.VALKOINEN && ruutu[1] - sijainti[1] == 1) {
				return true;
			}
			
			//Musta sotilas voi normaalisti liikkua yhden ruudun alasp�in
			else if(vari == Vari.MUSTA && sijainti[1] - ruutu[1] == 1) {
				return true;
			}
			
			int aloitusRivi = (vari == Vari.VALKOINEN) ? 2 : 7;
			
			//Ensimm�inen siirto voi olla kaksi ruutua
			if(annaSijainti()[1] == aloitusRivi && Math.abs(ruutu[1] - sijainti[1]) == 2  && lauta.tarkistaSiirtolinja(sijainti, ruutu)){
				
				//kohderuudussa ei saa olla nappulaa
				if(lauta.annaNappula(ruutu) != null) {
					return false;
				}
				
				return true;
				
			}
		}
		//Sy�nti tapahtuu vinoon yhden ruudun verran
		else if((ruutu[0] == sijainti[0] + 1 || ruutu[0] == sijainti[0] - 1) && ruutu[1] == sijainti[1] + ((lauta.vuoro == Vari.VALKOINEN) ? 1 : -1)){
			
			//Sy�nti onnistuu vain, jos kohderuudussa on vastustajan nappula
			Nappula kohdeNappula = lauta.annaNappula(ruutu);
			if(kohdeNappula != null && kohdeNappula.vari != lauta.vuoro) {
				return true;
			}
			
			/*
			 *Jos sy�ntiruudussa ei ole nappulaa, kyseess� voi olla ohestaly�nti.
			 *Ohestaly�nti tarkoittaa aloitusrivilt��n kaksi askelta liikkuneen sotilaan ly�mist� omalla sotilaalla, jonka rinnalle
			 *ly�t�v� sotilas siirtyi. Ly�nti tehd��n ly�t�v�n sotilaan takana olevaan ruutuun ik��n kuin t�m� olisi liikkunut vain yhden ruudun.
			 *Ohestaly�nnin voi suorittaa vain v�litt�m�sti seuraavalla siirrolla ly�t�v�n sotilaan siirtymisest�.
			 */
			if(kohdeNappula == null) {
				
				/*
				 *Vastustajan edellisen siirron tulee olla tapahtunut t�m�n sotilaan viereiseen ruutuun linjalle jolle ollaan ly�m�ss�,
				 *vastustajan sotilaiden aloitusrivilt� kahden ruudun verran
				 * lauta.edellinenSiirto[1] sis�lt�� edellisen siirron kohderuudun ja [0] l�ht�ruudun
				 */
				if(lauta.edellinenSiirto[1][0] == ruutu[0] && lauta.annaNappula(lauta.edellinenSiirto[1]) instanceof Sotilas
						&& lauta.annaNappula(lauta.edellinenSiirto[1]).vari != vari) {
					
					//Ly�t�v�n sotilaan tulee olla liikkunut kaksi askelta
					if(Math.abs(lauta.edellinenSiirto[0][1] - lauta.edellinenSiirto[1][1]) == 2) {
						enPassantSotilas = (Sotilas) lauta.annaNappula(lauta.edellinenSiirto[1]);
						return true;
						
					}
					
				}
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
