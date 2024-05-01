package com.FCCAJMAN;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActiveMembersButton extends JButton {

    private DatabaseManager databaseManager;

    public ActiveMembersButton(DatabaseManager databaseManager) {
        super("Active Members");
        this.databaseManager = databaseManager;

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showActiveMembersCount();
            }
        });
    }

    private void showActiveMembersCount() {
        int activeMembersCount = databaseManager.getActiveMembersCount();
        JOptionPane.showMessageDialog(null, "Number of Active Members: " + activeMembersCount, "Active Members", JOptionPane.INFORMATION_MESSAGE);
    }
}
