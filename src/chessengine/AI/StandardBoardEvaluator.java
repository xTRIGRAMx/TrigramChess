/*

 */
package chessengine.AI;

import chessengine.Board;
import chessengine.Piece;
import chessengine.Player;

/**
 *
 * @author User
 */
public class StandardBoardEvaluator implements BoardEvaluator {

    private static int CHECKMATE_BONUS = 10000;
    private static final int CHECK_BONUS = 50;
    private static final int DEPTH_BONUS = 100;
    private static int CASTLE_BONUS = 60;

    public StandardBoardEvaluator() {
    }

    @Override
    public int evaluate(final Board board,final int depth) {
        return scorePlayer(board,board.whitePlayer(), depth) - 
               scorePlayer(board,board.blackPlayer(), depth);
    }
    
    private int scorePlayer(Board board,
                Player player,
                int depth)
    {
        return pieceValue(player)+ 
               mobility(player)+ 
               check(player)+
               checkmate(player,depth)+
               castle(player);
    }
    
    private static int mobility(final Player player) {
        return player.getActivePieces().size();
    }
    
    private static int pieceValue(final Player player)
    {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) 
        {
                pieceValueScore += piece.getPieceValue();
            
        }
        return pieceValueScore;
    }  

    private static int depthBonus(int depth) {
        return depth == 0 ? 1: DEPTH_BONUS * depth;
    }    
    
    private static int check(Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS:0;
    }
    
    private static int checkmate(Player player,int depth)
    {
        return player.getOpponent().isInCheckmate() ? CHECKMATE_BONUS * depthBonus(depth):0;
    }
    
    private static int castle(final Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }
}
