package chessengine;

import chessengine.Board.Builder;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public abstract class Move 
{
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    final boolean isFirstMove;
    public static final  Move NULL_MOVE = new NullMove() {
    };
    Move(final Board board, 
                 final Piece movedPiece, 
                 final int destinationCoordinate) 
    {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    Move(final Board board, 
        final int destinationCoordinate) 
    {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }
    @Override 
    public boolean equals(final Object other)
    {
        if(this == other)
        {
            return true;
        }
        if(!(this == other))
        {
            return false;
        }
        final Move otherMove = (Move)other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getDestinationCoordinate()== otherMove.getDestinationCoordinate() &&
               getMovedPiece().equals(otherMove.getMovedPiece()) ;
    }
    public int getCurrentCoordinate()
    {
        return this.getMovedPiece().getPiecePosition();
    }
    
    public Board getBoard()
    {
        return this.board;
    }
    public boolean isAttack()
    {
        return false;
    }
    
    public boolean  isCastlingMove()
    {
        return false;
    }
    public Piece getAttackedPiece()
    {
        return null;
    }
     /*
    Use the board builder to materialize new board from execute
    going through all the pieces that aren't moved and place on new board
     */   
    public Board execute()
    {
            final Board.Builder builder = new Board.Builder();
            Collection<Piece> allPieces = this.board.getAllActivePieces();
            for (final Piece piece : allPieces) 
            {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }           
            }             
            builder.setPiece(this.movedPiece.movePiece(this));
            
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();      
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    public Piece getMovedPiece()
    {
        return this.movedPiece;
    }
    public static final class MajorMove extends Move
    {
        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate)
        {
            super(board,movedPiece,destinationCoordinate);      
        }
        @Override
        public boolean equals(final Object other)
        {
            return this ==(other) || other instanceof MajorMove && super.equals(other);
        }
        @Override
        public String toString()
        {
            return movedPiece.getPieceType().toString() +
            BoardUtil.getPositionAtCoordinate(this.destinationCoordinate);            
        }
    }
    
    public static class MajorAttackMove
            extends AttackMove {

        public MajorAttackMove(final Board board,
                               final Piece pieceMoved,
                               final int destinationCoordinate,
                               final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorAttackMove && super.equals(other);

        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + "x" +
                   BoardUtil.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    public static class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        /*
        Use the board builder to materialize new board from execute
        going through all the pieces that aren't moved and place on new board
         */      
        @Override
        public int hashCode()
        {
            return this.attackedPiece.hashCode()+ super.hashCode();
        }
        
        @Override
        public boolean equals(final Object other)
        {
            if(this == other)
            {
                return true;
            }
            if(!(other instanceof AttackMove))
            {
                return false;
            }
            
            final AttackMove otherAttackMove = (AttackMove)other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
        @Override
        public boolean isAttack()
        {
            return true;
        }
    
        @Override
        public Piece getAttackedPiece()
        {
            return this.attackedPiece;
        }

    }    
    
    public static final class PawnMove extends Move
    {
        public PawnMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate)
        {
            super(board,movedPiece,destinationCoordinate);      
        }
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }        
        @Override
        public String toString()
        {
            return movedPiece.getPieceType().toString() +
            BoardUtil.getPositionAtCoordinate(this.destinationCoordinate);            
        }        
    }

    public static class PawnAttackMove extends AttackMove
    {
        public PawnAttackMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate,
                         final Piece attackedPiece)
        {
            super(board,movedPiece,destinationCoordinate,attackedPiece);      
        }
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtil.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" +
                   BoardUtil.getPositionAtCoordinate(this.destinationCoordinate);
        }        
    }    
    
    public static final class PawnEnPassantAttackMove extends PawnAttackMove
    {
        public PawnEnPassantAttackMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate,
                         final Piece attackedPiece)
        {
            super(board,movedPiece,destinationCoordinate,attackedPiece);      
        } 
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();
            Collection<Piece> allPieces = this.board.getAllActivePieces();
            for (final Piece piece : allPieces) 
            {
                if (!(this.movedPiece.equals(piece)) && !piece.equals(this.attackedPiece) ) {
                    builder.setPiece(piece);
                }           
            }  
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this); 
            builder.setPiece(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    
    public static final class PawnJump extends Move
    {
        public PawnJump(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate)
        {
            super(board,movedPiece,destinationCoordinate);      
        }  
        @Override
        public Board execute()
        {
            final Board.Builder builder = new Board.Builder();
            Collection<Piece> allPieces = this.board.getAllActivePieces();
            for (final Piece piece : allPieces) 
            {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }           
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        @Override
        public String toString()
        {
            return BoardUtil.getPositionAtCoordinate(this.destinationCoordinate);            
        }        
    }
    
    public static final class PawnPromotion extends Move
    {
        final Move decoratedMove;
        final Pawn promotedPawn;
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(),decoratedMove.getMovedPiece(),decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn)getMovedPiece();
        }  
        
        @Override
        public int hashCode()
        {
            return decoratedMove.hashCode()+31* promotedPawn.hashCode();
        }
        
        @Override
        public boolean equals(final Object other)
        {
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }
        @Override 
        public Board execute()
        {
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Builder();
            for (final Piece piece : pawnMovedBoard.getAllActivePieces()) 
            {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }           
            }             
            builder.setPiece(this.promotedPawn.getPromotedPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());            
            return builder.build();
        }
        @Override
        public boolean isAttack()
        {
            return this.decoratedMove.isAttack();
        }
        
        @Override
        public Piece getAttackedPiece()
        {
            return this.decoratedMove.getAttackedPiece();
        }
        
        @Override
        public String toString()
        {
            return "";
        }
        
    }
    static abstract class CastleMove extends Move
    {
        protected  final Rook castleRook;
        protected  final int castleRookStart;
        protected  final int castleRookDestination;
        
        public CastleMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate,
                         final Rook castleRook,
                         final int castleRookStart,
                         final int castleRookDestination)
        {
            super(board,movedPiece,destinationCoordinate); 
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        
        public Rook getCastleRook()
        {
            return this.castleRook;
        }
        @Override
        public boolean isCastlingMove()
        {
            return true;
        }
        
        @Override
        public Board execute()
        {
            final Board.Builder builder = new Board.Builder();
            Collection<Piece> allPieces = this.board.getAllActivePieces();
            for (final Piece piece : allPieces) 
            {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }           
            }             
            builder.setPiece(this.movedPiece.movePiece(this));
            
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        
                @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastleMove)) {
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static abstract class KingSideCastleMove extends CastleMove
    {
        public KingSideCastleMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate,
                         final Rook castleRook,
                         final int castleRookStart,
                         final int castleRookDestination)
        {
            super(board,movedPiece,destinationCoordinate,castleRook, castleRookStart, castleRookDestination);      
        }
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof KingSideCastleMove)) {
                return false;
            }
            final KingSideCastleMove otherKingSideCastleMove = (KingSideCastleMove) other;
            return super.equals(otherKingSideCastleMove) && this.castleRook.equals(otherKingSideCastleMove.getCastleRook());
        }        
        @Override
        public String toString()
        {
            return  "0-0";
        }
    }
    public static abstract class QueenSideCastleMove extends CastleMove
    {
        public QueenSideCastleMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate,
                         final Rook castleRook,
                         final int castleRookStart,
                         final int castleRookDestination)
        {
            super(board,movedPiece,destinationCoordinate,castleRook, castleRookStart, castleRookDestination);      
        }
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof QueenSideCastleMove)) {
                return false;
            }
            final QueenSideCastleMove otherQueenSideCastleMove = (QueenSideCastleMove) other;
            return super.equals(otherQueenSideCastleMove) && this.castleRook.equals(otherQueenSideCastleMove.getCastleRook());
        }        
        @Override
        public String toString()
        {
            return  "0-0-0";
        }        
    }     
    
    public static abstract class NullMove extends Move
    {
        public NullMove()
        {
            super(null,-1);      
        }
        @Override
        public int getCurrentCoordinate() {
            return -1;
        }

        @Override
        public int getDestinationCoordinate() {
            return -1;
        }
        @Override
        public Board execute()
        {
            throw new RuntimeException("Cannot execute the null move");
        }
    }
    public static class MoveFactory
    {
        private MoveFactory()
        {
            throw new RuntimeException("Not instantiable!");
        }
        public static Move createMove(final Board board,
                final int currentCoordinate,
                final int destinationCoorditate)
        {
            ArrayList<Move> legals = new ArrayList<Move>();
            for(final Move move :board.getAllLegalMoves())
            {
                if(move.getCurrentCoordinate() == currentCoordinate &&
                   move.getDestinationCoordinate() == destinationCoorditate )
                {
                    legals.add(move);
                    return move;
                }
                
            }
            return NULL_MOVE;
        }
    }
    
}
