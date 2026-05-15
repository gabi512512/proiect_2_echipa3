package ro.tuiasi.ac.Proiect_PIP;

import junit.framework.TestCase;
import java.io.*;
import java.net.*;

public class AppTest extends TestCase {

    private final int PORT_TEST = 5001;

    /**
     * Testează dacă clientul poate comunica cu un server real (Mock Server).
     * JUnit 3 rulează această metodă deoarece începe cu prefixul "test".
     */
    public void testComunicareClientServer() throws Exception {
        // 1. Pornim un server temporar pe un fir de execuție separat
        Thread serverThread = new Thread(new Runnable() {
            public void run() {
                try (ServerSocket serverSocket = new ServerSocket(PORT_TEST);
                     Socket client = serverSocket.accept();
                     PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                    
                    String primit = in.readLine();
                    if ("test_comanda".equals(primit)) {
                        out.println("confirmare_server");
                    }
                } catch (IOException e) {
                    // Eroare în serverul de test
                }
            }
        });
        serverThread.start();

        // Oferim un mic răgaz serverului să pornească (esențial în JUnit 3)
        Thread.sleep(200);

        // 2. Simulăm logica din clasa App
        try (Socket socket = new Socket("localhost", PORT_TEST);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            writer.println("test_comanda");
            String raspuns = reader.readLine();

            // 3. Verificări (Asserts)
            assertEquals("Răspunsul serverului este incorect!", "confirmare_server", raspuns);
            assertTrue("Socket-ul ar trebui să fie deschis", socket.isConnected());
            
        } catch (IOException e) {
            fail("Conexiunea a eșuat deși serverul trebuia să fie pornit: " + e.getMessage());
        }
    }

    /**
     * Testează comportamentul când adresa IP este incorectă.
     */
    public void testHostNecunoscut() {
        try {
            // Folosim un host care nu există
            new Socket("adresa.falsa.pip", PORT_TEST);
            fail("Trebuia să arunce UnknownHostException");
        } catch (UnknownHostException e) {
            // Succes - eroarea așteptată a fost prinsă
        } catch (IOException e) {
            // Poate arunca și IOException în anumite rețele, e acceptabil
        }
    }

    /**
     * Testează dacă socket-ul se închide corect.
     */
    public void testInchidereSocket() throws IOException {
        // Creăm un server minim pentru acceptare
        ServerSocket ss = new ServerSocket(PORT_TEST + 1);
        
        Thread t = new Thread(new Runnable() {
            public void run() {
                try { ss.accept().close(); } catch (Exception e) {}
            }
        });
        t.start();

        Socket s = new Socket("localhost", PORT_TEST + 1);
        s.close();
        
        assertTrue("Socket-ul trebuie să fie închis", s.isClosed());
        ss.close();
    }
}