package ro.tuiasi.ac.Proiect_PIP;

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

    @Override
    public void start(Stage primaryStage) {
        
        // 1. Pregătim imaginile
        Image imagineBackground = new Image("file:.\\resurse\\new-iphone-pro-blue-titanium-smartphone-mockup-screen-front-back-view-editorial-vector-290816480.jpg");
        Image imagineWI_FI = new Image("file:.\\resurse\\power.png");
        Image imagineBluetooth = new Image("file:.\\resurse\\arrows.png");
        Image imagineLanterna = new Image("file:.\\resurse\\torch_off.png");
        Image imagineButon = new Image("file:.\\resurse\\send.png"); // Înlocuiește cu numele fișierului tău
        
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
		
        // 2. Creăm Label-urile și setăm imaginile lângă text
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
        
        // 3. Grupăm label-urile într-un container mic pentru a le muta împreună pe ecranul telefonului
        VBox statusContainer = new VBox(10); // 10 pixeli între rânduri
        statusContainer.setAlignment(Pos.CENTER_LEFT);
        statusContainer.getChildren().addAll(labelWI_FI ,labelLanterna, labelBluetooth);
        
        // Poziționăm întreg grupul pe ecranul mock-up-ului
        statusContainer.setTranslateX(350); 
        statusContainer.setTranslateY(-40);
        statusContainer.setPickOnBounds(false); // Permite click-uri prin container dacă e cazul

        // 4. StackPane-ul pentru suprapunere
        StackPane containerImagine = new StackPane();
        // Adăugăm fundalul și apoi containerul cu label-uri
        containerImagine.getChildren().addAll(vizualizatorImagine, statusContainer);

        // 5. Partea de jos (Input-ul)
        TextField casetaText = new TextField();
        casetaText.setPromptText("Scrie comanda...");
        casetaText.setMaxWidth(300);

        Button butonAction = new Button("");
        butonAction.setGraphic(iconButon);
        butonAction.setOnAction(e -> {
            labelWI_FI.setText(casetaText.getText());
            casetaText.clear();
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
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}