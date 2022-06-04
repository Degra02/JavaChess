package ChessApp.Engine.Pieces;

import ChessApp.Engine.Board.Board;
import ChessApp.Engine.Board.BoardUtils;
import ChessApp.Engine.Board.Move;
import ChessApp.Engine.Board.Move.AttackMove;
import ChessApp.Engine.Board.Move.NormalMove;
import ChessApp.Engine.Board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece{

    private final int[] CANDIDATE_MOVE_VECTOR_COORDS = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen(final int piecePos, final Alliance pieceAlliance) {
        super(PieceType.QUEEN, piecePos, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        int candidateDestCoords;
        List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidate: CANDIDATE_MOVE_VECTOR_COORDS){
            candidateDestCoords = this.piecePosition;
            while(BoardUtils.isValidTileCoords(candidateDestCoords)){
                if(isFirstColumn(this.piecePosition, currentCandidate) ||
                        isLastColumn(this.piecePosition, currentCandidate)) break;
                candidateDestCoords += currentCandidate;
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
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -9) || candidateOffset == 7 ||
                candidateOffset == -1);
    }

    private static boolean isLastColumn(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -7) || candidateOffset == 9 ||
                candidateOffset == 1);
    }

    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }

    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getDestinationCoords(), move.getMovedPiece().getPieceAlliance());
    }
}
