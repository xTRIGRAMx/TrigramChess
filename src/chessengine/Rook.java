/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author User
 */
public class Rook extends Piece
{
    private static final int[]CANDIDATE_MOVE_VECTOR_COORDINATE = {-8,-1,1,8};

    public Rook(final int piecePosition,final Alliance pieceAlliance) 
    {
        super(PieceType.ROOK,piecePosition,pieceAlliance,true);
    }
    public Rook(final int piecePosition,final Alliance PieceAlliance,final boolean isFirstMove)
    {
        super(PieceType.ROOK,piecePosition,PieceAlliance,isFirstMove);
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
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestinationTile));
                        } 
                        break;
                    } 
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
        @Override
    public String toString()
    {
        return pieceType.ROOK.toString();
    }
   @Override
    public Piece movePiece(Move move) {
        return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
    /*
    these are the exeptions to the constant rules of the candidate coordinates of this piece 
    */
    private static boolean isFirstColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.FIRST_COLUMN[currentPosition]&&(candidateOffset == -1 );
    } 
    private static boolean isEighthColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.EIGHTH_COLUMN[currentPosition]&&(candidateOffset == 1 );
    }
}
