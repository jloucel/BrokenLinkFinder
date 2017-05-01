package com.brokenlinkfinder.ui;

import com.brokenlinkfinder.utils.DBUtilities;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


/**
 * Created by loucelj on 4/23/2017.
 */
public class TestSelectionDialog extends JFrame{
    private JComboBox urlSelector;
    private JComboBox testDateSelector;
    private JButton okButton;
    private JButton cancelButton;
    private List<String> urlList;
    private List<String> runDatesList;
    private String url;
    private String date;

    public TestSelectionDialog(TextPanel textArea){
        setTitle("Select Test Results");
        setSize(350,250);
        setLayout(new MigLayout("","center",""));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        okButton = new JButton("Get Results");
        cancelButton = new JButton("Cancel");
        urlSelector = new JComboBox();
        Dimension urlDim = urlSelector.getPreferredSize();
        urlSelector.setPreferredSize(new Dimension(250,urlDim.height));
        testDateSelector = new JComboBox();
        Dimension dateDim = testDateSelector.getPreferredSize();
        testDateSelector.setPreferredSize(new Dimension(250,dateDim.height));

        add(urlSelector, "wrap");
        add(testDateSelector,"wrap");
        add(okButton, "split 2, center");
        add(cancelButton);

        // Init combos, date will reload in response to the action event with updated list
        urlList = new DBUtilities().getUrlList();
        urlSelector.setModel(new DefaultComboBoxModel(urlList.toArray()));
        url = (String) urlSelector.getSelectedItem();
        runDatesList = new DBUtilities().getRunDates(url);
        testDateSelector.setModel(new DefaultComboBoxModel(runDatesList.toArray()));

        urlSelector.addActionListener(e -> {
            url = (String) urlSelector.getSelectedItem();
            testDateSelector.removeAllItems();
            runDatesList = new DBUtilities().getRunDates(url);
            testDateSelector.setModel(new DefaultComboBoxModel(runDatesList.toArray()));
        });

        okButton.addActionListener((ActionEvent e) -> {
            textArea.clear();
            date = (String) testDateSelector.getSelectedItem();
            String result = new DBUtilities().getResults(url,date);
            textArea.appendText(result);
            textArea.resetCursor();
            this.dispose();
        });

        cancelButton.addActionListener(e -> {
            this.dispose();
        });

        setVisible(true);
    }

}


