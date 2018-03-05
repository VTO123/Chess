import java.io.Serializable;

public abstract class Nappula implements Comparable<Nappula>, Serializable{
	
	public final Vari vari;
	protected final Lauta lauta;
	
	//Sijainti: ensimm‰inen arvo vastaa pystyrivin kirjainta a-h ja toinen vaakarivin numeroa 1-8
	protected int[] sijainti = new int[2];
	
	public Nappula(Vari vari, int[] sijainti, Lauta lauta){
		this.vari = vari;
		this.lauta = lauta;
		if(sijainti[0] < 1 || sijainti[1] > 8
				|| sijainti[1] < 1 || sijainti[1] > 8){
			throw new IllegalArgumentException("Nappula ulkona laudalta!");
		}
		this.sijainti = sijainti;
		
	}
	
	
	
	public abstract boolean voikoLiikkuaRuutuun(int[] ruutu);
	
	
	/**
	 * Palauttaa nappulan sijainnin.
	 * 
	 * @return sijaintikoordinaatit
	 */
	
	public int[] annaSijainti(){
		return sijainti;
	}
	
	/**
	 * Asettaa nappulan sijainnin parametrin sijainti mukaiseksi.
	 * Heitt‰‰ poikkeuksen jos nappulaa yritet‰‰n asettaa laudan ulkopuolelle.
	 * 
	 * @param sijainti
	 * @throws IllegalArgumentException
	 */
	public void asetaSijainti(int[] sijainti){
		if(sijainti[0] < 1 || sijainti[1] > 8
				|| sijainti[1] < 1 || sijainti[1] > 8){
			throw new IllegalArgumentException("Nappula ulkona laudalta!");
		}
		this.sijainti = sijainti;
	}
	
	
	
	/**
	 * Vertailee nappuloita sijainnin perusteella. Ensin vaakarivin perusteella
	 * ylh‰‰lt‰ alasp‰in ja sitten pystysarakkeen perusteella vasemmalta oikealle.
	 * 
	 * @param nappula
	 * @return 1 jos t‰m‰ ol
	 */
	
	@Override
	public int compareTo(Nappula toinen){
		
		
		//Jos ollaan ylemm‰ll‰ vaakarivill‰ kuin toinen nappula
		if(this.sijainti[1] > toinen.annaSijainti()[1]){
			return -1;
		}
		//Jos ollaan alemmalla
		else if(this.sijainti[1] < toinen.annaSijainti()[1]){
			return 1;
		}
		
		//Jos ollaan samalla rivill‰ katsotaan sarake
		if(this.sijainti[0] < toinen.annaSijainti()[0]){
			return -1;
		}
		return 1;
		
	}
	
	
}
