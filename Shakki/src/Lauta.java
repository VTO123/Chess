import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Lauta implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Nappula> nappulat;
	private Vari vuoro;
	private HashMap<Character, Integer> koordMuunnos;
	
	public Lauta() {
		nappulat = new ArrayList<>();
		vuoro = Vari.VALKOINEN;
		alustaNappulat();
		alustaKoordMuunnos();
	}
	
	public void pelaaVuoro() {
		boolean siirtoOnnistui = false;
		
		while(! siirtoOnnistui) {
			int[][] siirto = kysySiirto();
			int[] lahto = siirto[0];
			int[] kohde = siirto[1];
			
			if(liikutaNappulaa(lahto, kohde)) {
				siirtoOnnistui = true;
			}
		}
		
		piirraLauta();
		if(onkoShakkiMatti()) {
			System.out.println("Shakki ja Matti");
		}
		
		vuoro = vuoro == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN;
		
	}
	
	private void poistaNappula(int[] koord) {
		for(Nappula n : nappulat) {
			if(n.annaSijainti()[0] == koord[0] && n.annaSijainti()[1] == koord[1]) {
				nappulat.remove(n);
				return;
			}
		}
	}
	
	private void liikutaNappulaa(int[] lahto, int[] kohde) {
		
	}
	
	//palauttaa true jos shakkimatti
	private boolean onkoShakkiMatti() {
		Nappula kuningas = null;
		for(Nappula n : nappulat) {
			if(n instanceof Kuningas && n.vari != vuoro) {
				kuningas = n;
				break;
			}
		}
		
		Vari uhka = kuningas.vari == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN;
		int[] alkupSijainti = kuningas.annaSijainti();
		int[] mahdSijainti = Arrays.copyOf(alkupSijainti, 2);
		
		if(!uhattuRuutu(uhka, alkupSijainti)) {
			return false;
		}
		
		for(int r = -1; r < 2; r++) {
			for(int s = -1; r < 2; s++) {
				mahdSijainti[0] += r;
				mahdSijainti[1] += s;
				if(kuningas.voikoLiikkuaRuutuun(sijainti) && tarkistaSiirtolinja(alkupSijainti, mahdSijainti)) {
					if(!uhattuRuutu(uhka, mahdSijainti)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	// Palauttaa true jos annetun v�rinen nappula pystyy sy�m��n nappulan annetusta ruudusta
	//@param uhkaaja : v�ri joka voi sy�d� nappulan annetusta ruudusta
	//@param ruutu : ruutu jonka uhka tarkistetaan
	private boolean uhattuRuutu(Vari uhkaaja, int[] ruutu) {
		for(Nappula n : nappulat) {
			if(n.vari == uhkaaja) {
				if(n.voikoLiikkuaRuutuun(ruutu)) {
					if(tarkistaSiirtolinja(n.annaSijainti(), ruutu)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//@return : return[0] == l�ht�koordinaatti + return[1] == kohdekoordinaatti
	private int[][] kysySiirto() {
		Scanner scanner = new Scanner(System.in);
		String vari = vuoro == Vari.VALKOINEN ? "Valkoinen" : "Musta"; 
		System.out.println(vari + " anna siirto:");
		
		//A1 A2 l�ht�ruutu *v�li* kohderuutu
		//TODO savegame tallentaa pelin
		String syote = scanner.nextLine();
		scanner.close();
		
		syote = syote.toUpperCase();
		int[][] siirto = new int[2][2];
		
		String tarkastus = "ABCDEFGH";
		if(!(tarkastus.contains(syote.charAt(0) + "") && tarkastus.contains(syote.charAt(3) + ""))){
			System.out.println("Virheellinen sy�te");
			return kysySiirto();
		}
		
		try {
		siirto[0][0] = koordMuunnos.get(syote.charAt(0));
		siirto[0][1] = Integer.parseInt(syote.charAt(1) + "");
		siirto[1][0] = koordMuunnos.get(syote.charAt(3));
		siirto[1][1] = Integer.parseInt(syote.charAt(4) + "");
		}catch(NumberFormatException e){
			System.out.println("Virheellinen sy�te");
			return kysySiirto();
		}
		
		return siirto;
	}

	public void piirraLauta() {
		Collections.sort(nappulat);
		int index = 0;
		System.out.println("  -----------------------------------------");
		for(int r = 8; r > 0; r--) {	
			String rivi = r + " |";
			for(int s = 1; s < 9; s++) {
				int[] sij = nappulat.get(index).annaSijainti();
				if(sij[0] == s && sij[1] == r) {
					rivi += " " + nappulat.get(index).toString();
					rivi += " |";
					if(index < nappulat.size()) {
						index++;
					}
				}else {
					rivi += "    |";
				}
			}
			System.out.println(rivi);
			System.out.println("  -----------------------------------------");
		}
		System.out.println("    a    b    c    d    e    f    g    h");
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

	private void alustaKoordMuunnos() {
		koordMuunnos = new HashMap<>();
		koordMuunnos.put('A', 1);
		koordMuunnos.put('B', 2);
		koordMuunnos.put('C', 3);
		koordMuunnos.put('D', 4);
		koordMuunnos.put('E', 5);
		koordMuunnos.put('F', 6);
		koordMuunnos.put('G', 7);
		koordMuunnos.put('H', 8);
	}
}

