package gui;

import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {
    private final JTree jTree;
    private final JScrollPane jScrollPane;
    private final JTextArea jTextArea;
    public OptionPanel(Controller controller) {
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        jTree=controller.createTreeWithDataModel();
        jTree.addMouseListener(new TreeMouseListener(jTree,controller));

        jScrollPane=new JScrollPane(jTree);
        jScrollPane.setPreferredSize(new Dimension(160,480));

        jTextArea=new JTextArea();
        jTextArea.setBackground(Color.lightGray);
        jTextArea.getDocument().addDocumentListener(new TextAreaListener(controller));

        add(jScrollPane);
        add(jTextArea);
    }

    public void setText(String text){
        jTextArea.setText(text);
    }
}
