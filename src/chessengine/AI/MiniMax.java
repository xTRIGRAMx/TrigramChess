/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessengine.AI;

import chessengine.Board;
import chessengine.Move;
import chessengine.MoveTransition;

/**
 *
 * @author User
 */
public class MiniMax implements MoveStrategy{
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    public MiniMax(final int searchDepth)
    {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }
    
    @Override 
    public String toString()
    {
        return "miniMax";
    }
    
    @Override
    public Move execute(Board board)
    {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;   
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.currentPlayer() + " THINKING with depth = " + this.searchDepth);
        
        final int numMoves = board.currentPlayer().getLegalMoves().size();
        for (final Move move : board.currentPlayer().getLegalMoves()) 
        { 
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
                if (moveTransition.getMoveStatus().isDone())
                {
                currentValue = board.currentPlayer().getAlliance().isWhite() ?
                                min(moveTransition.getTransitionBoard(), this.searchDepth - 1) :
                                max(moveTransition.getTransitionBoard(), this.searchDepth - 1);
                
                    if (board.currentPlayer().getAlliance().isWhite() &&
                        currentValue >= highestSeenValue) 
                    {
                        highestSeenValue = currentValue;
                        bestMove = move;
                    } else if (board.currentPlayer().getAlliance().isBlack() &&
                        currentValue <= lowestSeenValue) 
                    {
                        lowestSeenValue = currentValue;
                        bestMove = move;
                    }                
                }               
        } 
        final long executionTime = System.currentTimeMillis() - startTime;
        return bestMove;
    }
    
    private static boolean isEndGameScenario(final Board board) {
        return board.currentPlayer().isInCheckmate() || board.currentPlayer().isInStalemate();
    }    
    
    public int min(Board board, int depth)
    {
        /*
            continue to search the in game tree until the game has ended
            when a checkmate situation has been reached within the min max tree,
            discontinue searching for top engine move
            game over
        */
        if(depth == 0 || isEndGameScenario(board)) 
        {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }
    public int max(Board board, int depth)
    {
        //game over
        if(depth == 0 || isEndGameScenario(board)) 
        {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeemValue = Integer.MIN_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue <= highestSeemValue) {
                    highestSeemValue = currentValue;
                }
            }
        }
        return highestSeemValue;
    }
}
