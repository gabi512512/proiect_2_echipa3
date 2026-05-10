package ro.tuiasi.ac.Proiect_PIP;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerApp {
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

    // Metoda care comunică efectiv cu Ollama
    public static String trimiteCatreOllama(String comandaUtilizator) {
        try {
            // Construim memoria injectată și setările stricte (modul RAW)
            String promptRaw = "<|system|>\\nEști un sistem strict care returnează doar comenzi. Fără explicații.</s>\\n" +
                               "<|user|>\\nSTATUS</s>\\n<|assistant|>\\nSistem operațional.</s>\\n" +
                               "<|user|>\\nPORNESTE WI-FI</s>\\n<|assistant|>\\nwi-fi pornit.</s>\\n" +
                               "<|user|>\\nOPRESTE WI-FI</s>\\n<|assistant|>\\nwi-fi oprit.</s>\\n" +
                               "<|user|>\\n" + comandaUtilizator + "</s>\\n<|assistant|>\\n";

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
                promptRaw
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
            
            
           // System.out.println("DEBUG RAW OLLAMA: " + responseBody); // pus pentru debugging 
            // Extragem doar valoarea câmpului "response" din JSON-ul primit
            // (Metodă simplă pure-Java)
            String cautaCheia = "\"response\":\"";
            int indexStart = responseBody.indexOf(cautaCheia);
            
            if (indexStart != -1) {
                indexStart += cautaCheia.length();
                int indexEnd = responseBody.indexOf("\"", indexStart);
                String textFinal = responseBody.substring(indexStart, indexEnd);
                
                // Curățăm posibilele caractere de linie nouă adăugate de JSON
                return textFinal.replace("\\n", "").trim(); 
            }

            return "EROARE: Format de răspuns invalid de la AI.";

        } catch (Exception e) {
            System.out.println("Eroare de conexiune la Ollama: " + e.getMessage());
            return "EROARE: Conexiune eșuată.";
        }
    }
}