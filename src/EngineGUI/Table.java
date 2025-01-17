/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineGUI;
import chessengine.*;
import chessengine.AI.MiniMax;
import chessengine.AI.MoveStrategy;
import chessengine.Move.MoveFactory;
import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 *
 * @author User
 */
public class Table extends Observable{
    private Board chessBoard;
    private final JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final BoardPanel boardPanel;
    private final MoveLog moveLog;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private final GameSetup gameSetup;
    
    private Move computerMove;
    
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);    
    private static final  Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static final String defaultPieceImagePath = "images/Crusaders/";
    
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor =  Color.decode("#593E1A");
    
    private static final Table INSTANCE = new Table();
    
    private Table()
    {
        this.gameFrame = new JFrame("TrigramChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableBarMenu = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableBarMenu);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION); 
        this.chessBoard = Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);        
        this.gameFrame.add(this.gameHistoryPanel,BorderLayout.EAST);    
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.add(this.takenPiecesPanel,BorderLayout.WEST);
        this.gameFrame.setVisible(true);
        this.boardDirection = BoardDirection.NORMAL;
    }

    public static Table get()
    {
        return INSTANCE;
    }
    
    public void show()
    {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }
    
    private GameSetup getGameSetup()
    {
        
        return this.gameSetup;
    }
    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }    
    
    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }
    private JMenu createFileMenu()
    {
        final JMenu fileMenu = new JMenu("File");
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
    
    private JMenu createOptionsMenu()
    {
        final JMenu optionsMenu = new JMenu("Options");

        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener(e -> {
            Table.get().getGameSetup().promptUser();
            Table.get().setupUpdate(Table.get().getGameSetup());
        });
        optionsMenu.add(setupGameMenuItem);        
        return optionsMenu;
    }
    
    private void setupUpdate(final GameSetup gameSetup)
    {
        setChanged();
        notifyObservers(gameSetup);
    }
    public Board getGameBoard()
    {
       return this.chessBoard; 
    }

    public void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    private void updateComputerMove(Move computerMove) {
        this.computerMove = computerMove;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    private void moveMadeUpdate(PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);        
    }
    private static class TableGameAIWatcher implements Observer
    {
        @Override
        public void update(Observable o, Object arg) {
            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) &&
                !Table.get().getGameBoard().currentPlayer().isInCheckmate()&&
                !Table.get().getGameBoard().currentPlayer().isInStalemate()) {
                System.out.println(Table.get().getGameBoard().currentPlayer() + " is set to AI, thinking....");
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
                //create ai thread and execute ai work
            }
            if (Table.get().getGameBoard().currentPlayer().isInCheckmate()) {
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                        "Game Over: Player " + Table.get().getGameBoard().currentPlayer() + " is in checkmate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            if (Table.get().getGameBoard().currentPlayer().isInStalemate()) {
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                        "Game Over: Player " + Table.get().getGameBoard().currentPlayer() + " is in stalemate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
    }
    private static class AIThinkTank extends SwingWorker<Move, String> {

        private AIThinkTank() {
        }

        @Override
        protected Move doInBackground() throws Exception{
            final MoveStrategy miniMax = new MiniMax(4);
            final Move bestMove = miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }

        @Override
        public void done() {
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getMoveLog().addMoves(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
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
    enum PlayerType
    {
        HUMAN,
        COMPUTER
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
                                 moveLog.addMoves(move);
                                 //add the move that was made to the move log
                             }
                             sourceTile = null;
                             destinationTile = null;
                             humanMovedPiece = null;
                         }  
                             SwingUtilities.invokeLater(() -> {                                 
                                 gameHistoryPanel.redo(chessBoard, moveLog);
                                 takenPiecesPanel.redo(moveLog);
                                 if(gameSetup.isAIPlayer(chessBoard.currentPlayer()))
                                 {
                                     Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                 }
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
