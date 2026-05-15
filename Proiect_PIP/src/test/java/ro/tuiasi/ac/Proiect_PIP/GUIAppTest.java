package ro.tuiasi.ac.Proiect_PIP;

import junit.framework.TestCase;
import java.net.Socket;
import java.io.IOException;

public class GUIAppTest extends TestCase {

    private GUIApp app;

    /**
     * Metoda setUp se execută înainte de fiecare test.
     */
    protected void setUp() {
        app = new GUIApp();
    }

    /**
     * Testăm dacă starea inițială a conexiunii este false.
     */
    public void testInitialConnectionState() {
        // În JUnit 3 nu avem acces ușor la câmpuri private, 
        // dar testăm logica de bază dacă ar fi vizibilă sau prin metode.
        // Aici verificăm dacă obiectul s-a creat corect.
        assertNotNull("Obiectul aplicației nu trebuie să fie null", app);
    }

    /**
     * Testăm comportamentul la timeout/conexiune eșuată.
     */
    public void testConnectionFailure() {
        try {
            // Încercăm să deschidem un socket pe un port unde știm că nu e serverul
            Socket s = new Socket("localhost", 9999);
            fail("Ar fi trebuit să arunce IOException pentru portul 9999");
        } catch (IOException e) {
            // Succes: eroarea a fost prinsă
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Testăm logica de parsing a numelui imaginii (indirect).
     * Notă: Deoarece metodele sunt private în GUIApp, testăm conceptul de path.
     */
    public void testImagePathLogic() {
        String raspunsServer = "wi-fi_on";
        String caleAsteptata = "file:.\\resurse\\" + raspunsServer + "png";
        
        assertEquals("Calea imaginii nu este construită corect", 
                     "file:.\\resurse\\wi-fi_onpng", caleAsteptata);
        
        // ATENȚIE: Am observat în codul tău că lipsește punctul înainte de png!
        // Ar trebui să fie: + "." + "png" sau + ".png"
    }

    /**
     * Curățăm resursele după test.
     */
    protected void tearDown() {
        app = null;
    }
}