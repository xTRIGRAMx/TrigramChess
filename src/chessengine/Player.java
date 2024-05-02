/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessengine;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author User
 */
public abstract class Player {

     
    protected  final Board board;
    protected final King playerKing;
    protected final Collection<Move>legalMoves;
    private final boolean isInCheck;
    
    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move>opponentMoves)
    {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
        
        
    }
    public King getPlayerKing()
    {
        return this.playerKing;
    }
    public Collection<Move> getLegalMoves()
    {
        return this.legalMoves;
    }
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition ,Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move:moves)
        {
            if(piecePosition == move.getDestinationCoordinate())
            {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }
    private King establishKing()
    {
        for(final  Piece piece : getActivePieces())
        {
            if(piece.getPieceType().isKing())
            {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here! Not a valid Board!");
    }
    //TODO implement  these methods bellow!!!
    public boolean isMoveLegal(final Move move)
    {
        return this.legalMoves.contains(move);
    }
    public boolean  isInCheck()
    {
        return this.isInCheck;
    }
    public boolean  isInCheckmate()
    {
        return this.isInCheck && !hasEscapeMoves();
    }
    
    public boolean  isInStalemate()
    {
        return !this.isInCheck && !!hasEscapeMoves();
    }
    
    protected boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves)
        {
            final MoveTransition transition  = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
                    }
        return false;
    }

    public boolean  isCastled()
    {   
        return false;
    }
    public MoveTransition makeMove(final Move move)
    {
        final Board.Builder builder = new Board.Builder();
        if(!isMoveLegal(move))
        {
            return new MoveTransition(this.board,move, MoveStatus.ILLEGAL_MOVE);
        }       
        final Board transitionBoard = move.execute();
        final Collection<Move>KingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), 
                transitionBoard.currentPlayer().getLegalMoves());
        if(!KingAttacks.isEmpty())
        {
            return new MoveTransition(this.board, move, MoveStatus.LEAVE_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.Done);
    }
    public abstract Collection<Piece> getActivePieces();
    public abstract  Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move>playerLegals,Collection<Move>opponentLegals);
}
