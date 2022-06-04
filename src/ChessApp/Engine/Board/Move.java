package ChessApp.Engine.Board;

import ChessApp.Engine.Pieces.Pawn;
import ChessApp.Engine.Pieces.Piece;
import ChessApp.Engine.Pieces.Rook;

import static ChessApp.Engine.Board.Board.*;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destCoords;
    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destCoords){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destCoords = destCoords;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destCoords;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof final Move otherMove)) return false;
        return this.getDestinationCoords() == otherMove.getDestinationCoords() &&
                this.getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getDestinationCoords(){
        return this.destCoords;
    }

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public int getCurrentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }

    public Board execute() {
        final Builder builder = new Builder();
        for(final Piece piece: this.board.currentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static final class NormalMove extends Move{
        public NormalMove(final Board board, final Piece movedPiece, final int destCoords) {
            super(board, movedPiece, destCoords);
        }
    }

    public static class AttackMove extends Move{
        final Piece attackedPiece;
        public AttackMove(final Board board, final Piece movedPiece, final int destCoords, final Piece attackedPiece) {
            super(board, movedPiece, destCoords);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other){
            if(this == other) return true;
            if(!(other instanceof final AttackMove otherMove)) return false;
            return super.equals(otherMove) && getAttackedPiece().equals(otherMove.getAttackedPiece());
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!piece.equals(attackedPiece)){
                    builder.setPiece(piece);
                }
            }
            this.board.getBlackPieces().remove(this.attackedPiece); //TODO: change this, ImmutableList is more secure
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public boolean isAttack(){ return true; }

        @Override
        public Piece getAttackedPiece() { return this.attackedPiece; }
    }

    public static final class PawnMove extends Move{

        private PawnMove(final Board board, final Piece movedPiece, final int destCoords) {
            super(board, movedPiece, destCoords);
        }
    }

    public static class PawnAttackMove extends AttackMove{
        private PawnAttackMove(final Board board, final Piece movedPiece, final int destCoords, final Piece attackedPiece) {
            super(board, movedPiece, destCoords, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove{
        private PawnEnPassantAttackMove(final Board board, final  Piece movedPiece, final int destCoords, final Piece attackedPiece) {
            super(board, movedPiece, destCoords, attackedPiece);
        }
    }

    public static final class PawnJump extends Move{
        private PawnJump(final Board board, final Piece movedPiece, final int destCoords) {
            super(board, movedPiece, destCoords);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(! this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDest;
        private CastleMove(final Board board, final Piece movedPiece, final int destCoords, final Rook castleRook,
                           final int castleRookStart, final int castleRookDest) {
            super(board, movedPiece, destCoords);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDest = castleRookDest;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDest, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove{
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destCoords, final Rook castleRook,
                                  final int castleRookStart, final int castleRookDest) {
            super(board, movedPiece, destCoords, castleRook, castleRookStart, castleRookDest);
        }

        @Override
        public String toString(){
            return "O-O";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove{
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destCoords, final Rook castleRook,
                                   final int castleRookStart, final int castleRookDest) {
            super(board, movedPiece, destCoords, castleRook, castleRookStart, castleRookDest);
        }

        @Override
        public String toString(){
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move{
        private NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute null move");
        }
    }

    public static class MoveFactory {
        private MoveFactory(){
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(final Board board, final int currentCoords, final int destCoords){
            for(final Move move: board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoords && move.getDestinationCoords() == destCoords)
                    return move;
            }
            return NULL_MOVE;
        }
    }

}
