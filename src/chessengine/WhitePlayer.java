package chessengine;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author User
 */
public class WhitePlayer extends Player
{
    public WhitePlayer(final Board board,final Collection<Move> whiteStandardLegalMoves,final Collection<Move> BlackStandardLegalMoves){
        super(board, whiteStandardLegalMoves, BlackStandardLegalMoves);
    }
    @Override
    public Collection<Piece> getActivePieces()
    {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.White;
    }            

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move>kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove && !this.isInCheck())
        {
            //Whites Kingside Castle
            if(!this.board.getTile(61).isTileOccupied()&& 
                    !this.board.getTile(62).isTileOccupied())
            {
                final Tile rookTile = this.board.getTile(63);
                if(rookTile.isTileOccupied()&& rookTile.getPiece().isFirstMove)
                {
                    if(Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(62, opponentLegals).isEmpty()&&
                       rookTile.getPiece().getPieceType().isRook());
                    {
                        //TODO ADD a CastleMove
                        kingCastles.add(new Move.KingSideCastleMove(board, 
                                playerKing,
                                62,
                                (Rook)rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                61) {  
                                    
                        });
                    }
                }
            }          
            //Whites Queenside Castle
            if(!this.board.getTile(59).isTileOccupied()&& 
                    !this.board.getTile(58).isTileOccupied() && 
                    !this.board.getTile(57).isTileOccupied())
            {
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied()&& rookTile.getPiece().isFirstMove)
                {
                    if(Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(58, opponentLegals).isEmpty()&&
                       rookTile.getPiece().getPieceType().isRook());
                    {
                        //TODO ADD a CastleMove
                        kingCastles.add(new Move.KingSideCastleMove(board, 
                                playerKing,
                                58,
                                (Rook)rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                59) {  
                                    
                        });                       
                    }                    
                }
            }                        
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
