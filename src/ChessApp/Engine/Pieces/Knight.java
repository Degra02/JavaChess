package ChessApp.Engine.Pieces;

import ChessApp.Engine.Board.Board;
import ChessApp.Engine.Board.BoardUtils;
import ChessApp.Engine.Board.Move;
import ChessApp.Engine.Board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ChessApp.Engine.Board.Move.*;

public class Knight extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDS = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePos, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT, piecePos, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        int candidateDestCoords;
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDS){
            candidateDestCoords = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isValidTileCoords(candidateDestCoords)){
                if(isFirstColumn(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumn(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumn(this.piecePosition, currentCandidateOffset) ||
                        isLastColumn(this.piecePosition, currentCandidateOffset)){
                    continue;
                }
                final Tile candidateDestTile = board.getTile(candidateDestCoords);
                if(!candidateDestTile.isOccupied()){
                    legalMoves.add(new NormalMove(board, this, candidateDestCoords));
                } else {
                    final Piece pieceAtDest = candidateDestTile.getPiece();
                    if(this.pieceAlliance != pieceAtDest.getPieceAlliance()){
                        legalMoves.add(new AttackMove(board, this, candidateDestCoords, pieceAtDest));
                    }
                }
            } // return a Move only if possible
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumn(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumn(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == -6));
    }

    private static boolean isSeventhColumn(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == -6) || (candidateOffset == 10));
    }

    private static boolean isLastColumn(final int currentPosition, final int candidateOffset){
        return BoardUtils.LAST_COLUMN[currentPosition] && ((candidateOffset == -15) || (candidateOffset == -6) ||
                (candidateOffset == 10) || (candidateOffset == 17));
    }

    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getDestinationCoords(), move.getMovedPiece().getPieceAlliance());
    }
}
