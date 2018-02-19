
public class Torni extends Nappula{
	
	public Torni(Vari vari, int[] sijainti){
		super(vari, sijainti);
	}
	
	
	
	/*
	 * Tarkistaa voiko torni liikkua omasta sijainnistaan annettuun ruutuun.
	 * @param ruutu
	 * @return boolean
	 */
	@Override
	public boolean voikoLiikkuaRuutuun(int[] ruutu){
		//Torni voi liikkua vain suoraan riviä tai saraketta pitkin.
		if(ruutu[0] == sijainti[0] && ruutu[1] != sijainti[1]){
			if(ruutu[1] >= 1 && ruutu[1] <= 8){
				return true;
			}
			throw new IllegalArgumentException("Kohderuutu laudan ulkopuolella!");
		}
		else if(ruutu[0] != sijainti[0] && ruutu[1] == sijainti[1]){
			if(ruutu[0] >= 1 && ruutu[0] <= 8){
				return true;
			}
			throw new IllegalArgumentException("Kohderuutu laudan ulkopuolella!");
		}
		return false;
		
	}
	
}
