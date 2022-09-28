package org.pxf.view;

import org.pxf.model.*;

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
    private final Engine engine;
    private ChessBoardPosition selectedPosition;
    private final ArrayList<ChessBoard> currEpisode;
    private int currBoardIdx;
    public GUI(){
        engine = new Engine();
        selectedPosition = null;

        currEpisode = new ArrayList<>();
        currEpisode.add(engine.getBoard());
        currBoardIdx = 0;

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
        System.out.println(currBoardIdx);
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
                ChessBoardPosition newPosition = parsePosition(e);
                if (selectedPosition == null){
                    selectedPosition = newPosition;
                }else{
                    moveSelectedPosition(newPosition);
                    selectedPosition = null;
                    currBoardIdx+=2;
                }
                repaint();
            }
        };
    }

    private void moveSelectedPosition(ChessBoardPosition newPosition) {
        ChessBoardMove move = new ChessBoardMove(selectedPosition, newPosition);
        if (engine.move(move, Team.BLACK)) {
            currEpisode.add(engine.getBoard());
            engine.minMaxStep(Team.WHITE);
            currEpisode.add(engine.getBoard());
        }
    }

    private ChessBoardPosition parsePosition(MouseEvent e) {
        int row = e.getY() / GRID_CELL_SIZE;
        int col = e.getX() / GRID_CELL_SIZE;
        return new ChessBoardPosition(row, col);
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