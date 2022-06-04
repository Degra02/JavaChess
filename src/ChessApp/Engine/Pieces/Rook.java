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

public class Rook extends Piece{

    private final int[] CANDIDATE_MOVE_VECTOR_COORDS = {-8, -1, 1, 8};

    public Rook(final int piecePos, final Alliance pieceAlliance) {
        super(PieceType.ROOK, piecePos, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        int candidateDestCoords;
        List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset: CANDIDATE_MOVE_VECTOR_COORDS){
            candidateDestCoords = this.piecePosition;
            while(BoardUtils.isValidTileCoords(candidateDestCoords)){
                if(isFirstColumn(this.piecePosition, currentCandidateOffset) ||
                        isLastColumn(this.piecePosition, currentCandidateOffset)){
                    break;
                }
                candidateDestCoords += currentCandidateOffset;
                if(BoardUtils.isValidTileCoords(candidateDestCoords)){
                    final Tile candidateDestTile = board.getTile(candidateDestCoords);
                    if(!candidateDestTile.isOccupied()){
                        legalMoves.add(new NormalMove(board, this, candidateDestCoords));
                    } else {
                        final Piece pieceAtDest = candidateDestTile.getPiece();
                        if(this.pieceAlliance != pieceAtDest.getPieceAlliance()){
                            legalMoves.add(new AttackMove(board, this, candidateDestCoords, pieceAtDest));
                        }
                        break; // Because a Piece is occupying the vector
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumn(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }

    private static boolean isLastColumn(final int currentPosition, final int candidateOffset){
        return BoardUtils.LAST_COLUMN[currentPosition] && (candidateOffset == 1);
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    @Override
    public Rook movePiece(final Move move) {
        return new Rook(move.getDestinationCoords(), move.getMovedPiece().getPieceAlliance());
    }
}
