package gmo.library.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import java.io.File;

@EnableFeignClients
public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @SneakyThrows
    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(new File("main.fxml").toURI().toURL());
        Scene scene = new Scene(fxmlLoader.load());
        ((Controller)fxmlLoader.getController()).init();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Library App");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
