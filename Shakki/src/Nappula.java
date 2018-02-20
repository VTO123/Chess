
public abstract class Nappula implements Comparable<Nappula>{
	
	public final Vari vari;
	
	//Sijainti: ensimm�inen arvo vastaa pystyrivin kirjainta a-h ja toinen vaakarivin numeroa
	protected int[] sijainti = new int[2];
	
	public Nappula(Vari vari, int[] sijainti){
		this.vari = vari;
		if(sijainti[0] < 1 || sijainti[1] > 8
				|| sijainti[1] < 1 || sijainti[1] > 8){
			throw new IllegalArgumentException("Nappula ulkona laudalta!");
		}
		this.sijainti = sijainti;
		
	}
	
	
	
	public abstract boolean voikoLiikkuaRuutuun(int[] ruutu);
	
	public int[] annaSijainti(){
		return sijainti;
	}
	
	public void asetaSijainti(int[] sijainti){
		if(sijainti[0] < 1 || sijainti[1] > 8
				|| sijainti[1] < 1 || sijainti[1] > 8){
			throw new IllegalArgumentException("Nappula ulkona laudalta!");
		}
		this.sijainti = sijainti;
	}
	
	/*
	 * Vertailee nappuloita sijainnin perusteella. Ensin vaakarivin perusteella
	 * ylh��lt� alasp�in ja sitten pystysarakkeen perusteella vasemmalta oikealle.
	 * 
	 * @param nappula
	 */
	
	
	@Override
	public int compareTo(Nappula toinen){
		
		
		//Jos ollaan ylemm�ll� vaakarivill� kuin toinen nappula
		if(this.sijainti[1] > toinen.annaSijainti()[1]){
			return -1;
		}
		//Jos ollaan alemmalla
		else if(this.sijainti[1] < toinen.annaSijainti()[1]){
			return 1;
		}
		
		//Jos ollaan samalla rivill� katsotaan sarake
		if(this.sijainti[0] < toinen.annaSijainti()[0]){
			return -1;
		}
		return 1;
		
	}
	
	
}
