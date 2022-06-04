package ChessApp.Engine.Pieces;

import ChessApp.Engine.Board.Board;
import ChessApp.Engine.Board.BoardUtils;
import ChessApp.Engine.Board.Move;
import ChessApp.Engine.Board.Move.NormalMove;
import ChessApp.Engine.Board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece{
    final static int[] CANDIDATE_MOVE_COORDS = {8, 16};
    final static int[] CANDIDATE_ATTACK_COORDS = {9, 7};

    public Pawn(final int piecePos, final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePos, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) { // Also check if it's Pawn Promotion
        int candidateDestCoords;
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDS){
            candidateDestCoords = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
            if(!BoardUtils.isValidTileCoords(candidateDestCoords)) continue;
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestCoords).isOccupied()){
                legalMoves.add(new NormalMove(board, this, candidateDestCoords));
            } else if(currentCandidateOffset == 16  /*&& this.isFirstMove*/ &&
                    ((BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()))){
                //isFirstMove = false;
                final int behindCandidateDest = this.piecePosition + (this.getPieceAlliance().getDirection() * 8);
                if(!board.getTile(behindCandidateDest).isOccupied() &&
                   !board.getTile(candidateDestCoords).isOccupied()){
                    legalMoves.add(new NormalMove(board, this, candidateDestCoords));
                }
            }
        }
        for(final int currentCandidateAttackOffset: CANDIDATE_ATTACK_COORDS){
            if(isFirstColumn(this.piecePosition, currentCandidateAttackOffset, this.getPieceAlliance()) ||
                isLastColumn(this.piecePosition, currentCandidateAttackOffset, this.getPieceAlliance())) continue;
            candidateDestCoords = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateAttackOffset);
            final Tile candidateDestTile = board.getTile(candidateDestCoords);
            if(candidateDestTile.isOccupied() && (this.getPieceAlliance() != candidateDestTile.getPiece().getPieceAlliance())){
                legalMoves.add(new Move.AttackMove(board, this, candidateDestCoords, candidateDestTile.getPiece()));
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private boolean isFirstColumn(final int currentPosition, final int candidateOffset, Alliance pieceAlliance){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((pieceAlliance.isWhite() && candidateOffset == 9) ||
                (pieceAlliance.isBlack() && candidateOffset == 7));
    }

    private boolean isLastColumn(final int currentPosition, final int candidateOffset, Alliance pieceAlliance){
        return BoardUtils.LAST_COLUMN[currentPosition] && ((pieceAlliance.isWhite() && candidateOffset == 7) ||
                (pieceAlliance.isBlack() && candidateOffset == 9));
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoords(), move.getMovedPiece().getPieceAlliance());
    }
}
