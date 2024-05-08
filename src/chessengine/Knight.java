package chessengine; 

import chessengine.Move.MajorMove;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Knight extends Piece   
{
    /*
    most of the legal moves of the piece
    
    */
    private static final int[]CANDIDATE_MOVE_COORDINATE = {-17,-15,-10,-6,6,10,15,17};
    
    Knight(final int piecePosition,final Alliance pieceAlliance)
    {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance,true);
    }
    Knight(final int piecePosition,final Alliance pieceAlliance,final boolean isFirstMove)
    {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance,isFirstMove);
    }    
    
    @Override
    public Collection<Move>calculateLegalMoves(final Board board)
    {
        final List<Move>legalMoves =new ArrayList<>();          
        /*
        applying the offset list of moves to the current position 
        */
        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE)
        {
            /*
            this is applying the offset to the current possition 
            in order to find all ligal moves
            */
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(BoardUtil.isValidTileCoordinate(candidateDestinationCoordinate))
            {
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
                 ||isSecondColumnExclusion(this.piecePosition, currentCandidateOffset)
                 ||isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)
                 ||isEighthColumnExclusion(this.piecePosition, currentCandidateOffset))
                {
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                /*
                if a tile destination is NOT occupied then it qualifies as a legal move
                */
                if(!candidateDestinationTile.isTileOccupied())
                {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }else
                {
                    final Piece pieceAtDestinationTile = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestinationTile.getPieceAlliance();
                    if(this.pieceAlliance != pieceAlliance)
                    {
                        legalMoves.add(new Move.MajorAttackMove(board, pieceAtDestinationTile, candidateDestinationCoordinate, pieceAtDestinationTile));
                    }
                }
               
            }
            
        }
        return  Collections.unmodifiableList(legalMoves);
        
    }
        @Override
    public String toString()
    {
        return pieceType.KNIGHT.toString();
    }
    
    @Override
    public Piece movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
    
    /* 
    if the piece is in the first column of a chessBoard then the candidate destination must be adjusted according to the excention
    */
    private static boolean isFirstColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.FIRST_COLUMN[currentPosition]&&(candidateOffset == -17 
                                                      || candidateOffset == -10 
                                                      || candidateOffset == 6
                                                      || candidateOffset == 15);
    }
    private static boolean isSecondColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.SECOND_COLUMN[currentPosition]&&(candidateOffset == -10 
                                                       || candidateOffset == -6);
    }
    
    private static boolean isSeventhColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.SEVENTH_COLUMN[currentPosition]&&(candidateOffset == -6
                                                        || candidateOffset == 10) ;
    }
     private static boolean isEighthColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.EIGHTH_COLUMN[currentPosition] &&(candidateOffset == -15
                                                        || candidateOffset == -6
                                                        || candidateOffset == 10
                                                        || candidateOffset == 17);
    }
}
