package com.FCCAJMAN;

import javax.swing.*;
import java.awt.*;

public class MemberDetailsWindow extends JFrame {
    public MemberDetailsWindow(Member member) {
        setTitle("Member Details");
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents(member);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents(Member member) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("First Name:"));
        panel.add(new JLabel(member.getFirstName()));

        panel.add(new JLabel("Middle Name:"));
        panel.add(new JLabel(member.getMiddleName()));

        panel.add(new JLabel("Last Name:"));
        panel.add(new JLabel(member.getLastName()));

        panel.add(new JLabel("Contact Number:"));
        panel.add(new JLabel(member.getContactNumber()));

        panel.add(new JLabel("WhatsApp Number:"));
        panel.add(new JLabel(member.getWhatsappNumber()));

        panel.add(new JLabel("Birthday:"));
        panel.add(new JLabel(member.getFormattedBirthday()));

        panel.add(new JLabel("Wedding Anniversary:"));
        panel.add(new JLabel(member.getFormattedAnniversary()));

        panel.add(new JLabel("Baptized:"));
        panel.add(new JLabel(member.getBaptizedStatus()));

        panel.add(new JLabel("CGO Status:"));
        panel.add(new JLabel(member.getCgoStatus()));

        panel.add(new JLabel("Active Status:"));
        panel.add(new JLabel(member.getActiveStatus()));

        getContentPane().add(panel, BorderLayout.CENTER);
    }
}
