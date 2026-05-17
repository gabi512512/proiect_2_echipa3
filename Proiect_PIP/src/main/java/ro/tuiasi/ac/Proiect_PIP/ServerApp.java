package ro.tuiasi.ac.Proiect_PIP;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clasa ServerApp reprezintă serverul principal
 * al aplicației PIP.
 *
 * Serverul:
 * - acceptă conexiuni TCP
 * - primește comenzi de la client
 * - trimite comenzile către modelul AI Ollama
 * - returnează răspunsurile către client
 *
 * Comunicarea se realizează pe portul 5000.
 *
 * Modelul utilizat:
 * - TinyLlama prin Ollama API
 *
 * @author Student
 * @version 1.0
 */

public class ServerApp {
	/**
     * Metoda principală a serverului.
     *
     * Creează serverul TCP și așteaptă conectarea
     * unui client.
     *
     * Pentru fiecare comandă primită:
     * - trimite mesajul către Ollama
     * - primește răspunsul AI
     * - transmite răspunsul clientului
     *
     * @param args argumentele din linia de comandă
     */
	
    public static void main(String[] args) { 
        int port = 5000; // Portul pe care serverul va asculta

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serverul a pornit. Aștept conexiuni pe portul " + port + "...");

            // Programul se blochează aici și așteaptă conectarea unui client
            Socket socket = serverSocket.accept();
            System.out.println("Un client s-a conectat!");  

            // Fluxuri pentru citirea și trimiterea datelor
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String mesajDeLaClient;
            
            // Citim mesajele primite de la client în buclă
            while ((mesajDeLaClient = reader.readLine()) != null) {
                System.out.println("Clientul a trimis comanda: " + mesajDeLaClient);
                
                // Dacă clientul scrie 'exit', închidem conexiunea
                if(mesajDeLaClient.equalsIgnoreCase("exit")) {
                    writer.println("Conexiune închisă. La revedere!");
                    break;
                }

                // 1. Apelăm modelul TinyLlama cu mesajul primit
                String raspunsAI = trimiteCatreOllama(mesajDeLaClient);
                
                // 2. Trimitem răspunsul generat de AI înapoi către client
                writer.println(raspunsAI);
            }
            
            System.out.println("Conexiune încheiată.");
            socket.close();

        } catch (IOException ex) {
            System.out.println("Eroare la pornirea serverului: " + ex.getMessage());
        }   
    }
    
    /**
     * Trimite comanda utilizatorului către modelul AI Ollama.
     *
     * Metoda construiește un prompt RAW strict
     * care permite doar comenzile:
     * - status
     * - open wifi
     * - close wifi
     * - open bluetooth
     * - close bluetooth
     *
     * Orice altă comandă generează:
     * "error: Order not accepted."
     *
     * Comunicarea cu Ollama se face prin HTTP POST.
     *
     * @param comandaUtilizator comanda trimisă de client
     * @return răspunsul procesat de modelul AI
     */

    // Metoda care comunică efectiv cu Ollama
    public static String trimiteCatreOllama(String comandaUtilizator) {
        try {
         
        	String promptRaw = "<|system|>\n" +
                    " You are a strict sistem which returns only predefine orders. " +
                    "If the user gives ANY IMPUT that dont contain the folowing 'status', 'wifi' or 'bluetooth'," +
                    "responde EXCLUSIVELY with: 'error: Order not accepted.' Without explications and follow strictly those rule.</s>\n" +
                    "<|user|>\nstatus</s>\n<|assistant|>\nthe sistem is operating.</s>\n" +
                    "<|user|>\nopen wifi</s>\n<|assistant|>\nwi-fi_on.</s>\n" +
                    "<|user|>\nclose wifi</s>\n<|assistant|>\nwi-fi_off.</s>\n" +
                    "<|user|>\nopen bluetooth</s>\n<|assistant|>\nbluetooth_on.</s>\n" +
                    "<|user|>\nclose bluetooth</s>\n<|assistant|>\nbluetooth_off.</s>\n" +
                    "<|user|>\scmcjzx djashsdf</s>\n<|assistant|>\nerror: Order not accepted.</s>\n" +
                    "<|user|>\scmcjzx</s>\n<|assistant|>\nerror: Order not accepted.</s>\n" +
                    "<|user|>\nopen fsadgdsfgz</s>\n<|assistant|>\nerror: Order not accepted.</s>\n" +
                    "<|user|>\nclose dzvzscxbv</s>\n<|assistant|>\nerror: Order not accepted.</s>\n" +
                    "<|user|>\n" + comandaUtilizator.trim().toLowerCase() + "</s>\n<|assistant|>\n";
        	
        	String promptEscaped = promptRaw
        	        .replace("\\", "\\\\")   // Escapăm backslash-ul
        	        .replace("\"", "\\\"")   // Escapăm ghilimelele
        	        .replace("\n", "\\n")    // Transformăm enter-ul fizic în textul "\n"
        	        .replace("\r", "\\r");   // Pentru compatibilitate Windows
        	
            String jsonPayload = String.format(
                "{" +
                "\"model\": \"tinyllama\"," +
                "\"raw\": true," +
                "\"prompt\": \"%s\"," +
                "\"stream\": false," +
                "\"options\": {" +
                    "\"temperature\": 0.0," +
                    "\"num_predict\": 15," +
                    "\"stop\": [\"</s>\", \"\\n\", \"<|user|>\"]" +
                "}" +
                "}", 
                promptEscaped
            );

            // Trimitem request-ul HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            
            
           /**
            * Extragem doar valoarea câmpului "response" din JSON-ul primit
            * (Metodă simplă pure-Java)
            * după ce primești responseBody 
            */
            
            String cautaCheia = "\"response\":\"";
            int indexStart = responseBody.indexOf(cautaCheia);

            if (indexStart != -1) {
                indexStart += cautaCheia.length();
                int indexEnd = responseBody.indexOf("\"", indexStart);
                
                
                System.out.println("RAspunsul modelului "+responseBody);
                // Extragem textul brut
                String textFinal = responseBody.substring(indexStart, indexEnd);
                
                // Curățăm TOATE secvențele de tip escape pe care Ollama le trimite înapoi
                textFinal = textFinal.replace("\\n", "")
                                     .replace("\\r", "")
                                     .replace("\\\"", "\"")
                                     .trim();
                
                // DEBUG: Vezi exact ce a rămas după curățare
                System.out.println("Text procesat: [" + textFinal + "]");
                
                return textFinal;
            
            }

            return "EROARE: Format de răspuns invalid de la AI.";

        } catch (Exception e) {
            System.out.println("Eroare de conexiune la Ollama: " + e.getMessage());
            return "EROARE: Conexiune eșuată.";
        }
    }
}