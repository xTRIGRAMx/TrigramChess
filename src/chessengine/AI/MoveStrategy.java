/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessengine.AI;

import chessengine.Board;
import chessengine.Move;

/**
 *
 * @author User
 */
public interface MoveStrategy {
    Move execute(Board board,int depth);

}
