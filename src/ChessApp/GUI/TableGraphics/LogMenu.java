package ChessApp.GUI.TableGraphics;

import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LogMenu extends HBox {
    private VBox p1;
    private VBox p2;

    BufferInfo bufferInfo;

    LogMenu() {
        initP1Log();
        initP2Log();
        setWidth(400);
        setPrefHeight(Table.BOARD_SIZE - 500);
        setStyle("-fx-background-color:#e3d4b6;");
        this.getChildren().add(new HBox(p1, new Separator(), p2));
        this.setAlignment(Pos.CENTER);
    }

    public void initBufferInfo(){
        this.bufferInfo = new BufferInfo();
    }

    public void initP1Log(){
        this.p1 = new VBox();
        p1.setPrefWidth(200);
        p1.getChildren().add(new Text("WhitePlayer"));
        p1.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;" +
                        "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: black;");
    }

    public void initP2Log(){
        this.p2 = new VBox();
        p2.setPrefWidth(200);
        p2.getChildren().add(new Text("BlackPlayer"));
        p2.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: black;");
    }


}
