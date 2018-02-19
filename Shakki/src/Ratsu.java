
public class Ratsu extends Nappula{
	
	public Ratsu(Vari vari, int[] sijainti) {
		super(vari, sijainti);
	}
	
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu) {
		
		//Tarkistetaan että annettu ruutu on laudalla
		if(ruutu[0] < 1 || ruutu[0] > 8 || ruutu[1] < 1 || ruutu[1] > 8) {
			throw new IllegalArgumentException("Kohderuutu laudan ulkopuolella!");
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
	
}
