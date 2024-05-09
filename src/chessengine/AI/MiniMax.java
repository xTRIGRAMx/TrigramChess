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
    
    public MiniMax()
    {
        this.boardEvaluator = new StandardBoardEvaluator();
    }
    
    @Override 
    public String toString()
    {
        return "miniMax";
    }
    
    @Override
    public Move execute(Board board, int depth)
    {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;   
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.currentPlayer() + " THINKING with depth = " + depth);
        
        final int numMoves = board.currentPlayer().getLegalMoves().size();
        for (final Move move : board.currentPlayer().getLegalMoves()) 
        { 
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
                if (moveTransition.getMoveStatus().isDone())
                {
                currentValue = board.currentPlayer().getAlliance().isWhite() ?
                                min(moveTransition.getTransitionBoard(), depth - 1) :
                                max(moveTransition.getTransitionBoard(), depth - 1);
                
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
    public int min(Board board, int depth)
    {
        //game over
        if(depth == 0) 
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
        if(depth == 0) 
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
