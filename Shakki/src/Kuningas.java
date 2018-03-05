
public class Kuningas extends Nappula{
	
	public boolean onkoLiikkunut = false;
	public boolean linnoitus = false;
	
	public Kuningas(Vari vari, int[] sijainti, Lauta lauta) {
		super(vari, sijainti, lauta);
	}
	
	/*
	 * Tarkistaa, voiko kuningas siirty� omasta ruudustaan annettuun ruutuun.
	 * @param  ruutu
	 */
	
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu) {
		
		//Tarkistetaan ett� annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			return false;
		}
		
		//Kuningas voi liikkua mihin tahansa suuntaan yhden ruudun verran.
		int[] siirtyma = new int[2];
		
		siirtyma[0] = (int) Math.abs(sijainti[0] - ruutu[0]);
		siirtyma[1] = (int) Math.abs(sijainti[1] - ruutu[1]);
		
		if(lauta.annaNappula(ruutu) != null && lauta.annaNappula(ruutu).vari == vari) {
			return false; // omaa ei voi sy�d�
		}
		//Kuningas ei saa siirty� uhattuun ruutuun
		Vari uhka = vari == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN;
		if(lauta.uhattuRuutu(uhka, ruutu)) {
			return false;
		}
		
		
		//Siirryt��n vinottain yksi ruutu
		if(siirtyma[0] == 1 && siirtyma[1] == 1) {
			return true;
		}
		
		//Siirryt��n vaakarivilt� toiselle 
		else if(siirtyma[1] == 1 && siirtyma[0] == 0) {
			return true;
		}
		//Pystysarakkeelta toiselle
		else if(siirtyma[0] == 1 && siirtyma[1] == 0) {
			return true;
		}
		
		//Jos siirto on linnoitus (kuningasta ollaan siirt�m�ss� kahden askelen verran), t�ytyy tarkastaa, ett�
		//-kuningas ja torni eiv�t ole viel� liikkuneet
		//-kunigas ei ole shakissa
		//-kuningas ei liiku uhatun ruudun yli
		//-kuninkaan ja tornin v�liss� ei ole nappuloita.
		
		//Ei saa olla liikkunut aiemmin eik� saa olla shakissa
		if(siirtyma[0] == 2 && !onkoLiikkunut && !lauta.uhattuRuutu(uhka, sijainti)) {
			
			Nappula torni = null;
			
			if(sijainti[0] == 5 && sijainti[1] == 1) { //valkea kuningas
				
				if(ruutu[0] == 7 && ruutu[1] == 1) { //lyhyt linnoitus (oikealle)
					torni = lauta.annaNappula(new int[] {8,1});
					if(lauta.uhattuRuutu(uhka, new int[] {6,1})) { //kuninkaan ylitt�m� ruutu ei saa olla uhattu
						return false;
					}
				}
				else if(ruutu[0] == 3 && ruutu[1] == 1) { //pitk� linnoitus (vasemmalle)
					torni = lauta.annaNappula(new int[] {1,1});
					if(lauta.uhattuRuutu(uhka, new int[] {4,1})) { // kuninkaan ylitt�m� ruutu ei saa olla uhattu
						return false;
					}
				}
				
				
				//Torni ei saa kuulua vastustajalle, olla jokin muu kuin torni, olla null tai olla liikkunut
				if(torni == null || !(torni instanceof Torni)|| torni.vari == uhka || ((Torni) torni).onkoLiikkunut) {
					return false;
				}
					
				//V�liss� ei saa olla nappuloita
				if(!lauta.tarkistaSiirtolinja(sijainti, torni.annaSijainti())) {
					return false;
				}
				
				return true;
			}
			
			if(sijainti[0] == 5 && sijainti[1] == 8) { //musta kuningas
				
				if(ruutu[0] == 7 && ruutu[1] == 8) { //lyhyt linnoitus (oikealle)
					torni = lauta.annaNappula(new int[] {8,8});
					if(lauta.uhattuRuutu(uhka, new int[] {6,8})) { //kuninkaan ylitt�m� ruutu ei saa olla uhattu
						return false;
					}
				}
				else if(ruutu[0] == 3 && ruutu[1] == 8) { //pitk� linnoitus (vasemmalle)
					torni = lauta.annaNappula(new int[] {1,8});
					if(lauta.uhattuRuutu(uhka, new int[] {4,8})) { // kuninkaan ylitt�m� ruutu ei saa olla uhattu
						return false;
					}
				}
				
				
				//Torni ei saa kuulua vastustajalle, olla jokin muu kuin torni, olla null tai olla liikkunut
				if(torni == null || !(torni instanceof Torni)|| torni.vari == uhka || ((Torni) torni).onkoLiikkunut) {
					return false;
				}
					
				//V�liss� ei saa olla nappuloita
				if(!lauta.tarkistaSiirtolinja(sijainti, torni.annaSijainti())) {
					return false;
				}
				
				return true;
			}
			
			
			
			
			
		}
		
		
		
		return false;
		
	}
	
	@Override
	public String toString(){
		String palautus = "K";
		
		palautus = ((vari == Vari.VALKOINEN) ? "v" : "m") + palautus;
		
		return palautus;
		
	}
	
}
