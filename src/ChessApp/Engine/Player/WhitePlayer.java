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

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board,
                       final Collection<Move> legalMoves,
                       final Collection<Move> opponentMoves) {
        super(board, legalMoves, opponentMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !isInCheck()) {
            if (!this.board.getTile(61).isOccupied() && !this.board.getTile(62).isOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
                    if(Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                       rookTile.getPiece().getPieceType().isRook()){
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62,
                                (Rook)rookTile.getPiece(), rookTile.getTileCoords(), 61));
                    }
                }
            }
            if(!this.board.getTile(59).isOccupied() && !this.board.getTile(58).isOccupied() &&
                this.board.getTile(57).isOccupied()){
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                       rookTile.getPiece().getPieceType().isKing()){
                        kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58,
                                (Rook)rookTile.getPiece(), rookTile.getTileCoords(), 59));
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
