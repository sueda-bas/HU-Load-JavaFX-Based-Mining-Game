import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Main class represents the entry point of the application.
 */
public class Main extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;


    /**
     * The method called when the application starts.
     *
     * @param primaryStage The primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        //pane to create scene of the game
        GridPane root = new GridPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("HU-Load");

        BackgroundManager backgroundManager = new BackgroundManager();
        backgroundManager.createBackground(root,scene,primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();
        if(PlayGame.isGameOver){
            primaryStage.close();
        }


    }
    /**
     * The entry point of the application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}