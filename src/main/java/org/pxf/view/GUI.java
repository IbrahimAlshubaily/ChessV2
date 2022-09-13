package org.pxf.view;

import org.pxf.model.ChessBoardPosition;
import org.pxf.model.ChessPiece;
import org.pxf.model.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;

class GUI extends JPanel{
    private static final int BOARD_SIZE = 8;
    private static final int GRID_CELL_SIZE = 50;
    private final Engine engine = new Engine();
    private ChessPiece selected = null;
    public GUI(){
        engine.initPieces();
        addMouseListener(getMouseListener());
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
        int pad = GRID_CELL_SIZE / 3;
        for (Map.Entry<ChessBoardPosition, ChessPiece> entry : engine.getChessPieces().entrySet()){
            ChessPiece piece = entry.getValue();
            ChessBoardPosition position = entry.getKey();
            g.drawString(piece.toString(), position.col * GRID_CELL_SIZE + pad, position.row * GRID_CELL_SIZE + pad);
        }
    }
    private MouseListener getMouseListener() {
        return new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / GRID_CELL_SIZE;
                int col = e.getX() / GRID_CELL_SIZE;
                if (selected == null){
                    selected = select(row, col);
                    System.out.println("Selected : "+ selected);
                }else{
                    move(new ChessBoardPosition(row, col));
                    selected = null;
                    repaint();
                }
            }
        };
    }

    private ChessPiece select(int row, int col) {
        return engine.getChessPiece(new ChessBoardPosition(row, col));
    }
    private void move(ChessBoardPosition position ) {
        engine.move(selected, position);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int frameSize = GRID_CELL_SIZE*(BOARD_SIZE+1);
        frame.setSize(frameSize, frameSize);

        JPanel mainPanel = new GUI();
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);

    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(GUI::createAndShowGui);
    }
}