import java.util.Scanner;

public class Peli {

	public static final Scanner scanner = new Scanner(System.in); //Peliss� k�ytett�v� scanneri sy�tteiden lukemiseen
	
	public static void main(String[] args) {
		
		Lauta lauta = new Lauta();
		
		lauta.piirraLauta();
		while(!lauta.annaShakkiMatti()) {
			lauta.pelaaVuoro();
		}
		
		
		scanner.close();
	}
	


	
}
