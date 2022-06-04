package ChessApp.Engine.Pieces;

import ChessApp.Engine.Player.BlackPlayer;
import ChessApp.Engine.Player.Player;
import ChessApp.Engine.Player.WhitePlayer;

public enum Alliance {
    WHITE {
        @Override
        int getDirection() {
            return -1;
        }
        @Override
        public boolean isWhite(){ return true;}

        @Override
        public boolean isBlack() { return false;}

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer){
            return whitePlayer;
        }
        @Override
        public String toString(){
            return "White";
        }
    },
    BLACK{
        @Override
        int getDirection() {
        return 1;
      }

        @Override
        public boolean isWhite() { return false;}

        @Override
        public boolean isBlack() { return true;}

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer){
            return blackPlayer;
        }
        @Override
        public String toString(){
            return "Black";
        }
    };
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
    abstract int getDirection();
    public abstract boolean isWhite();

    public abstract boolean isBlack();
}
