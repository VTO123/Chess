import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Lauta implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Nappula> nappulat;
	public Vari vuoro;
	private HashMap<Character, Integer> koordMuunnos;
	
	public Lauta() {
		nappulat = new ArrayList<>();
		vuoro = Vari.VALKOINEN;
		alustaNappulat();
		alustaKoordMuunnos();
	}
	
	public void pelaaVuoro() {
		boolean siirtoOnnistui = false;
		
		//Kysyt‰‰n syˆte ja yritet‰‰n toimia sen mukaan kunnes onnistutaan
		while(! siirtoOnnistui) {
			int[][] siirto = kysySiirto();
			int[] lahto = siirto[0];
			int[] kohde = siirto[1];
			
			if(liikutaNappulaa(lahto, kohde)) {
				siirtoOnnistui = true;
			}else {
				System.out.println("Siirto ei mahdollinen!");
			}
		}
		
		//Piirret‰‰n lauta ja tarkistataan mahdollinen shakkimatti
		piirraLauta();
		if(onkoShakkiMatti()) {
			System.out.println("Shakki ja Matti");
		}
		
		//Vaihdetaan vuorossa olevaa v‰ri‰
		vuoro = vuoro == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN;
		
	}
	
	//Palauttaa nappulan annetusta sijainnista jos sijainnissa ei ole nappulaa palauttaa null
	//@param koord koordinatti josta nappulaa etsit‰‰n
	//@return nappula joka on sijainnissa koord tai null jos sijainnissa ei ole nappulaa
	Nappula annaNappula(int[] koord) {
		Nappula etsitty = null;
		for(Nappula n : nappulat) {
			if(n.annaSijainti()[0] == koord[0] && n.annaSijainti()[1] == koord[1]) {
				etsitty = n;
				break;
			}
		}
		return etsitty;
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
	 * Palauttaa true ja siirt‰‰ lahto- koordinaatissa olevan nappulan kohde- koordinaattiin,
	 * jos annettu siirto on laillinen. Muulloin palauttaa false.
	 * T‰m‰ metodi hoitaa nappuloiden syˆmisen tarvittaessa.
	 */
	
	private boolean liikutaNappulaa(int[] lahto, int[] kohde) {
		//Tarkistetaan onko annetussa l‰htˆruudussa nappula
		Nappula siirrettava = annaNappula(lahto);
		
		//Tyhj‰st‰ ruudusta tai vastustajan nappulaa ei voi siirt‰‰
		if(siirrettava == null || siirrettava.vari != vuoro){
			return false;
		}
		
		if(siirrettava.voikoLiikkuaRuutuun(kohde)){
			//T‰m‰ suoritetaan, jos siirto on sallittu nappulan omien liikkumiss‰‰ntˆjen puitteissa
			//ja laudan nappulatilanne sallii siirron
			
			Nappula syotava = annaNappula(kohde);
			//Jos kohderuudussa on nappula, sen on pakko olla vastustaja, koska voikoLiikkuaRuutuun-
			//metodi palauttaa false jos yritet‰‰n syˆd‰ omaa.
			if(syotava != null) {
				poistaNappula(kohde);
			}
			siirrettava.asetaSijainti(kohde);
			return true;
		}
		else{
			//Nappula ei voi omien s‰‰ntˆjens‰ mukaan liikkua ruutuun -> laiton siirto
			return false;
		}
	}
	
	
	/*
	 * Palauttaa true, jos l‰htˆ- ja kohderuudun v‰liss‰ ei ole nappuloita.
	 */
	boolean tarkistaSiirtolinja(int[] lahto, int[] kohde){
		
		//Siirtym‰alkio kertoo siirroksen suunnan.
		int[] siirtymaAlkio = new int[2];
		
		if(kohde[0] == lahto[0]){
			//Siirryt‰‰n vaakarivilt‰ toiselle
			siirtymaAlkio[1] = (kohde[1] - lahto[1] > 0) ? 1 : -1;
		}
		else if(kohde[1] == lahto[1]){
			//Siirryt‰‰n pystysarakkeelta toiselle
			siirtymaAlkio[0] = (kohde[0] - lahto[0] > 0) ? 1 : -1;

		}else{
			//Siirryt‰‰n vinottain
			siirtymaAlkio[0] = (kohde[0] - lahto[0] > 0) ? 1 : -1;
			siirtymaAlkio[1] = (kohde[1] - lahto[1] > 0) ? 1 : -1;
		}
		
		
		int[] tarkistettavaRuutu = new int[2];
		tarkistettavaRuutu[0] = lahto[0] + siirtymaAlkio[0];
		tarkistettavaRuutu[1] = lahto[1] + siirtymaAlkio[1];
		
		while(!(tarkistettavaRuutu[0] == kohde[0] && tarkistettavaRuutu[1] == kohde[1])){
			//toistetaan kunnes ollaan kohderuudussa
			
			//Tarkistetaan tarkistettava ruutu nappuloiden varalta.
			if(annaNappula(tarkistettavaRuutu) != null) {
				return false;
			}
			
			//P‰ivitet‰‰n tarkistettava ruutu
			tarkistettavaRuutu[0] += siirtymaAlkio[0];
			tarkistettavaRuutu[1] += siirtymaAlkio[1];
		}
		//Jos miss‰‰n siirtym‰linjalle osuvassa ruudussa ei ollut nappulaa, palautetaan true.
		return true;
		
	}
	

	
	
	
	
	//palauttaa true jos shakkimatti
	private boolean onkoShakkiMatti() {
		Nappula kuningas = null;
		
		//Etsit‰‰n kuningas joka saattaa olla uhattuna
		for(Nappula n : nappulat) {
			if(n instanceof Kuningas && n.vari != vuoro) {
				kuningas = n;
				break;
			}
		}
		
		Vari uhka = kuningas.vari == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN;
		int[] alkupSijainti = kuningas.annaSijainti();
		int[] mahdSijainti = Arrays.copyOf(alkupSijainti, 2);
		
		//Tarkistetaan onko kunigas uhattuna jos sit‰ ei siirret‰
		if(!uhattuRuutu(uhka, alkupSijainti)) {
			return false;
		}
		
		//Tarkistetaan onko kunigas uhattuna vaikka sit‰ siirrett‰isiin
		for(int r = -1; r < 2; r++) {
			for(int s = -1; r < 2; s++) {
				mahdSijainti[0] += r;
				mahdSijainti[1] += s;
				if(kuningas.voikoLiikkuaRuutuun(mahdSijainti)) { //pysyykˆ siirrett‰ess‰ laudalla
					if(annaNappula(mahdSijainti) != null || annaNappula(mahdSijainti).vari == kuningas.vari) { //onko kohteessa omia nappuloita
						if(!uhattuRuutu(uhka, mahdSijainti)) { //Onko kohde uhattu
							return false;
						}
					}
				}
			}
		}
		
		// TODO Voidaanko shakkimatti est‰‰ siirt‰m‰ll‰ jotain muuta nappulaa kuin kunigasta
		
		return true;
	}
	
	// Palauttaa true jos annetun v‰rinen nappula pystyy syˆm‰‰n nappulan annetusta ruudusta
	//@param uhkaaja : v‰ri joka voi syˆd‰ nappulan annetusta ruudusta
	//@param ruutu : ruutu jonka uhka tarkistetaan
	//@return : true jos ruutu on uhattu muuten false
	private boolean uhattuRuutu(Vari uhkaaja, int[] ruutu) {
		for(Nappula n : nappulat) {
			if(n.vari == uhkaaja) {
				if(n instanceof Sotilas) { // Sotilas syˆ eri tavalla kuin liikkuu
					if(uhkaaja == Vari.MUSTA) {
						if((ruutu[0] == n.annaSijainti()[0]+1 || ruutu[0] == n.annaSijainti()[0]-1) && ruutu[1] == n.annaSijainti()[1]-1) {
							return true;
						}
					}else if(uhkaaja == Vari.MUSTA) {
						if((ruutu[0] == n.annaSijainti()[0]+1 || ruutu[0] == n.annaSijainti()[0]-1) && ruutu[1] == n.annaSijainti()[1]+1) {
							return true;
						}
					}
				}
				else if(n.voikoLiikkuaRuutuun(ruutu)) {
					if(n instanceof Ratsu) { //Ratsu voi hyp‰t‰ nappuloiden yli
						return true;
					}
					else if(tarkistaSiirtolinja(n.annaSijainti(), ruutu)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	
	//@return : return[0] == l‰htˆkoordinaatti + return[1] == kohdekoordinaatti
	private int[][] kysySiirto() {
		boolean kelvollinen = false;
		int[][] siirto = new int[2][2];
		
		while(!kelvollinen) {
			
			String vari = vuoro == Vari.VALKOINEN ? "Valkoinen" : "Musta"; 
			System.out.print(vari + ", anna siirto: ");
		
			//A1 A2 l‰htˆruutu *v‰li* kohderuutu
			//TODO savegame tallentaa pelin
		
			while(!Peli.scanner.hasNextLine()) {
				//Odotetaan syˆtett‰
				try {
					//Tarkistetaan syˆtteen 100ms v‰lein
					Thread.sleep(100); //Ilman t‰t‰ odottaminen vie suoritinytimen l‰hes kaikki resurssit
				}
				catch(InterruptedException ie) {
					//P‰‰threadin keskeytt‰misen tapahtuessa ei ole paljon teht‰viss‰
					//mutta try-catch vaaditaan
				}
			
			}
		
			String syote = Peli.scanner.nextLine();
			
			
			syote = syote.toUpperCase();
			
			String tarkastus = "ABCDEFGH";
			if(!(syote.length() == 5 && tarkastus.contains(syote.charAt(0) + "") && tarkastus.contains(syote.charAt(3) + ""))){
				System.out.println("Virheellinen syˆte");
				continue;
			}
			
			try {
			siirto[0][0] = koordMuunnos.get(syote.charAt(0));
			siirto[0][1] = Integer.parseInt(syote.charAt(1) + "");
			siirto[1][0] = koordMuunnos.get(syote.charAt(3));
			siirto[1][1] = Integer.parseInt(syote.charAt(4) + "");
			kelvollinen = true;
			}catch(NumberFormatException e){
				System.out.println("Virheellinen syˆte");
				continue;
			}
		}
		return siirto;
	}

	public void piirraLauta() {
		
		//J‰rjestet‰‰n nappulat tulostusta varten
		Collections.sort(nappulat);
		
		int index = 0;
		
		//Sarakkeita vastaavat kirjaimet yl‰reunaan
		System.out.println("    a    b    c    d    e    f    g    h");
		System.out.println("  -----------------------------------------");
		
		//K‰yd‰‰n l‰pi ruudut rivi kerallaan ylh‰‰lt‰ alas ja tulostetaan nappulat paikoilleen
		for(int r = 8; r > 0; r--) {	
			String rivi = r + " |"; //Rivin numerot vasempaan reunaan
			for(int s = 1; s < 9; s++) {
				int[] sij = nappulat.get(index).annaSijainti();
				if(sij[0] == s && sij[1] == r) {
					rivi += " " + nappulat.get(index).toString();
					rivi += " | " + r; //Rivin numerot oikeaan reunaan
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
		//Sarakkeita vastaavat kirjaimet alareunaan
		System.out.println("    a    b    c    d    e    f    g    h");
	}
	
	//Tallentaa laudan tilan Save.txt tiedostoon
	public void tallennaLauta() throws FileNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Save.txt"));
		out.writeObject(this);
		out.close();
	}
	
	//Lataa tallennetun laudan tilan Save.txt tiedostosta
	//@param lauta Lauta-olio johon tallennettu tila ladataan
	public void lataaLauta(Lauta lauta) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("Save.txt"));
		lauta = (Lauta) in.readObject();
		in.close();
	}
	
	//Luo pelinappulat ja asettaa niiden sijainnit shakin alkutilanteen mukaisiksi
	private void alustaNappulat() {
		nappulat.add(new Torni(Vari.MUSTA, new int[] {1, 8}, this));
		nappulat.add(new Ratsu(Vari.MUSTA, new int[] {2, 8}, this));
		nappulat.add(new Lahetti(Vari.MUSTA, new int[] {3, 8}, this));
		nappulat.add(new Kuningatar(Vari.MUSTA, new int[] {4, 8}, this));
		nappulat.add(new Kuningas(Vari.MUSTA, new int[] {5, 8}, this));
		nappulat.add(new Lahetti(Vari.MUSTA, new int[] {6, 8}, this));
		nappulat.add(new Ratsu(Vari.MUSTA, new int[] {7, 8}, this));
		nappulat.add(new Torni(Vari.MUSTA, new int[] {8, 8}, this));
		for(int i = 1; i < 9; i++) {
			nappulat.add(new Sotilas(Vari.MUSTA, new int[] {i, 7}, this));
		}
		for(int i = 1; i < 9; i++) {
			nappulat.add(new Sotilas(Vari.VALKOINEN, new int[] {i, 2}, this));
		}
		nappulat.add(new Torni(Vari.VALKOINEN, new int[] {1, 1}, this));
		nappulat.add(new Ratsu(Vari.VALKOINEN, new int[] {2, 1}, this));
		nappulat.add(new Lahetti(Vari.VALKOINEN, new int[] {3, 1}, this));
		nappulat.add(new Kuningatar(Vari.VALKOINEN, new int[] {4, 1}, this));
		nappulat.add(new Kuningas(Vari.VALKOINEN, new int[] {5, 1}, this));
		nappulat.add(new Lahetti(Vari.VALKOINEN, new int[] {6, 1}, this));
		nappulat.add(new Ratsu(Vari.VALKOINEN, new int[] {7, 1}, this));
		nappulat.add(new Torni(Vari.VALKOINEN, new int[] {8, 1}, this));
	}

	//Alustaa HashMap olion jota k‰ytet‰‰n kirjainkoordinaattien muuntamiseen luvuiksi
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

