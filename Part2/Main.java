import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Wellington Regional Public Transport Network: Path Finding"); 

        NetworkViewer viewer = new NetworkViewer();
        Scene scene = viewer.setupGUI();

        // Put the GUI into a scene on the stage:
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> {System.exit(0);});
    
        viewer.initialise();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
