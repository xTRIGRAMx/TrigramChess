package chessengine;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Bishop extends Piece
{
    private static final int[]CANDIDATE_MOVE_VECTOR_COORDINATE = {-9,-7,7,9};
    public Bishop(final int piecePosition,final Alliance pieceAlliance) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance,true);
    }
    public Bishop(final int piecePosition,final Alliance pieceAlliance,final boolean isFirstMove) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance,isFirstMove);
    }    
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) 
    {
        final List<Move>legalMoves =new ArrayList<>(); 
        for(final int currentCandidateOffset:CANDIDATE_MOVE_VECTOR_COORDINATE)
        {
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtil.isValidTileCoordinate(candidateDestinationCoordinate))
            {                
                if(isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)
                    ||isEighthColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)     )
                {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if(BoardUtil.isValidTileCoordinate(candidateDestinationCoordinate))
                {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);                          
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
        return Collections.unmodifiableCollection(legalMoves);
    }
    @Override
    public String toString()
    {
        return pieceType.BISHOP.toString();
    }
    /*
    these are the exeptions to the constant rules of the candidate coordinates of this piece 
    */
    private static boolean isFirstColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.FIRST_COLUMN[currentPosition]&&(candidateOffset == -9 
                                                      || candidateOffset ==  7);
    } 
    private static boolean isEighthColumnExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtil.EIGHTH_COLUMN[currentPosition]&&(candidateOffset == -7 
                                                       || candidateOffset ==  9);
    }

    @Override
    public Piece movePiece(Move move) {
        return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
}
