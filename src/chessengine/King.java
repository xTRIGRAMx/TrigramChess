/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessengine;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author User
 */
public class King extends Piece{

    public King(final int piecePosition,final Alliance pieceAlliance) {
        super(PieceType.KING,piecePosition, pieceAlliance,true);
    }
    public King(final int piecePosition,final Alliance pieceAlliance,final boolean isFirstMove) {
        super(PieceType.KING,piecePosition, pieceAlliance,isFirstMove);
    }    
    private static final int[]CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9};

    @Override
    public Collection<Move> calculateLegalMoves(Board board) 
    {        
         final List<Move>legalMoves =new ArrayList<>();
         
         for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE)
         {
             final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
             if(BoardUtil.isValidTileCoordinate(candidateDestinationCoordinate))
             {
                 final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                 if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
                  ||isEighthColumnExclusion(this.piecePosition, currentCandidateOffset))
                 {
                     continue;
                 }
                 
                /*
                if a tile destination is NOT occupied then it qualifies as a legal move
                */
                if(!candidateDestinationTile.isTileOccupied())
                {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }else
                {
                    final Piece pieceAtDestinationTile = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestinationTile.getPieceAlliance();
                    if(this.pieceAlliance != pieceAlliance)
                    {
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestinationTile));
                    }
                }
               
             }
         }
         return ImmutableList.copyOf(legalMoves);
    }
        @Override
    public String toString()
    {
        return pieceType.KING.toString();
    }
    
    @Override
    public Piece movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
    
    private static boolean isFirstColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.FIRST_COLUMN[currentPosition]&&(candidateOffset == -9 
                                                      || candidateOffset == -1 
                                                      || candidateOffset == 7);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.EIGHTH_COLUMN[currentPosition]&&(candidateOffset == -7 
                                                      || candidateOffset == 1 
                                                      || candidateOffset == 9);
    }
}
