package ChessApp.Engine.Player;

import ChessApp.Engine.Board.Board;
import ChessApp.Engine.Board.Move;
import ChessApp.Engine.Board.Move.KingSideCastleMove;
import ChessApp.Engine.Board.Move.QueenSideCastleMove;
import ChessApp.Engine.Board.Tile;
import ChessApp.Engine.Pieces.Alliance;
import ChessApp.Engine.Pieces.Piece;
import ChessApp.Engine.Pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board,
                       final Collection<Move> opponentMoves,
                       final Collection<Move> legalMoves){
        super(board, legalMoves, opponentMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance(){
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !isInCheck()) {
            if (!this.board.getTile(5).isOccupied() && !this.board.getTile(6).isOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
                    if(Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 6,
                                (Rook)rookTile.getPiece(), rookTile.getTileCoords(), 5));
                    }
                }
            }
            if(!this.board.getTile(3).isOccupied() && !this.board.getTile(2).isOccupied() &&
                    this.board.getTile(1).isOccupied()){
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isKing()){
                        kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 2,
                                (Rook)rookTile.getPiece(), rookTile.getTileCoords(), 3));
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
