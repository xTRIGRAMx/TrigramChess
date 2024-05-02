/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessengine;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author User
 */
public abstract class Piece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    private final int cachedHashCode;

    public Piece(final PieceType pieceType,final int piecePosition,final Alliance pieceAlliance) 
    {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }
    @Override
    public boolean equals(final Object other)
    {
        if(this == other)
        {
            return true;
        }
        if(!(other instanceof Piece))
        {
            return false;
        }
        final Piece otherPiece=(Piece)other;
        return piecePosition == otherPiece.getPiecePosition()&& pieceType == otherPiece.getPieceType()&&
               pieceAlliance == otherPiece.getPieceAlliance()&& isFirstMove == otherPiece.isFirstMove;
    }
    
    private int computeHashCode() 
    {
       int result = pieceType.hashCode();
       result = 31*result + pieceAlliance.hashCode();
       result = 31*result + piecePosition;
       result = 31*result + (isFirstMove ? 1 : 0);
       return result;        
    }  
    
    public int hashcode()
    {
        return this.cachedHashCode;
    }
    public int getPiecePosition()
    {
        return this.piecePosition;
    }
    public PieceType getPieceType()
    {
        return this.pieceType;
    }
    
    public boolean isFirstMove()
    {
        return this.isFirstMove;
    }
    public Alliance getPieceAlliance()
    {
        return this.pieceAlliance;
    }
    /*
    all pieces are going to override this method as they all have their own unique behavior
    */
    public abstract Collection<Move>calculateLegalMoves(final Board board);  
    
    public abstract Piece movePiece (Move move);

    public enum PieceType
    {
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }            
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }            
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }            
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }            
        };
        
        private String pieceName;
        PieceType(final String pieceName)
        {
            this.pieceName = pieceName;
        }
        @Override
        public String toString()
        {
            return this.pieceName;
        }
        public abstract boolean isKing(); 
        public abstract boolean  isRook();
    }
        
     
     
}
