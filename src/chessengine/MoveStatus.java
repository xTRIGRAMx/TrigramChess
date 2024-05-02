/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessengine;

/**
 *
 * @author User
 */
public enum MoveStatus {
    Done {
        @Override
        public boolean isDone() 
        {
            return true;
        }
    },
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    LEAVE_PLAYER_IN_CHECK{
        @Override
        public boolean isDone(){
            return false;
        }
    };
    public abstract boolean isDone();
}
