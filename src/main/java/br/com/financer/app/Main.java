package br.com.financer.app;

import br.com.financer.database.InicializadorBanco;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        new InicializadorBanco().inicializar();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/br/com/financer/view/MainView.fxml"));
        Scene scene = new Scene(loader.load(), 980, 640);
        scene.getStylesheets().add(Main.class.getResource("/br/com/financer/view/styles.css").toExternalForm());

        stage.setTitle("financer");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(560);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
