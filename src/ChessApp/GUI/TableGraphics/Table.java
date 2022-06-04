package ChessApp.GUI.TableGraphics;

import ChessApp.Engine.Board.Board;
import ChessApp.Engine.Board.Board.Builder;
import ChessApp.Engine.Board.BoardUtils;
import ChessApp.Engine.Board.Move;
import ChessApp.Engine.Board.Tile;
import ChessApp.Engine.Pieces.Piece;
import ChessApp.Engine.Player.MoveTransition;
import ChessApp.GUI.AnimationController;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Table extends BorderPane {
    public final static int BOARD_SIZE = 1200;
    public final static int TILE_DIMENSION = 150;
    private final TopMenu topMenu;
    public static BoardGui boardGui;
    public static Board chessBoard;
    public TextArea buffer = new TextArea();
    public static boolean normalBoardDirection = true;

    private Tile sourceTile = null;
    private Tile destTile = null;
    private Piece movedPiece = null;
    public StackPane boardPane;
    public AnimationController moveTransition;

    public Table() {
        this.topMenu = new TopMenu();
        boardGui = new BoardGui();
        boardPane = new StackPane(boardGui);
        this.moveTransition = new AnimationController();
        buffer.setPrefHeight(500);
        buffer.setPrefWidth(500);
        buffer.setStyle("-fx-border-width: 5; -fx-border-color: black; -fx-control-inner-background: #FFFFFF;" +
                "-fx-background-color: black; -fx-font-size: 20; -fx-spacing: 0");
        VBox rightPane = new VBox(new LogMenu(), buffer);

        this.setTop(this.topMenu);
        this.setCenter(boardGui);
        this.setRight(rightPane);
        chessBoard = Board.createStandardBoard();
    }

    public static class TopMenu extends MenuBar {
        Menu newGameMenu;
        MenuItem pgnMenuItem;
        MenuItem pvpMenuItem;
        MenuItem exitMenuItem;
        public TopMenu(){
            this.newGameMenu = new Menu("File");
            initExitMenuItem();
            initNewGameMenu();
            this.getMenus().addAll(newGameMenu);
            this.setStyle("-fx-background-color: #50544b;");
        }

        private void initExitMenuItem() {
            this.exitMenuItem = new MenuItem("Exit");
            this.exitMenuItem.setOnAction(e -> {
                Platform.exit();
                System.exit(0);
            });
        }

        public void initNewGameMenu(){
            initPgnButton();
            initPvpButton();
            this.newGameMenu.getItems().addAll(this.pgnMenuItem, this.pvpMenuItem, this.exitMenuItem);
        }

        public void initPgnButton(){
            this.pgnMenuItem = new MenuItem("From PGN");
        }

        public void initPvpButton(){
            this.pvpMenuItem = new MenuItem("PvP");
        }

    }

    public class BoardGui extends GridPane {
        private final String white = "#eeeed2";
        private final String black = "#769656";
        private final String[] COLORS = {black, white};
        final List<TileGui> boardTiles;

        public Board board;
        public Builder builder;

        public BoardGui(){
            this.boardTiles = new ArrayList<>();
            this.board = Board.createStandardBoard();
            this.builder = new Builder();
            generateBoard();
        }

        public void drawTransitionBoard(final Board boardToDraw, final int startTileCoords, final int endTileCoords) {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.millis(200));
            transition.setToX(AnimationController.calculateEndCoordsX(startTileCoords, endTileCoords));
            transition.setToY(AnimationController.calculateEndCoordsY(startTileCoords, endTileCoords));
            transition.setNode(boardTiles.get(startTileCoords).pieceImage);
            transition.play();
            transition.setOnFinished(e -> drawTiles(boardToDraw, startTileCoords, endTileCoords));
        }

        public void drawTiles(final Board boardToDraw, final int startTileCoords, final int endTileCoords){
            int i = 0, j = 0;
            for(final TileGui tileGui: boardTiles){
                if(tileGui.getTileId() == startTileCoords || tileGui.getTileId() == endTileCoords){
                    getChildren().remove(tileGui);
                    tileGui.drawTile(boardToDraw);
                    this.add(tileGui, j, i);
                }
                if(j == 7){
                    i++;
                    j = -1;
                }
                j++;
            }
        }

        public void generateBoard(){
            TileGui tile;
            for(int i = 0; i < BoardUtils.NUM_TILES_X_ROW; i++){
                for(int j = 0; j < BoardUtils.NUM_TILES_X_ROW; j++){
                    tile = new TileGui(this, i, j);
                    this.boardTiles.add(tile);
                    this.add(tile, j, i);
                }
            }
        }

        public static void main(String[] args) {
        }
    }

    public class TileGui extends StackPane {
        public final String coordinates;
        public final int tileId;
        public ImageView pieceImage = null;

        public TileGui(final BoardGui boardGui, final int row, final int column){
            setPrefSize(Table.TILE_DIMENSION, Table.TILE_DIMENSION);
            this.coordinates = calculateCoordinates(row, column);
            this.tileId = calculateTileId(row, column);
            assignTileColor();
            assignTilePieceIcon(boardGui.board);
            setMouseEvents();
        }

        private void setMouseEvents(){
            setOnMouseClicked(e -> {
                if(e.getButton() == MouseButton.SECONDARY) {
                    sourceTile = null;
                    destTile = null;
                    movedPiece = null;
                }else if(e.getButton() == MouseButton.PRIMARY){
                    buffer.clear();
                    if(sourceTile == null){
                        sourceTile = chessBoard.getTile(this.tileId);
                        movedPiece = sourceTile.getPiece();
                        if(movedPiece == null) sourceTile = null;
                    } else {
                        destTile = chessBoard.getTile(this.tileId);
                        final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoords(),
                                destTile.getTileCoords());
                        final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                        buffer.appendText(transition.getMoveStatus().toString() + "\n");
                        if(transition.getMoveStatus().isDone()){
                            chessBoard = transition.getTransitionBoard(); //TODO: add this to move log
                            buffer.appendText(chessBoard.toString());
                        }
                        int startTileCoords = sourceTile.getTileCoords(), endTileCoords = destTile.getTileCoords();
                        sourceTile = null; destTile = null; movedPiece = null;
                        Platform.runLater(() -> boardGui.drawTransitionBoard(chessBoard, startTileCoords, endTileCoords));
                    }
                }
            });
        }

        public void drawTile(final Board board) {
            this.pieceImage = null;
            this.getChildren().clear();
            assignTilePieceIcon(board);
        }

        private void assignTileColor() {
            if (BoardUtils.FIRST_ROW[this.tileId] ||
                    BoardUtils.THIRD_ROW[this.tileId] ||
                    BoardUtils.FIFTH_ROW[this.tileId] ||
                    BoardUtils.SEVENTH_ROW[this.tileId]) {
                if(this.tileId % 2 == 0){
                    setStyle("-fx-background-color:#eeeed2;");
                } else {
                    setStyle("-fx-background-color:#769656;");
                }
            } else if(BoardUtils.SECOND_ROW[this.tileId] ||
                    BoardUtils.FOURTH_ROW[this.tileId] ||
                    BoardUtils.SIXTH_ROW[this.tileId]  ||
                    BoardUtils.EIGHTH_ROW[this.tileId]) {
                if(this.tileId % 2 != 0){
                    setStyle("-fx-background-color:#eeeed2;");
                } else {
                    setStyle("-fx-background-color:#769656;");
                }
            }
        }

        public String getCoords(){
            return this.coordinates;
        }

        public int getTileId(){
            return this.tileId;
        }

        public int calculateTileId(final int row, final int column){
            return row*8 + column;
        }

        private String calculateCoordinates(final int row, final int column){
            String coords;
            switch (column){
                case 0 -> coords = "a";
                case 1 -> coords = "b";
                case 2 -> coords = "c";
                case 3 -> coords = "d";
                case 4 -> coords = "e";
                case 5 -> coords = "f";
                case 6 -> coords = "g";
                case 7 -> coords = "h";
                default -> throw new RuntimeException("Invalid Coordinate");
            }
            coords += String.valueOf(7 - row + 1);
            return coords;
        }

        private void assignTilePieceIcon(final Board board){
            if(board.getTile(this.tileId).isOccupied()){
                String pieceIconPath = "file:src/PieceImages/";
                String pieceAlliance = board.getTile(this.tileId).getPiece().getPieceAlliance().toString();
                this.pieceImage = new ImageView(new Image(pieceIconPath + pieceAlliance + "/" + pieceAlliance + "_" +
                        board.getTile(this.tileId).getPiece().getPieceType().toString() + ".png"));
                this.pieceImage.setFitHeight(Table.TILE_DIMENSION*0.85);
                this.pieceImage.setFitWidth(Table.TILE_DIMENSION*0.85);
                this.pieceImage.setPreserveRatio(true);
                this.getChildren().add(this.pieceImage);
            }
        }
    }
}
