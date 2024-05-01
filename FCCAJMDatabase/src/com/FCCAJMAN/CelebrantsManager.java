package com.FCCAJMAN;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CelebrantsManager {
    private List<Member> members;

    public CelebrantsManager(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void showCelebrantsWindow() {
        SwingUtilities.invokeLater(() -> {
            CelebrantsWindow celebrantsWindow = new CelebrantsWindow(this);
            celebrantsWindow.refresh();
        });
    }
}
