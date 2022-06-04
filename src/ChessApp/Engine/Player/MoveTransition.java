package ChessApp.Engine.Player;

import ChessApp.Engine.Board.Board;
import ChessApp.Engine.Board.Move;

public class MoveTransition {
    private  Board transitionBoard;
    private  Move move;
    private  MoveStatus moveStatus;

    public MoveTransition(){}

    public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }
}
