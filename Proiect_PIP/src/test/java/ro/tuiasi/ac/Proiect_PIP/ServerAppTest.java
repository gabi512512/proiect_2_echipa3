package ro.tuiasi.ac.Proiect_PIP;

import java.io.IOException;
import java.net.Socket;

import junit.framework.TestCase;

public class ServerAppTest extends TestCase {

    /**
     * În JUnit 3, orice metodă care începe cu "test" 
     * va fi rulată automat.
     */
    public void testConexiuneServerEsuata() {
        try {
            // Încercăm să ne conectăm la un port care nu există
            new Socket("localhost", 9999);
            
            // Dacă ajunge aici, înseamnă că nu a aruncat eroare (ceea ce e rău)
            fail("Ar trebui să arunce IOException dacă serverul este oprit.");
        } catch (IOException e) {
            // Dacă prindem eroarea, testul este considerat REUȘIT
            assertNotNull(e.getMessage());
        }
    }

    public void testVerificareAdresaInvalida() {
        try {
            new Socket("adresa.inexistenta.com", 5000);
            fail("Ar fi trebuit să arunce UnknownHostException.");
        } catch (IOException e) {
            // Succes, eroarea a fost interceptată
        }
    }

    /**
     * JUnit 3 nu are assertThrows. 
     * Verificarea se face manual cu try-catch.
     */
    public void testPortInvalid() {
        try {
            new Socket("localhost", -1);
            fail("Portul negativ ar fi trebuit să genereze o eroare.");
        } catch (IllegalArgumentException e) {
            // Succes
        } catch (IOException e) {
            // Succes
        }
    }
}