import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Lauta implements Serializable {

	private ArrayList<Nappula> nappulat;
	private Vari vuoro;
	
	public Lauta() {
		nappulat = new ArrayList<>();
		vuoro = Vari.VALKOINEN;
		alustaNappulat();
	}
	
	private void poistaNappula(int[] koord) {
		for(Nappula n : nappulat) {
			if(n.annaSijainti()[0] == koord[0] && n.annaSijainti()[1] == koord[1]) {
				nappulat.remove(n);
				return;
			}
		}
	}
	
	public void pelaaVuoro() {
		
	}
	
	private void liikutaNappulaa(int[] lahto, int[] kohde) {
		
	}
	
	private void piirraLauta() {
		
	}
	
	public void tallennaLauta() {
		
	}
	
	public void lataaLauta() {
		
	}
	
	private void alustaNappulat() {
		nappulat.add(new Torni(Vari.MUSTA, new int[] {1, 8}));
		nappulat.add(new Ratsu(Vari.MUSTA, new int[] {2, 8}));
		nappulat.add(new Lahetti(Vari.MUSTA, new int[] {3, 8}));
		nappulat.add(new Kuningatar(Vari.MUSTA, new int[] {4, 8}));
		nappulat.add(new Kuningas(Vari.MUSTA, new int[] {5, 8}));
		nappulat.add(new Lahetti(Vari.MUSTA, new int[] {6, 8}));
		nappulat.add(new Ratsu(Vari.MUSTA, new int[] {7, 8}));
		nappulat.add(new Torni(Vari.MUSTA, new int[] {8, 8}));
		for(int i = 1; i < 9; i++) {
			nappulat.add(new Sotilas(Vari.MUSTA, new int[] {i, 7}));
		}
		for(int i = 1; i < 9; i++) {
			nappulat.add(new Sotilas(Vari.VALKOINEN, new int[] {i, 2}));
		}
		nappulat.add(new Torni(Vari.VALKOINEN, new int[] {1, 1}));
		nappulat.add(new Ratsu(Vari.VALKOINEN, new int[] {2, 1}));
		nappulat.add(new Lahetti(Vari.VALKOINEN, new int[] {3, 1}));
		nappulat.add(new Kuningatar(Vari.VALKOINEN, new int[] {4, 1}));
		nappulat.add(new Kuningas(Vari.VALKOINEN, new int[] {5, 1}));
		nappulat.add(new Lahetti(Vari.VALKOINEN, new int[] {6, 1}));
		nappulat.add(new Ratsu(Vari.VALKOINEN, new int[] {7, 1}));
		nappulat.add(new Torni(Vari.VALKOINEN, new int[] {8, 1}));
	}
}
