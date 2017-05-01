package com.brokenlinkfinder.ui;

import com.brokenlinkfinder.logic.LinkTest;
import com.brokenlinkfinder.utils.DBUtilities;
import com.brokenlinkfinder.utils.isValidURL;
import net.miginfocom.swing.MigLayout;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * Created by loucelj on 2/1/2017.
 */
public class Toolbar extends JPanel {

    private JButton checkLinkButton;
    private JTextField urlToTest;

    public Toolbar (TextPanel textArea){
        checkLinkButton = new JButton("Check Links");
        checkLinkButton.setToolTipText("Click to test URL");
        urlToTest = new JTextField();

        urlToTest.setToolTipText("Enter URL ex. http://www.test.com");
        setLayout(new MigLayout("","grow",""));
        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, getBackground()));

        add(checkLinkButton, "dock west, gap rel 2");
        add(urlToTest, "growx, gap rel 2");

        urlToTest.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkLinkButton.doClick();
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        checkLinkButton.addActionListener((ActionEvent e) -> {
            String testURL = urlToTest.getText();
            if (!isValidURL.check(testURL)) {
                textArea.appendText("Provided URL: " + testURL + " is invalid.<br>" +
                                    "URL format should be HTTP://hostname.resource<br>" +
                                    "i.e. http://example.com.");
            } else {
                textArea.clear();
                textArea.appendText("Searching for links on " + testURL + "<br>" +
                                    "Please wait...");
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    private int errCount = 0;
                    private int validCount = 0;
                    private int warnCount = 0;
                    private int exCount = 0;

                    @Override
                    protected Void doInBackground() throws Exception {
                        LinkTest test = new LinkTest();
                        List<String> siteLinks = test.findAllLinks((urlToTest.getText()));
                        textArea.appendText("<br>Found " + siteLinks.size() + " links to test...<br><br>");
                        for (String link : siteLinks) {
                            int index = siteLinks.indexOf(link) + 1;
                            String result = test.isLinkBroken(link);
                            if (result.contains("ERROR")) {
                                errCount++;
                            } else if (result.contains("Warning")) {
                                warnCount++;
                            } else if (result.contains("Exception occurred")){
                                exCount++;
                            } else {
                                validCount++;
                            }
                            publish(index + ": " + test.isLinkBroken(link) + "<br>");
                        }
                        return null;
                    }

                    @Override
                    protected void process(List<String> chunks) {
                        for (String chunk : chunks) {
                            textArea.appendText(chunk + "\n");
                            textArea.update(textArea.getGraphics());
                        }
                    }

                    @Override
                    protected void done() {
                        textArea.appendText("<br>Test Complete!");
                        textArea.appendText("<br>Found <font color=\"green\">" + validCount + "</font> valid URLs.");
                        textArea.appendText("<br>Found <font color=\"red\"> " + errCount + "</font> broken URLs.");
                        textArea.appendText("<br>Found <font color=\"olive\"> " + warnCount + "</font> warning URLs.");
                        textArea.appendText("<br>Found <font color=\"maroon\"> " + exCount + "</font> exceptions.");

                        DBUtilities resultWriter = new DBUtilities();
                        String result = textArea.getPanelContents();
                        resultWriter.saveTestResults(testURL,result);
                    }
                };
                worker.execute();
            }
        });
    }
}
