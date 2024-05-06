/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessengine;

import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public class BoardUtil 
{
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7) ;
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8; 
    
    public static final boolean[] EIGHTH_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FOURTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);
    public final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
    public final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    
    /*
    method for initializing & keeping track of a particular column on the chessBoard
    */
    private static boolean[] initRow(int rowNumber)
    {
        final boolean [] row = new boolean[NUM_TILES];
        do
        {
            row[rowNumber] = true;
            rowNumber ++;
        }while(rowNumber % NUM_TILES_PER_ROW != 0);
        return row;    }
    private static boolean[] initColumn(int columnNumber) 
    {
        final boolean[] column = new boolean[64];
        do
        {
            column[columnNumber] = true;
            columnNumber +=NUM_TILES_PER_ROW;
        }while(columnNumber < NUM_TILES);
        return column;
    }   

    public int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }
    
    private BoardUtil()
    {
        /*
        just in case
        */
        throw new RuntimeException("you can't instantiate me dumbass!");
    }
    /*
    will be usefull to all the pieces
    must make it a utility method
     */
    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }
	// a utility method to calculate whether a piece is on the correct tile 
	
    
}
