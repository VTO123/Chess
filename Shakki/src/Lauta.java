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
	private boolean shakki;
	
	
	public Lauta() {
		nappulat = new ArrayList<>();
		vuoro = Vari.VALKOINEN;
		alustaNappulat();
		alustaKoordMuunnos();
		shakki = false;
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
		
		//Piirret‰‰n lauta ja tarkistetaan mahdollinen shakkimatti
		piirraLauta();
		if(onkoShakki()) {
			System.out.println("Shakki!");
			shakki = true;
		}
		
		//Vaihdetaan vuorossa olevaa v‰ri‰
		vuoro = vuoro == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN;
		
	}
	
	/**
	 * Palauttaa nappulan annetusta sijainnista jos sijainnissa ei ole nappulaa palauttaa null
	 * 
	 * @param koord koordinaatti josta nappulaa etsit‰‰n
	 * @return nappula joka on sijainnissa koord tai null jos sijainnissa ei ole nappulaa
	 */
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
	
	
	
	
	/**
	 * Palauttaa true ja siirt‰‰ lahto- koordinaatissa olevan nappulan kohde- koordinaattiin,
	 * jos annettu siirto on laillinen. Muulloin palauttaa false.
	 * T‰m‰ metodi hoitaa nappuloiden syˆmisen tarvittaessa.
	 * 
	 * @param lahto Ruutu, jossa olevaa nappulaa liikutetaan
	 * @param kohde Ruutu, johon nappulaa liikutetaan
	 * @return tehtiinkˆ siirto (eli oliko se laillinen)
	 * 
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
			
			if(siirrettava instanceof Kuningas && ((Kuningas) siirrettava).linnoitus){
				
				Torni torni = null;
				
				if(siirrettava.vari == Vari.VALKOINEN){
					if(kohde[0] == 3){ //liikutaan e1- ruudusta vasemmalle
						torni = (Torni) annaNappula(new int[] {1,1});
						torni.asetaSijainti(new int[] {4,1});
					}
					else if(kohde[0] == 7){ //e1-ruudusta oikealle
						torni = (Torni) annaNappula(new int[] {8,1});
						torni.asetaSijainti(new int[] {7,1});
					}
				}
				else if(siirrettava.vari == Vari.MUSTA){
					if(kohde[0] == 3){ //e8-ruudusta vasemmalle
						torni = (Torni) annaNappula(new int[] {1,8});
						torni.asetaSijainti(new int[] {4,8});
					}
					else if(kohde[0] == 7){
						torni = (Torni) annaNappula(new int[] {8,8});
						torni.asetaSijainti(new int[] {7,8});
					}
				}
				Kuningas kunkku = (Kuningas) siirrettava;
				kunkku.linnoitus = false;
				kunkku.onkoLiikkunut = true;
				torni.onkoLiikkunut = true;
			}
			
			
			
			
			
			//Jos siirt‰j‰‰ shakataan, niin vain kuninkaan pelastavat siirrot ovat laillisia.
			//Shakkaustilanne tulee purkaa siirt‰m‰ll‰, joten t‰ytyy katsoa laudan tilanteen muututtua.
			if(shakki) {
				//Etsit‰‰n shakattu kuningas
				Nappula uhattuKuningas = null;
				for(Nappula n : nappulat) {
					if(n instanceof Kuningas && n.vari == vuoro) {
						uhattuKuningas = n;
						break;
					}
				}
				//Shakkaajan v‰ri
				Vari uhka = (vuoro == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN);
				
				//Jos siirron j‰lkeenkin siirt‰j‰n kuningasta shakataan, siirto on laiton ja se t‰ytyy peruuttaa.
				if(uhattuRuutu(uhka, uhattuKuningas.annaSijainti())) {
					//lis‰t‰‰n mahdollisesti syˆty nappula takaisin laudalle
					if(syotava != null) {
						nappulat.add(syotava);
					}
					//ja siirret‰‰n siirretty nappula takaisin l‰htˆruutuunsa
					siirrettava.asetaSijainti(lahto);
					return false;
				}
				
				//Muuten siirto oli ok ja shakki torjuttiin
				shakki = false;
				
			}
			
			
			
			return true;
		}
		else{
			//Nappula ei voi omien s‰‰ntˆjens‰ mukaan liikkua ruutuun -> laiton siirto
			return false;
		}
	}
	
	
	/**
	 * Palauttaa true, jos l‰htˆ- ja kohderuudun v‰liss‰ ei ole nappuloita.
	 * Ei tarkista kohderuutua.
	 * 
	 * @param lahto L‰htˆruudun koordinaatit
	 * @param kohde Kohderuudun koordinaatit
	 * @return true, jos l‰htˆ- ja kohderuudun v‰lisell‰ linjalla ei ole nappuloita
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
	
	/**
	 * Palauttaa true jos kuningasta uhataan
	 * 
	 * @return shakataanko kuningasta
	 */
	private boolean onkoShakki() {
		Nappula kuningas = null;
		
		//Etsit‰‰n kuningas joka saattaa olla uhattuna
		for(Nappula n : nappulat) {
			if(n instanceof Kuningas && n.vari != vuoro) {
				kuningas = n;
				break;
			}
		}
		
		Vari uhka = kuningas.vari == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN;
		
		if(uhattuRuutu(uhka, kuningas.annaSijainti())) {
			return true;
		}
		
		return false;
		
		
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
		//Eli tarkistetaan ovatko kuninkaan ymp‰rill‰ olevat ruuduissa nappula tai uhka.
		for(int r = -1; r < 2; r++) {
			for(int s = -1; s < 2; s++) {
				mahdSijainti[0] = alkupSijainti[0] + s; // [0] on pystysarakkeen numero (kirjain)
				mahdSijainti[1] = alkupSijainti[1] + r; // [1] on vaakarivin numero
				
				if(mahdSijainti[0] < 1 || mahdSijainti[0] > 8 || mahdSijainti[1] < 1 || mahdSijainti[1] > 8) {
					continue; //Laudan ulkopuoliset ruudut eiv‰t voi pelastaa kuningasta
				}
				
				
				if(!kuningas.voikoLiikkuaRuutuun(mahdSijainti)) {
					continue; //T‰h‰n ruutuun ei voida siirty‰
				}
					
				if(!uhattuRuutu(uhka, mahdSijainti)) { //Onko kohde uhattu
					return false;
				}
				
				
			}
		}
		
		// TODO Voidaanko shakkimatti est‰‰ siirt‰m‰ll‰ jotain muuta nappulaa kuin kuningasta
		
		//Voidaanko uhkaaja syˆd‰:
		for(Nappula uhkaaja : nappulat) {
			if(uhkaaja.vari != uhka) {
				continue;
			}
			
			if(uhkaaja.voikoLiikkuaRuutuun(alkupSijainti)){
				// nyt uhkaaja shakkaa kuningasta
				if(uhattuRuutu(kuningas.vari, uhkaaja.annaSijainti())) {
					return false;
				}
			}
			
			
		}
		
		
		
		return true;
	}
	
	/** Palauttaa true jos annetun v‰rinen nappula pystyy syˆm‰‰n nappulan annetusta ruudusta.
	 * 
	 * @param uhkaaja v‰ri joka voi syˆd‰ nappulan annetusta ruudusta
	 * @param ruutu ruutu jonka uhka tarkistetaan
	 * @return true jos ruutu on uhattu muuten false
	 */
	public boolean uhattuRuutu(Vari uhkaaja, int[] ruutu) {
		
		
		boolean ruutuUhattu = false;
		
		//Jos tarkistettavassa ruudussa on nappula, se pit‰‰ poistaa tarkistuksen ajaksi.
		//Muutoin uhkaajan nappuloiden voikoSiirtyaRuutuun palauttaa false jos ruudussa on saman v‰rinen nappula.
		Nappula syotava = annaNappula(ruutu);
		if(syotava != null) {
			nappulat.remove(syotava);
		}
		
		
		for(Nappula n : nappulat) {
			if(n.vari == uhkaaja) {
				if(n instanceof Sotilas) { // Sotilas syˆ eri tavalla kuin liikkuu
					if(uhkaaja == Vari.MUSTA) {
						if((ruutu[0] == n.annaSijainti()[0]+1 || ruutu[0] == n.annaSijainti()[0]-1) && ruutu[1] == n.annaSijainti()[1]-1) {
							ruutuUhattu = true;
							break;
						}
					}else if(uhkaaja == Vari.MUSTA) {
						if((ruutu[0] == n.annaSijainti()[0]+1 || ruutu[0] == n.annaSijainti()[0]-1) && ruutu[1] == n.annaSijainti()[1]+1) {
							ruutuUhattu = true;
							break;
						}
					}
				}
				//Kuninkaan voikoLiikkuaRuutuun-metodi hyˆdynt‰‰ t‰t‰ metodia, joten
				//sit‰ ei voi kutsua tai tulee ympyr‰viittaus ja stackoverflow.
				else if(n instanceof Kuningas) {
					//Kuningas uhkaa ruutua, jos se on ruudun v‰littˆm‰ss‰ naapuriruudussa.
					//(kuninkaat eiv‰t voi olla vierekk‰isiss‰ ruuduissa)
					int[] kuninkaanSij = n.annaSijainti();
					int xErotus = Math.abs(kuninkaanSij[0] - ruutu[0]);
					int yErotus = Math.abs(kuninkaanSij[1] - ruutu[1]);
					
					if((yErotus == 1 && xErotus == 1) || (yErotus == 0 && xErotus == 1) || (yErotus == 1 && xErotus == 0)) {
						ruutuUhattu = true;
					}
					
				}
				else if(n.voikoLiikkuaRuutuun(ruutu)) {
					if(n instanceof Ratsu) { //Ratsu voi hyp‰t‰ nappuloiden yli
						ruutuUhattu = true;
						break;
					}
					else if(tarkistaSiirtolinja(n.annaSijainti(), ruutu)) {
						ruutuUhattu = true;
						break;
					}
				}
			}
		}
		
		//Jos testattavasta ruudusta poistettiin nappula, lis‰t‰‰n se takaisin laudalle.
		if(syotava != null) {
			nappulat.add(syotava);
		}
		
		return ruutuUhattu;
	}

	
	/**
	 * return[0][] = l‰htˆkoordinaatti ja return[1][] = kohdekoordinaatti
	 * 
	 * @return siirron l‰htˆ- ja kohderuutujen koordinaatit.
	 */
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
	
	
	/**
	 * Tulostaa laudan ja nappulat ascii-grafiikalla komentoriville.
	 */
	public void piirraLauta() {
		
		//J‰rjestet‰‰n nappulat tulostusta varten
		Collections.sort(nappulat);
		
		int index = 0;
		
		
		System.out.println();
		System.out.println("    a    b    c    d    e    f    g    h"); //Sarakkeita vastaavat kirjaimet yl‰reunaan
		System.out.println("  -----------------------------------------");
		
		//K‰yd‰‰n l‰pi ruudut rivi kerallaan ylh‰‰lt‰ alas ja tulostetaan nappulat paikoilleen
		for(int r = 8; r > 0; r--) {	
			String rivi = r + " |"; //Rivin numerot vasempaan reunaan
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
			System.out.println(rivi + " " + r); //Rivin numerot oikeaan reunaan
			System.out.println("  -----------------------------------------");
		}
		//Sarakkeita vastaavat kirjaimet alareunaan
		System.out.println("    a    b    c    d    e    f    g    h");
		System.out.println();
	}
	
	/**
	 * Tallentaa laudan tilan Save.txt tiedostoon
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void tallennaPeli() throws FileNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Save.txt"));
		out.writeObject(nappulat);
		out.writeObject(vuoro);
		out.flush();
		out.close();
	}
	
	/**
	 * Lataa tallennetun laudan tilan Save.txt tiedostosta
	 * 
	 * @param lauta Lauta-olio johon tallennettu tila ladataan
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 *
	 */
	
	@SuppressWarnings("unchecked")
	public void lataaPeli() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("Save.txt"));
		nappulat = (ArrayList<Nappula>) in.readObject();
		vuoro = (Vari) in.readObject();
		in.close();
	}
	
	/**
	 * Luo pelinappulat laudalle ja asettaa niiden sijainnit shakin alkutilanteen mukaisiksi
	 */
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

	/**
	 * Alustaa HashMap olion jota k‰ytet‰‰n kirjainkoordinaattien muuntamiseen luvuiksi
	 */
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

