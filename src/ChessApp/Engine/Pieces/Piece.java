package ChessApp.Engine.Pieces;

import ChessApp.Engine.Board.Move;
import ChessApp.Engine.Board.Board;

import java.util.Collection;

public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    private final int cachedHashCode;

    Piece(final PieceType pieceType, final int piecePos, final Alliance pieceAlliance){
        this.piecePosition = piecePos;
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    @Override
    public boolean equals(final Object other){
        if(this == other) return true;
        if(!(other instanceof final Piece otherPiece)) return false;
        return piecePosition == otherPiece.getPiecePosition()  && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove;
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    private int computeHashCode(){
        int result =  pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public Piece.PieceType getPieceType(){
        return this.pieceType;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public boolean isFirstMove(){ return this.isFirstMove; }

    public abstract Piece movePiece(Move move);

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public enum PieceType {
        PAWN("P"),
        ROOK("R"){
            @Override
            public boolean isRook(){
                return true;
            }
        },
        KNIGHT("Kn"),
        BISHOP("B"),
        QUEEN("Q"),
        KING("K"){
            @Override
            public boolean isKing(){
                return true;
            }
        };
        private final String pieceName;
        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }
        public boolean isKing(){
            return false;
        }
        public boolean isRook() {
            return false;
        }
    }
}
