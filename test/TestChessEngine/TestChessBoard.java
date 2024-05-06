/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestChessEngine;

import chessengine.Board;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
/**
 *
 * @author User
 */
public class TestChessBoard {
    public void initialBoard()
    {
        final Board board = Board.createStandardBoard();
        System.out.println(board.currentPlayer().getLegalMoves().size());
    }
    
}
