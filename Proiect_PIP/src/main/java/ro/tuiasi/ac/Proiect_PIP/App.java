package ro.tuiasi.ac.Proiect_PIP;

import java.io.*;
import java.net.*;

/**
 * Clasa App reprezintă clientul consolei pentru aplicația PIP.
 *
 * Aceasta realizează conexiunea la server prin socket TCP
 * și permite utilizatorului să trimită comenzi de la tastatură.
 *
 * Serverul răspunde cu mesaje care sunt afișate în consolă.
 *
 * Exemple de comenzi:
 * - open wifi
 * - close wifi
 * - open bluetooth
 * - close bluetooth
 * - exit
 *
 * Comunicarea se realizează pe portul 5000.
 *
 * @author Alupei Victor,
 * @author Herghelegiu Cristian-Gabriel, 
 * @author Mocanu Marius-Gabriel, 
 * @author Partac Alexis-Matei
 * @version 2.0
 */

public class App {

	   /**
     * Metoda principală a aplicației client.
     *
     * Creează conexiunea către server, citește comenzile
     * de la tastatură și afișează răspunsurile primite.
     *
     * @param args argumentele din linia de comandă
     */
	public static void main(String[] args) {
		/**
         * Adresa IP a serverului.
         * localhost este utilizat pentru rulare locală.
         */
		
		String ipServer = "localhost"; 
		/**
         * Portul serverului.
         */
		int port = 5000;

		try (Socket socket = new Socket(ipServer, port)) {
			System.out.println("Te-ai conectat la server! Scrie un mesaj (sau 'exit' pentru a ieși):");

			/**
             * Flux pentru trimiterea mesajelor către server.
             */
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			/**
             * Flux pentru citirea mesajelor primite de la server.
             */
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			/**
             * Flux pentru citirea comenzilor de la tastatură.
             */
			BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
			String text;

			while (true) {
				// Citești de la tastatură
				text = consola.readLine(); 

				// Trimiți către server
				writer.println(text);

				// Primești și afișezi răspunsul de la server
				String raspuns = reader.readLine();
				System.out.println(raspuns);

				if (text.equalsIgnoreCase("exit")) {
					break;
				}
			}
		} catch (UnknownHostException ex) {
			System.out.println("Nu am găsit serverul. Verifică adresa IP!");
		} catch (IOException ex) {
			System.out.println("Eroare de conexiune (este pornit serverul?): " + ex.getMessage());
		}
	}
}
