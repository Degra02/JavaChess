package ChessApp.Engine;

import ChessApp.Engine.Board.Board;

public class MainTesting {
    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        System.out.println(board);
    }
}
