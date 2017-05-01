package com.brokenlinkfinder.ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * Created by loucelj on 2/1/2017.
 */
public class TextPanel extends JPanel {
    private JTextPane textPane;
    private JScrollPane scrollBar;
    private StringBuilder builder;

    public TextPanel(){
        builder = new StringBuilder();
        textPane = new JTextPane();
        textPane.setContentType("text/html");
        scrollBar = new JScrollPane(textPane);
        setLayout(new MigLayout("insets 2 5 4 4"));
        add(scrollBar, "dock center, grow");

    }

    public String getPanelContents(){
        return textPane.getText();
    }

    public void writeResults(Writer w){
        try {
            w.write(textPane.getText());
        } catch (IOException e){}
    }

    public void clear(){
        textPane.setText(null);
        builder.setLength(0);
    }

    public void resetCursor(){
        textPane.setCaretPosition(0);
    }

    public void appendText(String text){
        builder.append(text);
        textPane.setText("<html>" + builder.toString()+ "</html>");
        textPane.repaint();
    }
}
