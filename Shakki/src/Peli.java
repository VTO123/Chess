import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Peli {

	public static final Scanner scanner = new Scanner(System.in); //Pelissä käytettävä scanneri syötteiden lukemiseen
	
	public static void main(String[] args) {
		
		Lauta lauta = new Lauta();
		
		lauta.piirraLauta();
		for(int i = 0; i < 10; i++) {

			lauta.pelaaVuoro();
		}
		
		scanner.close();
	}
	

	/**
	 * Tallentaa laudan tilan Save.txt tiedostoon
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void tallennaLauta(Lauta lauta) throws FileNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Save.txt"));
		out.writeObject(lauta);
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
	
	public static void lataaLauta(Lauta lauta) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("Save.txt"));
		lauta = (Lauta) in.readObject();
		in.close();
	}
	
}
