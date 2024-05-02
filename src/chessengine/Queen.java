/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessengine;

import com.google.common.collect.ImmutableList;
import com.sun.imageio.plugins.common.I18N;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author User
 */
public class Queen extends Piece {
    
    private static final int[]CANDIDATE_MOVE_VECTOR_COORDINATE = {-9,-8,-7,-1,1,7,8,9};

    public Queen(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.QUEEN,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) 
    {
        final List<Move>legalMoves =new ArrayList<>(); 
        for(final int currentCandidateOffset:CANDIDATE_MOVE_VECTOR_COORDINATE)
        {
             /*
            this is applying the offset to the current position 
            in order to find all legal moves
            */
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtil.isValidTileCoordinate(candidateDestinationCoordinate))
            {                
                candidateDestinationCoordinate += currentCandidateOffset;
                if(BoardUtil.isValidTileCoordinate(candidateDestinationCoordinate))
                {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
                       ||isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)     )
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
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestinationTile));
                        } 
                        break;
                    } 
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public String toString()
    {
        return pieceType.QUEEN.toString();
    }
    
    @Override
    public Piece movePiece(Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
    
    /*
        exeptions to the constant rules of the candidate coordinates of this piece 
    */
    private static boolean isFirstColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.FIRST_COLUMN[currentPosition]&&(candidateOffset == -1 
                                                      || candidateOffset == -9
                                                      || candidateOffset ==  7);
    } 
    private static boolean isEighthColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.EIGHTH_COLUMN[currentPosition]&&(candidateOffset ==  1 
                                                       || candidateOffset ==  9
                                                       || candidateOffset == -7 );
    }

    
}
