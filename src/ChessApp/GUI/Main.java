package ChessApp.GUI;

import ChessApp.GUI.TableGraphics.Table;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public final double BOARD_SIZE = 1000;

    @Override
    public void start(Stage stage) throws Exception { //TODO: Change Check calculus
        Table table = new Table();
        //TODO: take the ImageView of the piece and move it

        Scene scene = new Scene(table);
        stage.setTitle("Chess fatto con il culo");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
