/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chess;

import EngineGUI.Table;
import chessengine.Board;

/**
 *
 * @author xTRIGRAMx
 */
public class ChessEngine {
    
    public static void main(String[] args)
    {
        Board board =  Board.createStandardBoard();
        System.out.println(board);
        Table table = new Table();
    }
}
