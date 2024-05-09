/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chessengine.AI;

import chessengine.Board;

/**
 *
 * @author User
 */
public interface BoardEvaluator {
        int evaluate(Board board, int depth);
}
