package ro.tuiasi.ac.Proiect_PIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

/**
 * Clasa GUIApp reprezintă interfața grafică JavaFX
 * pentru aplicația PIP.
 *
 * Interfața simulează un telefon mobil și permite
 * controlul funcțiilor:
 * - Wi-Fi
 * - Bluetooth
 * - Lanternă
 *
 * Comunicarea cu serverul se realizează prin socket TCP.
 *
 * Serverul trimite răspunsuri care actualizează
 * imaginile și stările afișate în interfață.
 *
 * @author Student
 * @version 1.0
 */



public class GUIApp extends Application {

	 /**
     * Socket utilizat pentru conexiunea cu serverul.
     */
	private Socket socket;
	/**
     * Flux pentru trimiterea comenzilor.
     */
    private PrintWriter out;
    /**
     * Flux pentru citirea mesajelor primite.
     */
    private BufferedReader in;
    /**
     * Variabilă care indică dacă aplicația este conectată.
     */
    private boolean esteConectat = false;
    /**
     * Metoda principală JavaFX.
     *
     * Inițializează:
     * - imaginile
     * - butoanele
     * - etichetele
     * - conexiunea la server
     *
     * @param primaryStage fereastra principală JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        
    	
    	
    	 /**
         * Metoda principală de lansare JavaFX.
         *
         * @param args argumentele aplicației
         */
        Image imagineBackground = new Image("file:.\\resurse\\new-iphone-pro-blue-titanium-smartphone-mockup-screen-front-back-view-editorial-vector-290816480.jpg");
        Image imagineWI_FI = new Image("file:.\\resurse\\wi-fi_off.png");
        Image imagineBluetooth = new Image("file:.\\resurse\\bluetooth_off.png");
        Image imagineLanterna = new Image("file:.\\resurse\\flashlight_off.png");
        Image imagineButon = new Image("file:C:.\\resurse\\send.png"); 
        
        ImageView vizualizatorImagine = new ImageView(imagineBackground);
        vizualizatorImagine.setFitWidth(500); 
        vizualizatorImagine.setPreserveRatio(true);
        
        
        ImageView iconWiFi = new ImageView(imagineWI_FI);
        iconWiFi.setFitWidth(50);
        iconWiFi.setPreserveRatio(true);
        
        ImageView iconBluetooth = new ImageView(imagineBluetooth); 
        iconBluetooth.setFitWidth(50);
        iconBluetooth.setPreserveRatio(true);
        
        ImageView iconLanterna = new ImageView(imagineLanterna); 
        iconLanterna.setFitWidth(50);
        iconLanterna.setPreserveRatio(true);
        
        ImageView iconButon = new ImageView(imagineButon);
        iconButon.setFitWidth(20); 
        iconButon.setPreserveRatio(true);
		
        
        Label labelWI_FI = new Label("off");
        labelWI_FI.setGraphic(iconWiFi);
        labelWI_FI.setGraphicTextGap(10); 
        labelWI_FI.setTextFill(Color.WHITE);
        labelWI_FI.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label labelBluetooth = new Label("off");
        labelBluetooth.setGraphic(iconBluetooth); 
        labelBluetooth.setGraphicTextGap(10); 
        labelBluetooth.setTextFill(Color.WHITE);
        labelBluetooth.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label labelLanterna = new Label("off");
        labelLanterna.setGraphic(iconLanterna); 
        labelLanterna.setGraphicTextGap(10);
        labelLanterna.setTextFill(Color.WHITE);
        labelLanterna.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        
        VBox statusContainer = new VBox(10); 
        statusContainer.setAlignment(Pos.CENTER_LEFT);
        statusContainer.getChildren().addAll(labelWI_FI ,labelLanterna, labelBluetooth);
        
        
        statusContainer.setTranslateX(350); 
        statusContainer.setTranslateY(-40);
        statusContainer.setPickOnBounds(false); 

    
        StackPane containerImagine = new StackPane();
       
        containerImagine.getChildren().addAll(vizualizatorImagine, statusContainer);

       
        TextField casetaText = new TextField();
        casetaText.setPromptText("Scrie comanda...");
        casetaText.setMaxWidth(300);

        Button butonAction = new Button("");
        butonAction.setGraphic(iconButon);
        butonAction.setStyle("-fx-background-radius: 5em; -fx-min-width: 40px; -fx-min-height: 40px;");
        butonAction.setOnAction(e -> {
            String mesaj = casetaText.getText();
            if (!mesaj.isEmpty() && esteConectat) {
                out.println(mesaj); 
                casetaText.clear();
                
                if (mesaj.equalsIgnoreCase("exit")) {
                    inchideResurse();
                }
            } else if (!esteConectat) {
                System.out.println("Serverul nu este conectat!");
            }
        });
        
