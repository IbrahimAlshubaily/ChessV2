package org.pxf.view;

import org.pxf.model.ChessBoard;
import org.pxf.model.ChessBoardPosition;
import org.pxf.model.ChessPiece;
import org.pxf.model.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

class GUI extends JPanel{
    private static final int BOARD_SIZE = 8;
    private static final int GRID_CELL_SIZE = 50;
    private final Engine engine = new Engine();
    private ChessPiece selected = null;
    private ArrayList<ChessBoard> currEpisode;
    private int currBoardIdx = 0;
    public GUI(){
        engine.initPieces();
        //addMouseListener(getMouseListener());
        currEpisode = engine.rollOut( 3, 32);
        System.out.println("X");
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawPieces(g);
    }

    private void drawGrid(Graphics g) {
        int start;
        int end = GRID_CELL_SIZE * BOARD_SIZE;
        for (int row = 0; row <= BOARD_SIZE; row++) {
            start = row * GRID_CELL_SIZE;
            g.drawLine(start, 0, start, end);//vertical line
            g.drawLine(0, start, end, start);//horizontal line
        }
    }

    private void drawPieces(Graphics g) {
        if (currEpisode == null)
            return;

        int pad = GRID_CELL_SIZE / 3;
        currEpisode.get(currBoardIdx).getPieces().forEach( (position, piece) -> g.drawString(piece.toString(),
                                                                position.col * GRID_CELL_SIZE + pad,
                                                                position.row * GRID_CELL_SIZE + pad));
    }
    private MouseListener getMouseListener() {
        return new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / GRID_CELL_SIZE;
                int col = e.getX() / GRID_CELL_SIZE;
                ChessBoardPosition position = new ChessBoardPosition(row, col);
                if (selected == null){
                    selected = engine.getChessPiece(position);
                    System.out.println("Selected : "+ selected);
                }else{
                    engine.move(selected, position);
                    selected = null;
                    repaint();
                }
            }
        };
    }
    private ActionListener getButtonListener(){
        return e -> {
            if (e.getActionCommand().equalsIgnoreCase("next") && currBoardIdx < currEpisode.size() - 1){
                this.currBoardIdx++;
            }else if (e.getActionCommand().equalsIgnoreCase("prev") && currBoardIdx > 0){
                this.currBoardIdx--;
            }
            repaint();
        };
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int frameSize = GRID_CELL_SIZE*(BOARD_SIZE+1);
        frame.setSize(frameSize, frameSize);

        GUI mainPanel = new GUI();

        JButton next = new JButton("Next");
        JButton prev = new JButton("Prev");
        ActionListener buttonListener = mainPanel.getButtonListener();
        next.addActionListener(buttonListener);
        prev.addActionListener(buttonListener);
        mainPanel.add(prev);
        mainPanel.add(next);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(GUI::createAndShowGui);
    }
}