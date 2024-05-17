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
public class BlackPlayer extends Player
{
    public BlackPlayer(final Board board,final Collection<Move> blackStandardLegalMoves, final Collection<Move> whiteStandardLegalMoves)
    {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }
    @Override
    public Collection<Piece> getActivePieces()
    {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.Black;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,final Collection<Move> opponentLegals) {
        final List<Move>kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove && !this.isInCheck())
        {
            //Black Kingside Castle
            if(!this.board.getTile(5).isTileOccupied()&& 
                    !this.board.getTile(6).isTileOccupied())
            {
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied()&& rookTile.getPiece().isFirstMove)
                {
                    if(Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(6, opponentLegals).isEmpty()&&
                       rookTile.getPiece().getPieceType().isRook());
                    {
                        kingCastles.add(new Move.KingSideCastleMove(this.board, 
                                this.playerKing,
                                6,
                                (Rook)rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                5) {  
                                    
                        });                        
                    }
                }
            }
            
            //Black Queenside Castle
            if(!this.board.getTile(1).isTileOccupied()&& 
                    !this.board.getTile(2).isTileOccupied() && 
                    !this.board.getTile(3).isTileOccupied())
            {
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied()&& rookTile.getPiece().isFirstMove)
                {
                    if(
                       Player.calculateAttacksOnTile(2, opponentLegals).isEmpty()&&
                       Player.calculateAttacksOnTile(3, opponentLegals).isEmpty()&&
                       rookTile.getPiece().getPieceType().isRook());
                    {
                        kingCastles.add(new Move.KingSideCastleMove(board, 
                                playerKing,
                                2,
                                (Rook)rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                3) {  
                                    
                        });                        
                    }                    
                }
            }                        
        }
        return ImmutableList.copyOf(kingCastles);
    }    
}
