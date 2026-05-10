package ro.tuiasi.ac.Proiect_PIP;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage; 

public class GUIApp extends Application {

    // Metoda 'start' este punctul de intrare pentru orice interfață grafică JavaFX
    @Override
    public void start(Stage primaryStage) {
        
        // 1. Creăm un element vizual (o simplă etichetă de text)
        Label mesaj = new Label("Salut! Aceasta este prima mea fereastră JavaFX.");

        // 2. Creăm un "Layout" (modul în care așezăm elementele). 
        // StackPane pune automat totul pe centru.
        StackPane root = new StackPane();
        root.getChildren().add(mesaj);

        // 3. Creăm Scena, punem layout-ul în ea și îi setăm lățimea (400) și înălțimea (300)
        Scene scena = new Scene(root, 400, 300);

        // 4. Configurăm "Stage-ul" (Fereastra de Windows în sine)
        primaryStage.setTitle("Prima mea aplicație"); // Titlul din bara de sus
        primaryStage.setScene(scena);                 // Atașăm scena la fereastră
        primaryStage.show();                          // Afișăm fereastra pe ecran
    }

    // Metoda main clasică a programelor Java
    public static void main(String[] args) {
        // 'launch' pornește motorul JavaFX și apelează automat metoda 'start' de mai sus
        launch(args);
    }
}
