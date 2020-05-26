package gmo.library.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;

@EnableFeignClients
public class Main extends Application {
    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        Application.launch(args);
    }

    /*@Override
    public void init() throws Exception {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        SpringApplication.run(Main.class, args);
        //applicationContext = new SpringApplicationBuilder().sources(App.class).run(args);
    }*/

    /*@Override
    public void stop() throws Exception {
        applicationContext.close();
        Platform.exit();
    }*/

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
