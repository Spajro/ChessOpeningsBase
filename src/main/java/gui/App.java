package gui;

import data.model.DataModel;
import gui.board.BoardMouseListener;
import gui.board.BoardPanel;
import gui.option.OptionPanel;

import javax.swing.*;
import java.awt.*;


public class App {
    private final JFrame frame;
    private final BoardPanel boardPanel;
    private final OptionPanel optionPanel;
    private final Controller controller;
    private final JMenuBar menuBar;

    public App(DataModel dataModel) {
        frame = new JFrame();
        menuBar = new JMenuBar();
        createFileMenu(dataModel);
        createFeatureMenu();
        createEditionMenu();
        boardPanel = new BoardPanel(dataModel.getActualNode());
        controller = new Controller(dataModel, boardPanel);
        optionPanel = new OptionPanel(controller);
        controller.setOptionPanel(optionPanel);
        setUpBoardPanel();
        setUpFrame();
    }

    private void setUpFrame() {
        frame.setJMenuBar(menuBar);
        frame.setLayout(new GridLayout());
        frame.add(boardPanel);
        frame.add(optionPanel);
        frame.setSize(640, 480);
        frame.setVisible(true);
    }

    private void createFileMenu(DataModel dataModel) {
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveMenuItem = new JMenuItem("Save to file");
        saveMenuItem.addActionListener(e -> controller.saveDataToFile(getFilename()));
        fileMenu.add(saveMenuItem);

        JMenuItem loadMenuItem = new JMenuItem("Load from file");
        loadMenuItem.addActionListener(e -> controller.loadDataFromFile(getFilename()));
        fileMenu.add(loadMenuItem);

        JMenuItem loadFENMenuItem = new JMenuItem("Load from FEN");
        loadFENMenuItem.addActionListener(e -> controller.loadChessBoardFromFEN(getFEN()));
        fileMenu.add(loadFENMenuItem);

        JMenuItem saveFENMenuItem = new JMenuItem("Save to FEN");
        saveFENMenuItem.addActionListener(e -> controller.saveChessBoardToFEN());
        fileMenu.add(saveFENMenuItem);

        menuBar.add(fileMenu);
    }

    private void createFeatureMenu() {
        JMenu featureMenu = new JMenu("Features");

        JMenuItem coverageMenuItem = new JMenuItem("Coverage");
        coverageMenuItem.addActionListener(e -> boardPanel.swapDoPaintCoverage());
        featureMenu.add(coverageMenuItem);

        JMenuItem legalMovesMenuItem = new JMenuItem("Legal moves");
        legalMovesMenuItem.addActionListener(e -> boardPanel.swapDoPaintLegalMoves());
        featureMenu.add(legalMovesMenuItem);

        JMenuItem weakPointsMenuItem = new JMenuItem("Weak points");
        weakPointsMenuItem.addActionListener(e -> boardPanel.swapDoPaintWeakPoints());
        featureMenu.add(weakPointsMenuItem);

        menuBar.add(featureMenu);
    }

    private void createEditionMenu() {
        JMenu editionMenu = new JMenu("Edit");

        JMenuItem addMovesItem = new JMenuItem("Add moves");
        addMovesItem.addActionListener(e -> controller.makeMoves(getMoves()));
        editionMenu.add(addMovesItem);

        menuBar.add(editionMenu);
    }

    private void setUpBoardPanel() {
        boardPanel.addMouseListener(new BoardMouseListener(controller));
        boardPanel.setFocusable(true);
        boardPanel.setSize(480, 480);
    }

    private String getFilename() {
        return JOptionPane.showInputDialog(frame, "What is name of file?");
    }

    private String getFEN() {
        return JOptionPane.showInputDialog(frame, "Write FEN you want to import");
    }

    private String getMoves() {
        return JOptionPane.showInputDialog(frame, "Write moves you want to add");
    }
}
