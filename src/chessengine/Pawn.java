package chessengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece
{
    private static final int[]CANDIDATE_MOVE_COORDINATE = {8,16,7,9};
    Pawn(final int piecePosition,final Alliance pieceAlliance)
    {
        super(PieceType.PAWN,piecePosition, pieceAlliance,true);
    }
    Pawn(final int piecePosition,final Alliance pieceAlliance,final boolean isFirstMove)
    {
        super(PieceType.PAWN,piecePosition, pieceAlliance,isFirstMove);
    }
    @Override
    public Collection<Move> calculateLegalMoves(final Board board)
    {
        final List<Move>legalMoves = new ArrayList<>(); 
        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE)
        {
            
            /*
            this is applying the offset to the current position 
            in order to find all legal moves
            also defines the direction in which the piece is going
            by multiplying by 1 or -1 from @Alliance enum
            */
            final int candidateDestinationCoordinate = this.piecePosition + 
                 this.pieceAlliance.getDirection() * currentCandidateOffset;
            if(!BoardUtil.isValidTileCoordinate(candidateDestinationCoordinate))
            {
                continue;
            }
            /*
            if tile is not occupied then we run it to all legal moves
            */
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied())
            {
                //hmm more WORK to do here (>_<)deal wit promotions!!!
                legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
            }
            else if (currentCandidateOffset == 16 && this.isFirstMove() 
                 && ((BoardUtil.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) 
                 || (BoardUtil.SECOND_RANK[this.piecePosition]&& this.getPieceAlliance().isWhite())) )
            {
                final int behindCandidateDestinationCoordinate 
                           = this.piecePosition + this.pieceAlliance.getDirection() * 8;
                /*
                if both the behind and current candiates arent occupied
                then we run legal moves
                
                make provisions for pawns that are on the first and right column 
                so that they cant capture a piece outside theri border
                */
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()
                && !board.getTile(candidateDestinationCoordinate).isTileOccupied())
                {
                    legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
                }
            }
                else if(currentCandidateOffset == 7 &&
                    !((BoardUtil.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                      (BoardUtil.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))))
                {
                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied())
                    {
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAlliance != pieceOnCandidate.pieceAlliance)
                        {
                            //more WORK to do 
                            legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate));
                        }
                    }
                    else if (board.getEnPassantPawn() != null) 
                    {
                        if(board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition + (this.pieceAlliance.getOppositeDirection())))
                        {
                            final Piece candidatePiece = board.getEnPassantPawn();
                            if (this.pieceAlliance != candidatePiece.getPieceAlliance()) 
                            {
                                legalMoves.add(
                                new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, candidatePiece));                        
                            }                            
                        }
                    }                   
                }
                else if (currentCandidateOffset == 9 &&
                    !((BoardUtil.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                      (BoardUtil.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))))
                {                    
                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied())
                    {
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAlliance != pieceOnCandidate.pieceAlliance)
                        {
                            //more WORK to do 
                            legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate));
                        }                        
                    }
                    else if (board.getEnPassantPawn() != null) 
                    {
                        if(board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition - (this.pieceAlliance.getOppositeDirection())))
                        {
                            final Piece candidatePiece = board.getEnPassantPawn();
                            if (this.pieceAlliance != candidatePiece.getPieceAlliance()) 
                            {
                                legalMoves.add(
                                new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, candidatePiece));                        
                            }                            
                        }
                    }                

                }        
        }
        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public String toString()
    {
        return pieceType.PAWN.toString();
    }
    
    @Override
    public Piece movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }    
}
