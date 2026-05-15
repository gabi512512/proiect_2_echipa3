package ro.tuiasi.ac.Proiect_PIP;

import java.io.*;
import java.net.*;

public class App {

	public static void main(String[] args) {
		// IMPORTANT: Aici pui adresa IP a laptopului 1 (Serverul)
		String ipServer = "localhost"; //Exemplu:192.168.49.213-camin///192.168.49.28-facultate 
		int port = 5000;

		try (Socket socket = new Socket(ipServer, port)) {
			System.out.println("Te-ai conectat la server! Scrie un mesaj (sau 'exit' pentru a ieși):");

			// Fluxuri pentru comunicarea cu serverul
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Flux pentru a citi ce scrii tu la tastatură
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
