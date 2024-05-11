/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package TestChessEngine;

import chessengine.AI.MiniMax;
import chessengine.AI.MoveStrategy;
import chessengine.Board;
import chessengine.BoardUtil;
import chessengine.Move;
import chessengine.Move.MoveFactory;
import chessengine.MoveTransition;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sun.util.resources.Bundles;

/**
 *
 * @author User
 */
public class TestChessEngine {
    
    public TestChessEngine() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() 
    {
        final Board board = Board.createStandardBoard();
        assertEquals(board.currentPlayer().getLegalMoves().size(), 20);   
        System.out.println(board.currentPlayer().getLegalMoves().size()); 
        assertFalse(board.currentPlayer().isInCheck());  
        assertFalse(board.currentPlayer().isCastled());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());  
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isCastled());  
//        assertTrue(board.whitePlayer().toString().equals("White"));
//        assertTrue(board.blackPlayer().toString().equals("Black"));     
    }
    @Test
    public void testFoolsMate() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtil.getCoordinateAtPosition("f2"),
                                BoardUtil.getCoordinateAtPosition("f3")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.getTransitionBoard(), BoardUtil.getCoordinateAtPosition("e7"),
                                BoardUtil.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.getTransitionBoard(), BoardUtil.getCoordinateAtPosition("g2"),
                                BoardUtil.getCoordinateAtPosition("g4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveStrategy moveStrategy = new MiniMax(4);
        final Move aiMove = moveStrategy.execute(t3.getTransitionBoard());
        final Move bestMove = Move.MoveFactory.createMove(t3.getTransitionBoard(), 
                BoardUtil.getCoordinateAtPosition("d8"), BoardUtil.getCoordinateAtPosition("h4"));
        assertEquals(aiMove,bestMove);
//        final MoveTransition t4 = t3.getTransitionBoard()
//                .currentPlayer()
//                .makeMove(MoveFactory.createMove(t3.getTransitionBoard(), BoardUtil.getCoordinateAtPosition("d8"),
//                                BoardUtil.getCoordinateAtPosition("h4")));
//
//        assertTrue(t4.getMoveStatus().isDone());
//
//        assertTrue(t4.getTransitionBoard().currentPlayer().isInCheckmate());

    }
}
