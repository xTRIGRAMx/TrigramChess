package chessengine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author User
 */
public abstract class Tile {
    /*
    defining a class that represents a tile tile
    key methods are to check if tile coordinate is occupide or not
    to get a piece on the tile
    */
    private static final Map<Integer,emptyTile>EMPTY_TILEScache = createAllPossibleEmptyTiles();

    private static Map<Integer, emptyTile> createAllPossibleEmptyTiles() 
    {
        final Map<Integer,emptyTile> emptyTileMap = new HashMap<>();
        for(int i = 0;i <BoardUtil.NUM_TILES;i++)
        {
            emptyTileMap.put(i, new emptyTile(i));
        }
        /*
        programmers recommend guava's immutable package but...
        for some of us with little internet access java has a
        package unmodifiableMap.
        */
        return Collections.unmodifiableMap(emptyTileMap);
        
    }
    /*
    only method you can use to create a tile
    but we can catch all possible occupied tiles
    */
    static Tile createTile(final int tileCoordinate,final Piece piece)
    {
        return piece != null ? new OccupiedTile(tileCoordinate, piece): EMPTY_TILEScache.get(tileCoordinate);
    }
    
    protected final int tileCoordinate;
    public abstract boolean isTileOccupied();
 
    /*
    @param tileCoordinate that is established
    */
    private Tile(final  int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }
    public abstract Piece getPiece();
    public int getTileCoordinate()
    {
        return this.tileCoordinate;
    }
    
    public static final class emptyTile extends Tile
    {
        private emptyTile(int coordinate) 
        {
            super(coordinate);
        }
        
        @Override
        public String toString()
        {
            return "-";
        }
        
        @Override
        public boolean isTileOccupied()
        {
            return false;
        }
        
        @Override
        public Piece getPiece()
        {
            return null;
        }
        
    }
    
    
    public static final class OccupiedTile extends Tile
    {
        private final Piece pieceOnTile;
        
        @Override
        public String toString()
        {
            return getPiece().getPieceAlliance().isBlack() 
                 ? getPiece().toString().toLowerCase() :getPiece().toString();
        }
        private OccupiedTile(final int Coordinate,final Piece pieceOnTile)//constructor takes coordinate and piece
        {
            /*
            tileCoordinate has been changed to coordinate to avoid confusion in the method
            */
            super(Coordinate);
            this.pieceOnTile = pieceOnTile;               
        }
        
        @Override
        public boolean isTileOccupied()
        {
            return true;
        }
        @Override
        public Piece getPiece()
        {
            return pieceOnTile;
        }
        /*
        will have to make minor improvments on this code
        */
    }
}
