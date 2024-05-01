package com.FCCAJMAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Optional;

public class SearchMemberWindow extends JFrame {
    private JTextField searchTextField;
    private DefaultListModel<String> suggestionListModel;
    private JList<String> suggestionList;
    private List<Member> memberList;

    public SearchMemberWindow(List<Member> memberList) {
        this.memberList = memberList;

        setTitle("Search Member");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        initComponents();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        JLabel searchLabel = new JLabel("Search Member");
        suggestionListModel = new DefaultListModel<>();
        suggestionList = new JList<>(suggestionListModel);
        JScrollPane suggestionScrollPane = new JScrollPane(suggestionList);
        searchTextField = new JTextField(20);

        // Add key listener to update suggestions dynamically
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> updateSuggestions(searchTextField.getText()));
            }
        });

        // Add MouseListener to handle double-click
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openDetailsWindowForSelectedMember();
                }
            }
        });

        add(searchLabel);
        add(searchTextField);
        add(suggestionScrollPane);
    }

    private void updateSuggestions(String input) {
        suggestionListModel.clear();

        for (Member member : memberList) {
            String fullName = member.getFullNameWithMiddleInitial();
            if (fullName.toLowerCase().contains(input.toLowerCase())) {
                suggestionListModel.addElement(fullName);
            }
        }

        suggestionList.setVisible(!suggestionListModel.isEmpty());
    }

    private void openDetailsWindowForSelectedMember() {
        String selectedMemberName = suggestionList.getSelectedValue();
        if (selectedMemberName != null) {
            Optional<Member> optionalMember = findMemberByName(selectedMemberName);

            if (optionalMember.isPresent()) {
                Member selectedMember = optionalMember.get();
                openMemberDetailsWindow(selectedMember);
            }
        }
    }

    private void openMemberDetailsWindow(Member member) {
        SwingUtilities.invokeLater(() -> {
            new MemberDetailsWindow(member);
            dispose();
        });
    }

    private Optional<Member> findMemberByName(String name) {
        return memberList.stream()
                .filter(member -> member.getFullNameWithMiddleInitial().equalsIgnoreCase(name))
                .findFirst();
    }
}
