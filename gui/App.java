package gui;

import dts.Board;
import dts.DataModel;

import javax.swing.*;
import java.awt.*;


public class App {
    private JFrame frame;
    private BoardPanel boardPanel;
    private OptionPanel optionPanel;

    public App(DataModel dataModel) {
        frame=new JFrame();
        boardPanel=new BoardPanel(dataModel.getActualBoard());
        optionPanel=new OptionPanel();
        boardPanel.addMouseListener(new MouseListener(boardPanel,dataModel));
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(optionPanel, BorderLayout.EAST);
        frame.setSize(640,480);
        frame.setVisible(true);
    }
    public void setBoard(Board board){
        boardPanel.setBoard(board);
    }
}
