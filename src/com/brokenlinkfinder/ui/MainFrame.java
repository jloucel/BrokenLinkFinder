package com.brokenlinkfinder.ui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by loucelj on 2/1/2017.
 */
public class MainFrame extends JFrame {

    private TextPanel textPanel;
    private Toolbar toolbar;


    public MainFrame() {
        super("Broken Link Finder");
        setLayout(new BorderLayout());
        textPanel = new TextPanel();
        toolbar = new Toolbar(textPanel);

        setJMenuBar(createMenuBar());

        add(toolbar, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);

        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(e -> System.exit(0));

        JMenu windowMenu = new JMenu("Window");
        JMenuItem exportResults = new JMenuItem("Export Results...");
        JMenuItem loadPastResult = new JMenuItem("Load Past Results...");
        windowMenu.add(exportResults);

        exportResults.addActionListener(e -> {
            JFileChooser saveResults = new JFileChooser("./");
            int returnVal = saveResults.showSaveDialog(textPanel);
            File file = saveResults.getSelectedFile();
            BufferedWriter resultWriter = null;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    resultWriter = new BufferedWriter(new FileWriter((file.getAbsoluteFile() + ".html")));
                    textPanel.writeResults(resultWriter);
                    JOptionPane.showMessageDialog(textPanel, "Search results have been saved!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    resultWriter.close();
                } catch (IOException ioex) {
                    JOptionPane.showMessageDialog(textPanel, "An error has occured",
                            "Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        windowMenu.add(loadPastResult);
        loadPastResult.addActionListener(e -> {
            new TestSelectionDialog(textPanel);
        });


        menuBar.add(fileMenu);
        menuBar.add(windowMenu);
        return menuBar;
    }
}
