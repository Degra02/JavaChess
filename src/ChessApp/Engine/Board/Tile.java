package ChessApp.Engine.Board;

import ChessApp.Engine.Pieces.Alliance;
import ChessApp.Engine.Pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
    protected final int coords;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllEmptyTiles();

    private static Map<Integer, EmptyTile> createAllEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final int coords, final Piece piece){
        return piece != null ? new OccupiedTile(coords, piece) : EMPTY_TILES_CACHE.get(coords);
    }

    public abstract boolean isOccupied();

    public abstract Piece getPiece();

    private Tile(final int coords){
        this.coords = coords;
    }

    public int getTileCoords() {
        return this.coords;
    }

    public static final class EmptyTile extends Tile{
        EmptyTile(int coords){
            super(coords);
        }

        @Override
        public String toString(){
            return "-";
        }

        @Override
        public boolean isOccupied(){
            return false;
        }

        @Override
        public Piece getPiece(){
            return null;
        }
    }

    public static final class OccupiedTile extends Tile{

        private final Piece pieceOnTile;

        OccupiedTile(final int coords, final Piece piece) {
            super(coords);
            pieceOnTile = piece;
        }

        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }

        @Override
        public boolean isOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }
    }

}