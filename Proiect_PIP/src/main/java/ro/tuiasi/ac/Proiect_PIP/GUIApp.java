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



public class GUIApp extends Application {

	private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean esteConectat = false;
    @Override
    public void start(Stage primaryStage) {
        
    	
    	
        //  Pregătim imaginile
        Image imagineBackground = new Image("file:.\\resurse\\new-iphone-pro-blue-titanium-smartphone-mockup-screen-front-back-view-editorial-vector-290816480.jpg");
        Image imagineWI_FI = new Image("file:.\\resurse\\wi-fi_off.png");
        Image imagineBluetooth = new Image("file:.\\resurse\\bluetooth_off.png");
        Image imagineLanterna = new Image("file:.\\resurse\\flashlight_off.png");
        Image imagineButon = new Image("file:C:.\\resurse\\send.png"); 
        
        ImageView vizualizatorImagine = new ImageView(imagineBackground);
        vizualizatorImagine.setFitWidth(500); 
        vizualizatorImagine.setPreserveRatio(true);
        
        // Configurăm vizualizatoarele pentru iconițe
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
        iconButon.setFitWidth(20); // O facem mică să încapă în buton
        iconButon.setPreserveRatio(true);
		
        //  Creăm Label-urile și setăm imaginile lângă text
        Label labelWI_FI = new Label("off");
        labelWI_FI.setGraphic(iconWiFi); // Pune iconița în stânga textului
        labelWI_FI.setGraphicTextGap(10); // Distanța dintre iconiță și text
        labelWI_FI.setTextFill(Color.WHITE);
        labelWI_FI.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label labelBluetooth = new Label("off");
        labelBluetooth.setGraphic(iconBluetooth); // Pune iconița în stânga textului
        labelBluetooth.setGraphicTextGap(10); 
        labelBluetooth.setTextFill(Color.WHITE);
        labelBluetooth.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label labelLanterna = new Label("off");
        labelLanterna.setGraphic(iconLanterna); // Pune iconița în stânga textului
        labelLanterna.setGraphicTextGap(10);
        labelLanterna.setTextFill(Color.WHITE);
        labelLanterna.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        //  Grupăm label-urile într-un container mic pentru a le muta împreună pe ecranul telefonului
        VBox statusContainer = new VBox(10); // 10 pixeli între rânduri
        statusContainer.setAlignment(Pos.CENTER_LEFT);
        statusContainer.getChildren().addAll(labelWI_FI ,labelLanterna, labelBluetooth);
        
        // Poziționăm întreg grupul pe ecranul mock-up-ului
        statusContainer.setTranslateX(350); 
        statusContainer.setTranslateY(-40);
        statusContainer.setPickOnBounds(false); // Permite click-uri prin container dacă e cazul

        //  StackPane-ul pentru suprapunere
        StackPane containerImagine = new StackPane();
        // Adăugăm fundalul și apoi containerul cu label-uri
        containerImagine.getChildren().addAll(vizualizatorImagine, statusContainer);

        //  Partea de jos (Input-ul)
        TextField casetaText = new TextField();
        casetaText.setPromptText("Scrie comanda...");
        casetaText.setMaxWidth(300);

        Button butonAction = new Button("");
        butonAction.setGraphic(iconButon);
        butonAction.setStyle("-fx-background-radius: 5em; -fx-min-width: 40px; -fx-min-height: 40px;");
        butonAction.setOnAction(e -> {
            String mesaj = casetaText.getText();
            if (!mesaj.isEmpty() && esteConectat) {
                out.println(mesaj); // Trimitem doar mesajul
                casetaText.clear();
                
                if (mesaj.equalsIgnoreCase("exit")) {
                    inchideResurse();
                }
            } else if (!esteConectat) {
                System.out.println("Serverul nu este conectat!");
            }
        });
        
        HBox randInput = new HBox(10); // 10 pixeli distanță între ele
        randInput.setAlignment(Pos.CENTER); // Le centrăm pe orizontală
        randInput.getChildren().addAll(casetaText, butonAction);
        
        VBox layoutPrincipal = new VBox(20); 
        layoutPrincipal.setAlignment(Pos.CENTER); 
        // Înlocuim casetaText și butonAction cu randInput
        layoutPrincipal.getChildren().addAll(containerImagine, randInput);
        Scene scena = new Scene(layoutPrincipal, 600, 750);
        primaryStage.setTitle("Interfață aplicatie");
        primaryStage.setScene(scena);
        conectareServer(labelWI_FI, iconWiFi, labelBluetooth, iconBluetooth, labelLanterna, iconLanterna);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            inchideResurse(); // Metoda care închide socket-ul, out și in
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    private void actualizeazaInterfata(String raspuns, Label lWifi, ImageView iWifi, Label lBt, ImageView iBt, Label lLant, ImageView iLant) {
        try {
            // Verificăm dacă răspunsul conține caracterul de separare
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
    private void actualizeazaStare(Label label, ImageView view, Image nouaImagine, String stare) {
    	
    	if (nouaImagine == null || nouaImagine.isError()) {
            System.out.println("Imaginea pentru " + stare + " nu a putut fi încărcată!");
            return;
        }
    	
        label.setText(stare);
        view.setImage(nouaImagine);
        view.setFitWidth(50);
        view.setPreserveRatio(true);
        // Verificăm dacă textul conține "on" (ex: wi-fi_on)
        if(stare.toLowerCase().contains("_on")) {
            label.setTextFill(Color.LIME);
        } else {
            label.setTextFill(Color.WHITE);
        }
    
    }
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

                    // Actualizăm UI-ul ori de câte ori serverul trimite ceva
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

    private void inchideResurse() {
        try {
            esteConectat = false;
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
}