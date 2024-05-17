package chessengine;
/**
 *
 * @author User
 */

public enum Alliance 
{
    White 
    {
        @Override 
        public boolean isWhite()
        {
            return true;
        }
        
        @Override 
        public boolean isBlack()
        {
            return false;
        }
        @Override
        public int getDirection() 
        {
            return -1 ;
        }
        @Override
        public int getOppositeDirection() {
            return 1;
        }
        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer)
        {
            return whitePlayer;
        }
        @Override
        public String toString() {
            return "White";
        }        

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtil.EIGHTH_RANK[position];
        }
    },
    
    Black 
    {
        
        @Override 
        public boolean isWhite()
        {
            return false;
        }
        
        @Override 
        public boolean isBlack()
        {
            return true;
        }
        
        @Override
        public int getDirection() 
        {
            return 1 ;
        }
  
        @Override
        public int getOppositeDirection() {
            return -1;
        }
        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer)
        {
            return blackPlayer;
        }
        @Override
        public String toString() {
            return "Black";
        }        

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtil.FIRST_RANK[position];
        }
    };
    public abstract int getDirection();
    public abstract int getOppositeDirection();
    public abstract boolean isBlack();
    public abstract boolean isWhite();
    public abstract boolean isPawnPromotionSquare(int position);
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
    
    
}