        HBox randInput = new HBox(10); 
        randInput.setAlignment(Pos.CENTER); 
        randInput.getChildren().addAll(casetaText, butonAction);
        
        VBox layoutPrincipal = new VBox(20); 
        layoutPrincipal.setAlignment(Pos.CENTER); 
        
        layoutPrincipal.getChildren().addAll(containerImagine, randInput);
        Scene scena = new Scene(layoutPrincipal, 600, 750);
        primaryStage.setTitle("Interfață aplicatie");
        primaryStage.setScene(scena);
        conectareServer(labelWI_FI, iconWiFi, labelBluetooth, iconBluetooth, labelLanterna, iconLanterna);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            inchideResurse(); 
            System.exit(0);
        });
    }
    
    /**
     * Metoda principală de lansare JavaFX.
     *
     * @param args argumentele aplicației
     */


    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Actualizează interfața grafică în funcție
     * de răspunsul primit de la server.
     *
     * Exemple de răspunsuri:
     * - wi-fi_on
     * - wi-fi_off
     * - bluetooth_on
     * - flashlight_off
     *
     * @param raspuns răspunsul primit de la server
     * @param lWifi label pentru Wi-Fi
     * @param iWifi imagine Wi-Fi
     * @param lBt label Bluetooth
     * @param iBt imagine Bluetooth
     * @param lLant label Lanternă
     * @param iLant imagine Lanternă
     */
    private void actualizeazaInterfata(String raspuns, Label lWifi, ImageView iWifi, Label lBt, ImageView iBt, Label lLant, ImageView iLant) {
        try {
          
            if (!raspuns.contains("_")) return;

            String tip = raspuns.substring(0, raspuns.indexOf("_"));
            String caleImagine = "file:.\\resurse\\" + raspuns + "png";
            Image imagineNoua = new Image(caleImagine);

            switch (tip) {
                case "wi-fi":
                    actualizeazaStare(lWifi, iWifi, imagineNoua, raspuns);
                    break;
                case "bluetooth":
                    actualizeazaStare(lBt, iBt, imagineNoua, raspuns);
                    break;
                case "flashlight":
                    actualizeazaStare(lLant, iLant, imagineNoua, raspuns);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Eroare la încărcarea imaginii: " + e.getMessage());
        }
    }
   
    /**
     * Actualizează starea unei componente grafice.
     *
     * Schimbă:
     * - textul labelului
     * - imaginea
     * - culoarea textului
     *
     * Verde pentru ON.
     * Alb pentru OFF.
     *
     * @param label labelul care trebuie actualizat
     * @param view imaginea asociată
     * @param nouaImagine noua imagine
     * @param stare noua stare
     */
    
    private void actualizeazaStare(Label label, ImageView view, Image nouaImagine, String stare) {
    	
    	if (nouaImagine == null || nouaImagine.isError()) {
            System.out.println("Imaginea pentru " + stare + " nu a putut fi încărcată!");
            return;
        }
    	
        label.setText(stare);
        view.setImage(nouaImagine);
        view.setFitWidth(50);
        view.setPreserveRatio(true);
       
        if(stare.toLowerCase().contains("_on")) {
            label.setTextFill(Color.LIME);
        } else {
            label.setTextFill(Color.WHITE);
        }
    
    }
    
    /**
     * Creează conexiunea permanentă cu serverul.
     *
     * Rulează într-un thread separat și ascultă
     * continuu mesajele primite de la server.
     *
     * La primirea unui mesaj,
     * interfața este actualizată automat.
     *
     * @param lWifi label Wi-Fi
     * @param iWifi imagine Wi-Fi
     * @param lBt label Bluetooth
     * @param iBt imagine Bluetooth
     * @param lLant label Lanternă
     * @param iLant imagine Lanternă
     */
    
    private void conectareServer(Label lWifi, ImageView iWifi, Label lBt, ImageView iBt, Label lLant, ImageView iLant) {
        Thread listenerThread = new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                esteConectat = true;

                String raspunsServer;
                while (esteConectat && (raspunsServer = in.readLine()) != null) {
                    final String msg = raspunsServer;
                    
                    if (msg.equalsIgnoreCase("exit")) {
                        esteConectat = false;
                        break;
                    }

                    
                    javafx.application.Platform.runLater(() -> {
                        actualizeazaInterfata(msg, lWifi, iWifi, lBt, iBt, lLant, iLant);
                    });
                    
                }
            } catch (IOException e) {
                System.out.println("Eroare la conexiunea permanentă: " + e.getMessage());
            } finally {
                inchideResurse();
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    /**
     * Închide toate resursele utilizate:
     * - socket
     * - fluxuri de date
     *
     * Este apelată la:
     * - închiderea aplicației
     * - comanda exit
     */
    
    private void inchideResurse() {
        try {
            esteConectat = false;
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
}