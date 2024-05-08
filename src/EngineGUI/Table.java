/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineGUI;
import chessengine.*;
import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 *
 * @author User
 */
public class Table {
    private Board chessBoard;
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);    
    private static final  Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static final String defaultPieceImagePath = "images/Crusaders/";
    
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor =  Color.decode("#593E1A");

    public Table()
    {
        this.gameFrame = new JFrame("TrigramChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableBarMenu = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableBarMenu);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION); 
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.boardDirection = BoardDirection.NORMAL;

    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }
    private JMenu createFileMenu()
    {
        final JMenu fileMenu = new JMenu("file");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up that PGN file!");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;      
    }
    
    private JMenu createPreferencesMenu()
    {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBarMenuItem = new JMenuItem("Flip Board");
        flipBarMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBarMenuItem);
        return preferencesMenu;
    }
    
    private class BoardPanel extends JPanel
    {
        final List<TilePanel> boardTiles;

        public BoardPanel() {
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i < BoardUtil.NUM_TILES; i++)
            {
                final TilePanel tilePanel = new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.decode("#8B4721"));
            validate();
        }
        public void drawBoard(final Board board)
        {
            removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles))
            {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }
    
    public static class MoveLog
    {
        private final List<Move> moves;

        public MoveLog() 
        {
            this.moves = new ArrayList<>();
        }
        
        public List<Move> getMoves()
        {
            return this.moves;
        }
        
        public void addMoves(final Move move)
        {
            this.moves.add(move);
        }
        
        public int size()
        {
            return this.moves.size();
        }
        
        public void clear()
        {
            this.moves.clear();
        }
        
        public Move removeMove(int index)
        {
            return this.moves.remove(index);
        }
        
        public boolean removeMove(final Move move)
        {
            return this.moves.remove(move);
        }
    }
    
    private class TilePanel extends JPanel
    {
        private final int tileId;
        TilePanel(final BoardPanel boardPanel, final int tileId)
        { 
             super(new GridBagLayout());
             
             this.tileId = tileId;
             setPreferredSize(TILE_PANEL_DIMENSION);
             assignTileColor();
             assignTilePieceIcon(chessBoard);
//             highlightLegalMoves(chessBoard);
             addMouseListener(new MouseListener() {
                 @Override
                 public void mouseClicked(MouseEvent e) {
                     if(SwingUtilities.isRightMouseButton(e))
                     {
                         sourceTile = null;
                         destinationTile = null;
                         humanMovedPiece = null;
                     }
                     else if(SwingUtilities.isLeftMouseButton(e))
                     {
                         if(sourceTile == null)
                         {
                             //first click
                             sourceTile = chessBoard.getTile(tileId);
                             humanMovedPiece = sourceTile.getPiece();
                             if(humanMovedPiece ==null)
                             {
                                 sourceTile = null;
                             }
                             //System.out.println("Mouse Blicked on "+chessBoard.getTile(tileId));
                         }
                         else
                         {
                             
                             destinationTile = chessBoard.getTile(tileId);
                             final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), tileId);
                             final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                             if(transition.getMoveStatus().isDone())
                             {
                                 chessBoard = transition.getTransitionBoard();
                                 //add the move that was made to the move log
                             }
                             sourceTile = null;
                             destinationTile = null;
                             humanMovedPiece = null;
                         }  
                             SwingUtilities.invokeLater(() -> {
                                 boardPanel.drawBoard(chessBoard);
                         });                        
                     }
                        //second click                     
                 }

                 @Override
                 public void mousePressed(MouseEvent e) {

                 }

                 @Override
                 public void mouseReleased(MouseEvent e) {

                 }

                 @Override
                 public void mouseEntered(MouseEvent e) {

                 }

                 @Override
                 public void mouseExited(MouseEvent e) {

                 }
             });
             validate();
        }
        public void drawTile(final Board board)
        {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegalMoves(chessBoard);
            validate();
            repaint();
        }
        
        private void assignTilePieceIcon(final Board board)
        {
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied())
            {
                
                try {
                    final BufferedImage image =
                          ImageIO.read(new File(defaultPieceImagePath + board.getTile(this.tileId).getPiece().getPieceAlliance().toString()
                          .substring(0,1)+ board.getTile(this.tileId).getPiece().toString()+".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        private void highlightLegalMoves(final Board board)
        {
            if(true)
            {
                for(final Move move: pieceLegalMoves(board))
                {
                    if(move.getDestinationCoordinate() == this.tileId)
                    {
                        try
                        {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("images/misc/green_dot.png")))));
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        private Collection<Move> pieceLegalMoves(final Board board) 
        {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance())
            {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }        
        
        private void assignTileColor() {
            
            if(BoardUtil.EIGHTH_RANK[this.tileId]||
                    BoardUtil.SIXTH_RANK[this.tileId]||
                    BoardUtil.FOURTH_RANK[this.tileId]||
                    BoardUtil.SECOND_RANK[this.tileId]
               )       
            {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            }
            else if(BoardUtil.SEVENTH_RANK[this.tileId]||
                    BoardUtil.FIFTH_RANK[this.tileId]||
                    BoardUtil.THIRD_RANK[this.tileId]||
                    BoardUtil.FIRST_RANK[this.tileId])
            {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }            
        }
    }
    public enum BoardDirection
    {
        NORMAL
        {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles)
            {
                return boardTiles;
            }
            @Override
            BoardDirection opposite()
            {
                return FLIPPED;
            }
        },
        FLIPPED
        {
            @Override
            List<TilePanel>traverse(final List<TilePanel>  boardTiles)
            {
                return Lists.reverse(boardTiles);
            }
            @Override
            BoardDirection opposite()
            {
                return NORMAL;
            }
        };
        abstract List<TilePanel>traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }
    
}
