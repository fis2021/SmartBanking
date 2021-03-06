package smart.banking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smart.banking.services.BankRepresentativeService;
import smart.banking.services.ClientService;
import smart.banking.services.FileSystemService;
import smart.banking.services.UserService;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends Application {

    public static Stage stg;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stg=primaryStage;
        UserService.initDatabase();
        ClientService.initDatabase();
        BankRepresentativeService.initDatabase();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Smart-Banking");
        primaryStage.setScene(new Scene(root, 600, 525));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
