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
	public int[][] edellinenSiirto = new int[][] {{0,0},{0,0}};
	
	
	public Lauta() {
		nappulat = new ArrayList<>();
		vuoro = Vari.VALKOINEN;
		alustaNappulat();
		alustaKoordMuunnos();
		shakki = false;
	}
	
	public void pelaaVuoro() {
		boolean siirtoOnnistui = false;
		
		//Kysytään syöte ja yritetään toimia sen mukaan kunnes onnistutaan
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
		
		//Piirretään lauta ja tarkistetaan mahdollinen shakkimatti
		piirraLauta();
		if(onkoShakki()) {
			System.out.println("Shakki!");
			shakki = true;
		}
		
		//Vaihdetaan vuorossa olevaa väriä
		vuoro = vuoro == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN;
		
	}
	
	/**
	 * Palauttaa nappulan annetusta sijainnista jos sijainnissa ei ole nappulaa palauttaa null
	 * 
	 * @param koord koordinaatti josta nappulaa etsitään
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
	 * Palauttaa true ja siirtää lahto- koordinaatissa olevan nappulan kohde- koordinaattiin,
	 * jos annettu siirto on laillinen. Muulloin palauttaa false.
	 * Tämä metodi hoitaa nappuloiden syömisen tarvittaessa.
	 * 
	 * @param lahto Ruutu, jossa olevaa nappulaa liikutetaan
	 * @param kohde Ruutu, johon nappulaa liikutetaan
	 * @return tehtiinkö siirto (eli oliko se laillinen)
	 * 
	 */
	
	private boolean liikutaNappulaa(int[] lahto, int[] kohde) {
		//Tarkistetaan onko annetussa lähtöruudussa nappula
		Nappula siirrettava = annaNappula(lahto);
		
		//Tyhjästä ruudusta tai vastustajan nappulaa ei voi siirtää
		if(siirrettava == null || siirrettava.vari != vuoro){
			return false;
		}
		
		if(siirrettava.voikoLiikkuaRuutuun(kohde)){
			//Tämä suoritetaan, jos siirto on sallittu nappulan omien liikkumissääntöjen puitteissa
			//ja laudan nappulatilanne sallii siirron
			
			Nappula syotava = annaNappula(kohde);
			//Jos kohderuudussa on nappula, sen on pakko olla vastustaja, koska voikoLiikkuaRuutuun-
			//metodi palauttaa false jos yritetään syödä omaa.
			if(syotava != null) {
				poistaNappula(kohde);
			}
			siirrettava.asetaSijainti(kohde);
			
			//Ohestalyönnissä lyödään sotilas eri ruudusta kuin mihin siirrytään
			if(siirrettava instanceof Sotilas && ((Sotilas) siirrettava).enPassantSotilas != null) {
				
				nappulat.remove( ((Sotilas) siirrettava).enPassantSotilas );
				((Sotilas) siirrettava).enPassantSotilas = null;
			}
			
			//Jos siirto on linnoitus, niin siirretään vielä tornia.
			if(siirrettava instanceof Kuningas && ((Kuningas) siirrettava).linnoitus){
				
				Torni torni = null;
				
				int rivi = (siirrettava.vari == Vari.VALKOINEN ? 1 : 8); // Valkean linnoitus tapahtuu aina rivillä 1 ja mustan rivillä 8.
				
				if(kohde[0] == 3){ //pitkä linnoitus vasemmalle
					torni = (Torni) annaNappula(new int[] {1,rivi});
					if(torni.onkoLiikkunut) {
						return false;
					}
					torni.asetaSijainti(new int[] {4,rivi});
				}
				else if(kohde[0] == 7){ //lyhyt linnoitus oikealle
					torni = (Torni) annaNappula(new int[] {8,rivi});
					if(torni.onkoLiikkunut) {
						return false;
					}
					torni.asetaSijainti(new int[] {6,rivi});
				}
				((Kuningas) siirrettava).linnoitus = false;
				torni.onkoLiikkunut = true;
			}
			
			
			
			
			
			//Jos siirtäjää shakataan, niin vain kuninkaan pelastavat siirrot ovat laillisia.
			//Shakkaustilanne tulee purkaa siirtämällä, joten täytyy katsoa laudan tilanteen muututtua onko kuningas vielä shakissa.
			if(shakki) {
				//Etsitään shakattu kuningas
				Nappula uhattuKuningas = null;
				for(Nappula n : nappulat) {
					if(n instanceof Kuningas && n.vari == vuoro) {
						uhattuKuningas = n;
						break;
					}
				}
				//Shakkaajan väri
				Vari uhka = (vuoro == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN);
				
				//Jos siirron jälkeenkin siirtäjän kuningasta shakataan, siirto on laiton ja se täytyy peruuttaa.
				if(uhattuRuutu(uhka, uhattuKuningas.annaSijainti())) {
					//lisätään mahdollisesti syöty nappula takaisin laudalle
					if(syotava != null) {
						nappulat.add(syotava);
					}
					//ja siirretään siirretty nappula takaisin lähtöruutuunsa
					siirrettava.asetaSijainti(lahto);
					return false;
				}
				
				//Muuten siirto oli ok ja shakki torjuttiin
				shakki = false;
				
			}
			
			//merkitään kuningas tai torni liikkuneeksi jos sellaista siirrettiin
			if(siirrettava instanceof Kuningas) {
				((Kuningas) siirrettava).onkoLiikkunut = true;
			}
			else if(siirrettava instanceof Torni) {
				((Torni) siirrettava).onkoLiikkunut = true;
			}
			
			//kirjataan tehty siirto viimeisimmäksi siirroksi
			edellinenSiirto[0][0] = lahto[0];
			edellinenSiirto[0][1] = lahto[1];
			edellinenSiirto[1][0] = kohde[0];
			edellinenSiirto[1][1] = kohde[1];
			
			return true;
		}
		else{
			//Nappula ei voi omien sääntöjensä mukaan liikkua ruutuun -> laiton siirto
			return false;
		}
	}
	
	
	/**
	 * Palauttaa true, jos lähtö- ja kohderuudun välissä ei ole nappuloita.
	 * Ei tarkista kohderuutua.
	 * 
	 * @param lahto Lähtöruudun koordinaatit
	 * @param kohde Kohderuudun koordinaatit
	 * @return true, jos lähtö- ja kohderuudun välisellä linjalla ei ole nappuloita
	 */
	boolean tarkistaSiirtolinja(int[] lahto, int[] kohde){
		
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
			if(annaNappula(tarkistettavaRuutu) != null) {
				return false;
			}
			
			//Päivitetään tarkistettava ruutu
			tarkistettavaRuutu[0] += siirtymaAlkio[0];
			tarkistettavaRuutu[1] += siirtymaAlkio[1];
		}
		//Jos missään siirtymälinjalle osuvassa ruudussa ei ollut nappulaa, palautetaan true.
		return true;
		
	}
	
	/**
	 * Palauttaa true jos kuningasta uhataan
	 * 
	 * @return shakataanko kuningasta
	 */
	private boolean onkoShakki() {
		Nappula kuningas = null;
		
		//Etsitään kuningas joka saattaa olla uhattuna
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
		
		//Etsitään kuningas joka saattaa olla uhattuna
		for(Nappula n : nappulat) {
			if(n instanceof Kuningas && n.vari != vuoro) {
				kuningas = n;
				break;
			}
		}
		
		Vari uhka = kuningas.vari == Vari.VALKOINEN ? Vari.MUSTA : Vari.VALKOINEN;
		int[] alkupSijainti = kuningas.annaSijainti();
		int[] mahdSijainti = Arrays.copyOf(alkupSijainti, 2);
		
		//Tarkistetaan onko kunigas uhattuna jos sitä ei siirretä
		if(!uhattuRuutu(uhka, alkupSijainti)) {
			return false;
		}
		
		//Tarkistetaan onko kunigas uhattuna vaikka sitä siirrettäisiin
		//Eli tarkistetaan ovatko kuninkaan ympärillä olevat ruuduissa nappula tai uhka.
		for(int r = -1; r < 2; r++) {
			for(int s = -1; s < 2; s++) {
				mahdSijainti[0] = alkupSijainti[0] + s; // [0] on pystysarakkeen numero (kirjain)
				mahdSijainti[1] = alkupSijainti[1] + r; // [1] on vaakarivin numero
				
				if(mahdSijainti[0] < 1 || mahdSijainti[0] > 8 || mahdSijainti[1] < 1 || mahdSijainti[1] > 8) {
					continue; //Laudan ulkopuoliset ruudut eivät voi pelastaa kuningasta
				}
				
				
				if(!kuningas.voikoLiikkuaRuutuun(mahdSijainti)) {
					continue; //Tähän ruutuun ei voida siirtyä
				}
					
				if(!uhattuRuutu(uhka, mahdSijainti)) { //Onko kohde uhattu
					return false;
				}
				
				
			}
		}
		
		// TODO Voidaanko shakkimatti estää siirtämällä jotain muuta nappulaa kuin kuningasta
		
		//Voidaanko uhkaaja syödä:
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
	
	/** Palauttaa true jos annetun värinen nappula pystyy syömään nappulan annetusta ruudusta.
	 * 
	 * @param uhkaaja väri joka voi syödä nappulan annetusta ruudusta
	 * @param ruutu ruutu jonka uhka tarkistetaan
	 * @return true jos ruutu on uhattu muuten false
	 */
	public boolean uhattuRuutu(Vari uhkaaja, int[] ruutu) {
		
		
		boolean ruutuUhattu = false;
		
		//Jos tarkistettavassa ruudussa on nappula, se pitää poistaa tarkistuksen ajaksi.
		//Muutoin uhkaajan nappuloiden voikoSiirtyaRuutuun palauttaa false jos ruudussa on saman värinen nappula.
		Nappula syotava = annaNappula(ruutu);
		if(syotava != null) {
			nappulat.remove(syotava);
		}
		
		
		for(Nappula n : nappulat) {
			if(n.vari == uhkaaja) {
				if(n instanceof Sotilas) { // Sotilas syö eri tavalla kuin liikkuu
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
				//Kuninkaan voikoLiikkuaRuutuun-metodi hyödyntää tätä metodia, joten
				//sitä ei voi kutsua tai tulee ympyräviittaus ja stackoverflow.
				else if(n instanceof Kuningas) {
					//Kuningas uhkaa ruutua, jos se on ruudun välittömässä naapuriruudussa.
					//(kuninkaat eivät voi olla vierekkäisissä ruuduissa)
					int[] kuninkaanSij = n.annaSijainti();
					int xErotus = Math.abs(kuninkaanSij[0] - ruutu[0]);
					int yErotus = Math.abs(kuninkaanSij[1] - ruutu[1]);
					
					if((yErotus == 1 && xErotus == 1) || (yErotus == 0 && xErotus == 1) || (yErotus == 1 && xErotus == 0)) {
						ruutuUhattu = true;
					}
					
				}
				else if(n.voikoLiikkuaRuutuun(ruutu)) {
					if(n instanceof Ratsu) { //Ratsu voi hypätä nappuloiden yli
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
		
		//Jos testattavasta ruudusta poistettiin nappula, lisätään se takaisin laudalle.
		if(syotava != null) {
			nappulat.add(syotava);
		}
		
		return ruutuUhattu;
	}

	
	/**
	 * return[0][] = lähtökoordinaatti ja return[1][] = kohdekoordinaatti
	 * 
	 * @return siirron lähtö- ja kohderuutujen koordinaatit.
	 */
	private int[][] kysySiirto() {
		boolean kelvollinen = false;
		int[][] siirto = new int[2][2];
		
		while(!kelvollinen) {
			
			String vari = vuoro == Vari.VALKOINEN ? "Valkoinen" : "Musta"; 
			System.out.print(vari + ", anna siirto: ");
		
			//A1 A2 lähtöruutu *väli* kohderuutu
		
			while(!Peli.scanner.hasNextLine()) {
				//Odotetaan syötettä
				try {
					//Tarkistetaan syötteen 100ms välein
					Thread.sleep(100); //Ilman tätä odottaminen vie suoritinytimen lähes kaikki resurssit
				}
				catch(InterruptedException ie) {
					//Pääthreadin keskeyttämisen tapahtuessa ei ole paljon tehtävissä
					//mutta try-catch vaaditaan
				}
			
			}
		
			String syote = Peli.scanner.nextLine();
			
			
			syote = syote.toUpperCase();
			
			String tarkastus = "ABCDEFGH";
			
			if (syote.contains("TALLENNA")) {
				System.out.println("Tallennetaan peliä...");
				
				for(int i = 0; i < 3; i++) { //Tallennusta yritetään kolme kertaa
					try {
						tallennaPeli();
						System.out.println("Peli tallennettu");
						break;
					}catch(Exception e) {
						if(i < 2) {
							System.out.println("Tallennus epäonnistui. Yritetään uudelleen...");
						}else {
							System.out.println("Peliä ei voitu tallentaa");
						}
					}
				}
				continue;
			}
			else if (syote.contains("LATAA")) {
				System.out.println("Ladataan peliä...");
				for(int i = 0; i < 3; i++) {
					try {
						lataaPeli();
						System.out.println("Peli ladattu");
						break;
					}catch(FileNotFoundException e) {
						System.out.println("Tiedostoa ei löytynyt");
					}catch(IOException|ClassNotFoundException e) {
						if(i < 2) {
							System.out.println("Lataaminen epäonnistui. Yritetään uudelleen...");
						}else {
							System.out.println("Peliä ei pystytty lataaman");
						}
					}
				}
				piirraLauta();
				continue;
			}
			else if(!(syote.length() == 5 && tarkastus.contains(syote.charAt(0) + "") && tarkastus.contains(syote.charAt(3) + ""))){
				System.out.println("Virheellinen syöte");
				continue;
			}
			
			try {
			siirto[0][0] = koordMuunnos.get(syote.charAt(0));
			siirto[0][1] = Integer.parseInt(syote.charAt(1) + "");
			siirto[1][0] = koordMuunnos.get(syote.charAt(3));
			siirto[1][1] = Integer.parseInt(syote.charAt(4) + "");
			kelvollinen = true;
			}catch(NumberFormatException e){
				System.out.println("Virheellinen syöte");
				continue;
			}
		}
		return siirto;
	}
	
	
	/**
	 * Tulostaa laudan ja nappulat ascii-grafiikalla komentoriville.
	 */
	public void piirraLauta() {
		
		//Järjestetään nappulat tulostusta varten
		Collections.sort(nappulat);
		
		int index = 0;
		
		
		System.out.println();
		System.out.println("    a    b    c    d    e    f    g    h"); //Sarakkeita vastaavat kirjaimet yläreunaan
		System.out.println("  -----------------------------------------");
		
		//Käydään läpi ruudut rivi kerallaan ylhäältä alas ja tulostetaan nappulat paikoilleen
		for(int r = 8; r > 0; r--) {
			String rivi = r + " |"; //Rivin numerot vasempaan reunaan
			for(int s = 1; s < 9; s++) {
				int[] sij = nappulat.get(index).annaSijainti();
				if(sij[0] == s && sij[1] == r) {
					rivi += " " + nappulat.get(index).toString();
					rivi += " |";
					if(index < nappulat.size() - 1) {
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
	 * Alustaa HashMap olion jota käytetään kirjainkoordinaattien muuntamiseen luvuiksi
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

