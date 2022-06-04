package ChessApp.GUI;

import ChessApp.GUI.TableGraphics.Table;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class AnimationController {

    public ImageView pieceImage;
    public int endCoordsX;
    public int endCoordsY;
    public double moveX;
    public double moveY;

    public AnimationController(){}

    public static int calculateEndCoordsX(final int startCoords, final int endCoords){
        int startCoordsCol = startCoords % 8;
        int endCoordsCol = endCoords % 8;
        return (endCoordsCol - startCoordsCol) * Table.TILE_DIMENSION;
    }

    public static int calculateEndCoordsY(final int startCoords, final int endCoords){
        int startCoordsRow = startCoords / 8;
        int endCoordsRow = endCoords / 8;
        return (endCoordsRow - startCoordsRow) * Table.TILE_DIMENSION;
    }
}
