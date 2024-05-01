package com.FCCAJMAN;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.swing.ImageIcon;

public class GUIManager {
    private JFrame frame;
    private JTable table;
    private DatabaseManager databaseManager;

    public GUIManager() {
        frame = new JFrame("Filipino Chrisitan Church Ajman Member Database");
        frame.setTitle("FCC Ajman Member Database Manager");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       

        // Initialize other UI components
        JButton addButton = new JButton("Add Member");
        JButton editButton = new JButton("Edit Member");
        JButton sortByFirstNameButton = new JButton("Sort by First Name");
        JButton sortByLastNameButton = new JButton("Sort by Last Name");
        JButton searchButton = new JButton("Search Member");
        JButton deleteButton = new JButton("Delete Member");
        
        deleteButton.addActionListener(e -> deleteMemberButtonAction());


        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        // ... (existing code)
        frame.add(deleteButton);
        
        searchButton.addActionListener(e -> new SearchMemberWindow(databaseManager.getMembers()));
        frame.add(searchButton);

        sortByFirstNameButton.addActionListener(e -> {
            databaseManager.sortMembersByColumn("first name");
            refreshTable();
        });

        sortByLastNameButton.addActionListener(e -> {
            databaseManager.sortMembersByColumn("last name");
            refreshTable();
        });
        
        JButton attendanceButton = new JButton("Mark Attendance");
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the "Mark Attendance" button click event
                AttendanceManager attendanceManager = new AttendanceManager(databaseManager.getMembers());
                attendanceManager.showAttendanceWindow();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the add button click event
                // You can open a new window for adding members

                // Example: Open a dialog to gather member details
                String firstName = getInput("Enter First Name:");
                if (firstName == null) {
                    return; // User clicked cancel, cancel the operation
                }

                String middleName = getInput("Enter Middle Name (Optional):");
                String lastName = getInput("Enter Last Name:");
                if (lastName == null) {
                    return; // User clicked cancel, cancel the operation
                }

                String contactNumber = getInput("Enter Contact Number (Optional):");
                String whatsappNumber = getInput("Enter WhatsApp Number (Optional):");
                String birthdayString = getInput("Enter Birthday (DD-MMM-YYYY) (Optional):");
                if (birthdayString == null) {
                    return; // User clicked cancel, cancel the operation
                }

                // Parse date strings into Date objects
                Date birthday = parseDate(birthdayString);

                String anniversaryString = getInput("Enter Anniversary (DD-MMM-YYYY) (Optional):");
                if (anniversaryString == null) {
                    return; // User clicked cancel, cancel the operation
                }

                // Parse date strings into Date objects
                Date anniversary = parseDate(anniversaryString);

                boolean baptized = gatherBaptizedStatus();
                boolean cgo = gatherCGOStatus();

                // Check if required fields are provided
                if (lastName != null && !lastName.isEmpty()) {
                	Member newMember = new Member(firstName, middleName, lastName, contactNumber, whatsappNumber, birthday, anniversary, baptized, cgo, true);
                    databaseManager.addMember(newMember);
                } else {
                    JOptionPane.showMessageDialog(frame, "Last Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the edit button click event
                // You can open a new window for editing members

                // Example: Open a dialog to get member details for editing
                Member memberToEdit = gatherMemberDetailsForEdit();
                if (memberToEdit != null) {
                    databaseManager.editMember(memberToEdit);

                    // You may want to refresh the table or UI after editing
                    refreshTable();
                }
            }
        });


        // ... (Rest of the buttons and their action listeners)

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(sortByFirstNameButton);
        buttonPanel.add(sortByLastNameButton);
        buttonPanel.add(searchButton);

        frame.add(buttonPanel, BorderLayout.NORTH);

        // Create and display the table of members
        databaseManager = new DatabaseManager();
        table = new JTable(databaseManager.getTableModel());
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        frame.setSize(800, 600);
        frame.setVisible(true);

        frame.setVisible(true);
    }
    
    private void start() {
        JButton celebrantsButton = new JButton("Show Celebrants of the Month");
        JButton saveButton = new JButton("Save Data");

        celebrantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the "Show Celebrants of the Month" button click event
                CelebrantsManager celebrantsManager = new CelebrantsManager(databaseManager.getMembers());
                celebrantsManager.showCelebrantsWindow();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the save button click event
                // You can manually trigger the save data process
                databaseManager.saveData();
                JOptionPane.showMessageDialog(frame, "Data successfully saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        ActiveMembersButton activeMembersButton = new ActiveMembersButton(databaseManager);

        // Create a panel for the south buttons and set its layout to GridLayout
        JPanel southButtonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        southButtonPanel.add(activeMembersButton);
        southButtonPanel.add(celebrantsButton);
        southButtonPanel.add(saveButton);

        frame.add(southButtonPanel, BorderLayout.SOUTH);

        // Add any additional UI components or functionality as needed

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
    }


    private void deleteMemberButtonAction() {
        String firstName = getInput("Enter First Name to delete:");
        if (firstName == null) {
            return; // User clicked cancel, cancel the operation
        }

        String lastName = getInput("Enter Last Name to delete:");
        if (lastName == null) {
            return; // User clicked cancel, cancel the operation
        }

        databaseManager.deleteMember(firstName, lastName);
    }

    private Date parseDate(String dateString) {
        try {
            if (dateString == null || dateString.trim().isEmpty()) {
                return null; // Return null for empty date string
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Handle the parsing exception appropriately
            e.printStackTrace(); // Print the stack trace for debugging
            return null;
        }
    }
    
    private boolean gatherBaptizedStatus() {
        // Implement logic to gather baptized status (e.g., show a dialog with options)
        int choice = JOptionPane.showConfirmDialog(frame, "Is the member baptized?", "Baptism Status", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    private boolean gatherCGOStatus() {
        // Implement logic to gather CGO status (e.g., show a dialog with options)
        int choice = JOptionPane.showConfirmDialog(frame, "Is the member part of CGO?", "CGO Status", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    private String getInput(String message) {
        return JOptionPane.showInputDialog(frame, message);
    }

    // Helper method to gather member details for editing
    private Member gatherMemberDetailsForEdit() {
        String firstName = getInput("Enter First Name to edit:");
        if (firstName == null) {
            return null; // User clicked cancel, cancel the operation
        }

        String lastName = getInput("Enter Last Name to edit:");
        if (lastName == null) {
            return null; // User clicked cancel, cancel the operation
        }

        Optional<Member> optionalMember = databaseManager.findMember(firstName.trim(), lastName.trim());

        if (optionalMember.isPresent()) {
            // Member found, proceed with editing
            return optionalMember.get();
        } else {
            // Member not found, display an error message
            JOptionPane.showMessageDialog(frame, "Member not found for editing.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


    // Helper method to refresh the table or UI after editing
    private void refreshTable() {
        table.setModel(databaseManager.getTableModel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUIManager().start();
        });
    }
}
