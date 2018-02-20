import java.io.Serializable;
import java.util.ArrayList;
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
		
	}
	
	private void poistaNappula(int[] koord) {
		for(Nappula n : nappulat) {
			if(n.annaSijainti()[0] == koord[0] && n.annaSijainti()[1] == koord[1]) {
				nappulat.remove(n);
				return;
			}
		}
	}
	
	
	
	
	/*
	 * Palauttaa true ja siirtää lahto- koordinaatissa olevan nappulan kohde- koordinaattiin,
	 * jos annettu siirto on laillinen. Muulloin palauttaa false.
	 * Tämä metodi hoitaa nappuloiden syömisen tarvittaessa.
	 */
	
	private boolean liikutaNappulaa(int[] lahto, int[] kohde) {
		//Tarkistetaan onko annetussa lähtöruudussa nappula
		Nappula siirrettava = null;
		for(Nappula n : nappulat){
			if(n.annaSijainti()[0] == lahto[0] && n.annaSijainti()[1] == lahto[1]){
				siirrettava = n;
				//Loppuja nappuloita ei tarvitse tarkistaa
				break;
			}
		}
		
		//Tyhjästä ruudusta tai vastustajan nappulaa ei voi siirtää
		if(siirrettava == null || siirrettava.vari != vuoro){
			return false;
		}
		
		if(siirrettava.voikoLiikkuaRuutuun(kohde)){
			//Tämä suoritetaan, jos siirto on sallittu nappulan omien liikkumissääntöjen puitteissa
			//(poikkeus: sotilaan erikoissiirrot syöminen, ensimmäinen liikkuminen, ohilyönti)
			
			//Jos lähtö- ja kohderuudun välissä on nappuloita, ei voi siirtää (paitsi ratsua)
			if(!tarkistaSiirtolinja(lahto, kohde) && ! (siirrettava instanceof Ratsu)){
				return false;
			}
			
			//Tarkistetaan onko kohderuudussa nappulaa;
			//oma estää siirtymisen ja vastustaja syödään
			Nappula kohdeNappula = null;
			for(Nappula n : nappulat){
				if(n.annaSijainti()[0] == kohde[0] && n.annaSijainti()[1] == kohde[1]){
					kohdeNappula = n;
					//Loppuja nappuloita ei tarvitse tarkistaa
					break;
				}
			}
			
			//Jos kohderuudussa ei nappulaa, siirretään.
			if(kohdeNappula == null){
				siirrettava.asetaSijainti(kohde);
				return true;
			}
			
			//Vastustaja syödään ja siirretään siirrettävää nappulaa
			else if(kohdeNappula.vari != vuoro){
				poistaNappula(kohde);
				siirrettava.asetaSijainti(kohde);
				return true;
			}
			//Oman nappulan päälle ei voi siirtää
			else{
				return false;
			}
			
		}
		else if(siirrettava instanceof Sotilas){
			//Sotilaalla on omat erikoissiirtonsa.
			
			//Sotilas voi siirtyä kaksi ruutua eteenpäin aloitusriviltään
			//Valkoiset sotilaat aloittavat riviltä 2 ja mustat riviltä 7.
			int aloitusRivi = (siirrettava.vari == Vari.VALKOINEN) ? 2 : 7;
			
			//Jos sotilasta ei ole vielä siirretty sitä voi siirtää kaksi ruutua
			if(siirrettava.annaSijainti()[1] == aloitusRivi && lahto[0] == kohde[0] && tarkistaSiirtolinja(lahto, kohde)){
				
				//kohderuudussa ei saa olla nappulaa
				for(Nappula n : nappulat){
					if(n.annaSijainti()[0] == kohde[0] && n.annaSijainti()[1] == kohde[1]){
						return false;
					}
				}
			}
			
			
			//Kyseessä voi myös olla syönti, jonka sotilas suorittaa vinoon
			
			//Valkoinen sotilas liikkuu ylöspäin laudalla
			if(vuoro == Vari.VALKOINEN){
				if((kohde[0] == lahto[0] + 1 || kohde[0] == lahto[0] - 1) && kohde[1] == lahto[1] + 1){
					//Onnistuu vain jos kohderuudussa on vastustajan nappula
					for(Nappula n : nappulat){
						if(n.annaSijainti()[0] == kohde[0] && n.annaSijainti()[1] == kohde[1]){
							if(n.vari != vuoro){
								poistaNappula(kohde);
								siirrettava.asetaSijainti(kohde);
								return true;
							}
							return false;	//Omaa nappulaa ei voi lyödä
						}
					}
					
				}
			}
			//Musta sotilas liikkuu alaspäin laudalla
			else if(vuoro == Vari.MUSTA){
				if((kohde[0] == lahto[0] + 1 || kohde[0] == lahto[0] - 1) && kohde[1] == lahto[1] - 1){
					//Onnistuu vain jos kohderuudussa on vastustajan nappula
					for(Nappula n : nappulat){
						if(n.annaSijainti()[0] == kohde[0] && n.annaSijainti()[1] == kohde[1]){
							if(n.vari != vuoro){
								poistaNappula(kohde);
								siirrettava.asetaSijainti(kohde);
								return true;
							}
							return false;	//Omaa nappulaa ei voi lyödä
						}
					}
					
				}
			}
			
			//TODO en passant?
			
			return false;
			
		}
		else{
			//Laiton siirto
			return false;
		}
	}
	
	
	/*
	 * Palauttaa true, jos lähtö- ja kohderuudun välissä ei ole nappuloita.
	 */
	private boolean tarkistaSiirtolinja(int[] lahto, int[] kohde){
		
		//Siirtymäalkio kertoo siirroksen suunnan.
		int[] siirtymaAlkio = new int[2];
		
		if(kohde[0] == lahto[0]){
			//Siirrytään vaakariviltä toiselle
			siirtymaAlkio[1] = (kohde[1] - lahto[1] > 0) ? 1 : -1;
		}
		else if(kohde[1] == lahto[1]){
			//Siirrytään pystysarakkeelta toiselle
			siirtymaAlkio[0] = (kohde[0] - lahto[0] > 0) ? 1 : -1;

		}else{
			//Siirrytään vinottain
			siirtymaAlkio[0] = (kohde[0] - lahto[0] > 0) ? 1 : -1;
			siirtymaAlkio[1] = (kohde[1] - lahto[1] > 0) ? 1 : -1;
		}
		
		
		int[] tarkistettavaRuutu = new int[2];
		tarkistettavaRuutu[0] = lahto[0] + siirtymaAlkio[0];
		tarkistettavaRuutu[1] = lahto[1] + siirtymaAlkio[1];

		while(!(tarkistettavaRuutu[0] == kohde[0] && tarkistettavaRuutu[1] == kohde[1])){
			//toistetaan kunnes ollaan kohderuudussa
			
			//Tarkistetaan tarkistettava ruutu nappuloiden varalta.
			for(Nappula n : nappulat){
				
				//Palautetaan false jos jokin nappula on tarkistettavassa ruudussa
				if(n.annaSijainti()[0] == tarkistettavaRuutu[0] && n.annaSijainti()[1] == tarkistettavaRuutu[1]){
					return false;
				}
			}
			
			//Päivitetään tarkistettava ruutu
			tarkistettavaRuutu[0] = lahto[0] + siirtymaAlkio[0];
			tarkistettavaRuutu[1] = lahto[1] + siirtymaAlkio[1];
		}
		//Jos missään siirtymälinjalle osuvassa ruudussa ei ollut nappulaa, palautetaan true.
		return true;
		
	}
	
	
	//@return : return[0] == lähtökoordinaatti + return[1] == kohdekoordinaatti
	private int[][] kysySiirto() {
		Scanner scanner = new Scanner(System.in);
		String vari = vuoro == Vari.VALKOINEN ? "Valkoinen" : "Musta"; 
		System.out.println(vari + " anna siirto:");
		
		//A1 A2 lähtöruutu *väli* kohderuutu
		//@TODO : savegame tallentaa pelin
		String syote = scanner.nextLine();
		scanner.close();
		
		syote = syote.toUpperCase();
		int[][] siirto = new int[2][2];
		
		String tarkastus = "ABCDEFGH";
		if(!(tarkastus.contains(syote.charAt(0) + "") && tarkastus.contains(syote.charAt(3) + ""))){
			System.out.println("Virheellinen syöte");
			return kysySiirto();
		}
		
		try {
		siirto[0][0] = koordMuunnos.get(syote.charAt(0));
		siirto[0][1] = Integer.parseInt(syote.charAt(1) + "");
		siirto[1][0] = koordMuunnos.get(syote.charAt(3));
		siirto[1][1] = Integer.parseInt(syote.charAt(4) + "");
		}catch(NumberFormatException e){
			System.out.println("Virheellinen syöte");
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
					rivi += " " +nappulat.get(index).toString();
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

