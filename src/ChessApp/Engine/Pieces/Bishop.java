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

public class Bishop extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDS = {-9, -7, 7, 9};

    public Bishop(final int piecePos, final Alliance pieceAlliance) {
        super(PieceType.BISHOP, piecePos, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        int candidateDestCoords;
        List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset: CANDIDATE_MOVE_VECTOR_COORDS){
            if(isFirstColumn(this.piecePosition, currentCandidateOffset) ||
                isLastColumn(this.piecePosition, currentCandidateOffset)){
                break;
            }
            candidateDestCoords = this.piecePosition;
            while(BoardUtils.isValidTileCoords(candidateDestCoords)){
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
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -9) || candidateOffset == 7);
    }

    private static boolean isLastColumn(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -7) || candidateOffset == 9);
    }

    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }

    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getDestinationCoords(), move.getMovedPiece().getPieceAlliance());
    }
}
