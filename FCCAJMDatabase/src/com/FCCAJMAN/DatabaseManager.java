package com.FCCAJMAN;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Comparator;

public class DatabaseManager {
    private List<Member> memberList;
    private NonEditableTableModel tableModel;
    private static final String DATA_FILE_NAME = "members.dat";

    public DatabaseManager() {
        memberList = new ArrayList<>();
        tableModel = new NonEditableTableModel();
        initializeTableModel();
        loadDataFromFile();
    }

    private void initializeTableModel() {
        // Initialize the table model columns
        String[] columns = {"First Name", "Middle Name", "Last Name", "Contact Number", "WhatsApp Number",
                "Birthday", "Wedding Anniversary", "Baptized", "CGO", "Age", "Is active?"}; // Added "Is Active?" column
        tableModel.setColumnIdentifiers(columns);
    }

    public void addMember(Member member) {
        memberList.add(member);
        updateTableModel();
        // Save data only when the user is done
    }

    public void deleteMember(String firstName, String lastName) {
        Optional<Member> optionalMember = findMember(firstName, lastName);

        if (optionalMember.isPresent()) {
            Member memberToDelete = optionalMember.get();
            memberList.remove(memberToDelete);
            updateTableModel();
            // Save data only when the user is done
        } else {
            JOptionPane.showMessageDialog(null, "Member not found for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editMember(Member editedMember) {
        Optional<Member> optionalMember = findMember(editedMember.getFirstName(), editedMember.getLastName());

        if (optionalMember.isPresent()) {
            Member existingMember = optionalMember.get();

            SwingUtilities.invokeLater(() -> {
                EditMemberWindow editWindow = new EditMemberWindow(existingMember, e -> {
                    memberList.remove(existingMember);
                    memberList.add(editedMember);
                    updateTableModel();
                    // Save data only when the user is done
                });

                // Show the edit window
                editWindow.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(null, "Member not found for editing.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setActiveStatus(String firstName, String lastName, boolean isActive) {
        Optional<Member> optionalMember = findMember(firstName, lastName);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setActive(isActive);
            updateTableModel();
            // Save data only when the user is done
        } else {
            JOptionPane.showMessageDialog(null, "Member not found for setting active status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Member> getMembers() {
        return memberList.stream()
                .sorted(Comparator.comparing(Member::getFirstName, Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }

    public NonEditableTableModel getTableModel() {
        return tableModel;
    }

    private void loadDataFromFile() {
        File dataFile = new File(DATA_FILE_NAME);

        if (dataFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
                List<Member> loadedMembers = (List<Member>) ois.readObject();
                if (loadedMembers != null && !loadedMembers.isEmpty()) {
                    memberList.addAll(loadedMembers);
                    updateTableModel();
                } else {
                    System.out.println("File is empty or null. Initializing memberList.");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Data file not found: " + DATA_FILE_NAME);
            saveData(); // Create a new file
        }
    }


    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE_NAME))) {
            oos.writeObject(memberList);
            // JOptionPane.showMessageDialog(null, "Data successfully saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }


    public Optional<Member> findMember(String firstName, String lastName) {
        return memberList.stream()
                .filter(member -> member.getFirstName().equalsIgnoreCase(firstName) &&
                        member.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    private void updateTableModel() {
        tableModel.setRowCount(0); // Clear existing rows

        for (Member member : memberList) {
            Object[] rowData = {
                    member.getFirstName(),
                    member.getMiddleName(),
                    member.getLastName(),
                    member.getContactNumber(),
                    member.getWhatsappNumber(),
                    member.getFormattedBirthday(),
                    member.getFormattedAnniversary(),
                    member.getBaptizedStatus(),
                    member.getCgoStatus(),
                    member.calculateAge(),
                    member.getActiveStatus() // New column for "Is Active?"
            };
            tableModel.addRow(rowData);
        }
    }

    public Optional<Member> findMemberByName(String name) {
        String[] names = name.split("\\s+");
        if (names.length == 1) {
            // Search by first name or last name
            return memberList.stream()
                    .filter(member -> member.getFirstName().equalsIgnoreCase(name) || member.getLastName().equalsIgnoreCase(name))
                    .findFirst();
        } else if (names.length >= 2) {
            // Search by first name and last name
            String firstName = names[0];
            String lastName = names[names.length - 1];
            return memberList.stream()
                    .filter(member -> member.getFirstName().equalsIgnoreCase(firstName) && member.getLastName().equalsIgnoreCase(lastName))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }

    public void sortMembersByColumn(String column) {
        switch (column.toLowerCase()) {
            case "first name":
                memberList.sort(Comparator.comparing(Member::getFirstName, String.CASE_INSENSITIVE_ORDER));
                break;
            case "last name":
                memberList.sort(Comparator.comparing(Member::getLastName, String.CASE_INSENSITIVE_ORDER));
                break;
            // Add more cases for additional columns if needed
            default:
                // Default to sorting by first name if the column is not recognized
                memberList.sort(Comparator.comparing(Member::getFirstName, String.CASE_INSENSITIVE_ORDER));
                break;
        }

        // Update the table model after sorting
        updateTableModel();
    }

    public int getActiveMembersCount() {
        return (int) memberList.stream().filter(member -> member.getActiveStatus().equalsIgnoreCase("yes")).count();
    }
}
