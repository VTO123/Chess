
public class Ratsu extends Nappula{
	
	public Ratsu(Vari vari, int[] sijainti, Lauta lauta) {
		super(vari, sijainti, lauta);
	}
	
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu) {
		
		//Tarkistetaan ett� annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			return false;
		}
		
		if(lauta.annaNappula(ruutu) != null && lauta.annaNappula(ruutu).vari == vari) {
			return false; // omaa ei voi sy�d�
		}
		
		//Ratsu siirtyy yhteen suuntaan kaksi ruutua ja sitten kohtisuoraan yhden ruudun.
		int[] siirtyma = new int[2];
		
		siirtyma[0] = (int) Math.abs(sijainti[0] - ruutu[0]);
		siirtyma[1] = (int) Math.abs(sijainti[1] - ruutu[1]);
		
		
		//Kaksi ruutua vaakaan ja yksi pystyyn
		if(siirtyma[0] == 2 && siirtyma[1] == 1) {
			return true;
		}
		//Kaksi ruutua pystyyn ja yksi vaakaan
		else if(siirtyma[1] == 2 && siirtyma[0] == 1) {
			return true;
		}
		
		return false;
		
	}
	
	@Override
	public String toString(){
		String palautus = "R";
		
		palautus = ((vari == Vari.VALKOINEN) ? "v" : "m") + palautus;
		
		return palautus;
		
	}
	
}
