 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessengine;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author User
 */
public class Board {
    private final List<Tile>gameBoard;
    private final Collection<Piece>whitePieces;
    private final Collection<Piece>blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Collection<Piece> allPieces;
    private Board(final Builder builder)
    {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = CalculatedActivePieces(this.gameBoard,Alliance.White);
        this.blackPieces = CalculatedActivePieces(this.gameBoard,Alliance.Black);
        this.allPieces = AllCalculatedActivePieces(gameBoard);
        final Collection<Move>whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move>blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this ,whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this,blackStandardLegalMoves,blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
        
    }
    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        for(int i = 0;i < BoardUtil.NUM_TILES; i++)
        {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%s  " , tileText));
            if((i+1) % BoardUtil.NUM_TILES_PER_ROW == 0)
            {
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    public Player whitePlayer()
    {
        return this.whitePlayer;
    }
        public Player blackPlayer()
    {
        return this.blackPlayer;
    }
    public Player currentPlayer()
    {
        return this.currentPlayer;
    }
    public  Collection<Piece>getBlackPieces()
    {
        return this.blackPieces;
    }
    public  Collection<Piece>getWhitePieces()
    {
        return this.whitePieces;
    }
    public Collection<Piece>getAllActivePieces()
    {
        return this.allPieces;
    }
    private Collection<Move> calculateLegalMoves(Collection<Piece> Pieces) {
        final List<Move>legalMoves = new ArrayList<>();
        for(final Piece piece:Pieces)
        {
            legalMoves.addAll( piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
    public Tile getTile(final int tileCoordinate)
    {
        return gameBoard.get(tileCoordinate);
    }
     
     
    private static List<Tile> createGameBoard(final Builder builder)
    {
        
        //an array of 64 tiles
        
        final Tile[]tiles = new Tile[BoardUtil.NUM_TILES];
        for(int i = 0; i <BoardUtil.NUM_TILES;i++)
        {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    private static Collection<Piece> CalculatedActivePieces(List<Tile> gameBoard, Alliance alliance) 
    {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile:gameBoard)
        {
            if(tile.isTileOccupied())
            {
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance)
                {
                    activePieces.add(piece);
                }
            }
        }
        return Collections.unmodifiableList(activePieces);
    }
    
    private static Collection<Piece> AllCalculatedActivePieces(List<Tile> gameBoard) 
    {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile:gameBoard)
        {
            if(tile.isTileOccupied())
            {
                final Piece piece = tile.getPiece();
                activePieces.add(piece);
            }
        }
        return Collections.unmodifiableList(activePieces);
    }    

    Iterable<Move> getAllLegalMoves() 
    {
       return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),this.blackPlayer.getLegalMoves()));
    }

            
    public static class Builder
    {
        /*
        giving each tile coordinate its own uniqe signature 
        */
        Map<Integer, Piece>boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;
        public Builder()
        {
            this.boardConfig = new HashMap<>();
        }
        public Builder setPiece(final Piece piece)
        {
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }
        public Builder setMoveMaker(final Alliance nextMoveMaker)
        {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        
        public Board build()
        {
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
           this.enPassantPawn = enPassantPawn;
        }
    }
    
        public static Board createStandardBoard()
        {
            final Builder builder = new Builder();
            //Black layout
            builder.setPiece(new Rook(0,Alliance.Black));
            builder.setPiece(new Knight(1,Alliance.Black));
            builder.setPiece(new Bishop(2,Alliance.Black));
            builder.setPiece(new Queen(3,Alliance.Black));
            builder.setPiece(new King(4,Alliance.Black));
            builder.setPiece(new Bishop(5,Alliance.Black));
            builder.setPiece(new Knight(6,Alliance.Black));
            builder.setPiece(new Rook(7,Alliance.Black));
            builder.setPiece(new Pawn(8,Alliance.Black));
            builder.setPiece(new Pawn(9,Alliance.Black));
            builder.setPiece(new Pawn(10,Alliance.Black));
            builder.setPiece(new Pawn(11,Alliance.Black));
            builder.setPiece(new Pawn(12,Alliance.Black));
            builder.setPiece(new Pawn(13,Alliance.Black));
            builder.setPiece(new Pawn(14,Alliance.Black));
            builder.setPiece(new Pawn(15,Alliance.Black));
            //white layout
            builder.setPiece(new Pawn(48,Alliance.White));
            builder.setPiece(new Pawn(49,Alliance.White));
            builder.setPiece(new Pawn(50,Alliance.White));
            builder.setPiece(new Pawn(51,Alliance.White));
            builder.setPiece(new Pawn(52,Alliance.White));
            builder.setPiece(new Pawn(53,Alliance.White));
            builder.setPiece(new Pawn(54,Alliance.White));
            builder.setPiece(new Pawn(55,Alliance.White));
            builder.setPiece(new Rook(56,Alliance.White));
            builder.setPiece(new Knight(57,Alliance.White));
            builder.setPiece(new Bishop(58,Alliance.White));
            builder.setPiece(new King(60,Alliance.White));
            builder.setPiece(new Queen(59,Alliance.White));
            builder.setPiece(new Bishop(61,Alliance.White));
            builder.setPiece(new Knight(62,Alliance.White));
            builder.setPiece(new Rook(63,Alliance.White));
            //white to move
            builder.setMoveMaker(Alliance.White);            
            return builder.build();
        }
}
