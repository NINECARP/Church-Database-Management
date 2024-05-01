package com.FCCAJMAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttendanceManager {
    private List<Member> members;
    private JList<String> attendanceList;
    private DefaultListModel<String> listModel;
    private JFrame attendanceFrame;

    public AttendanceManager(List<Member> members) {
        this.members = members;
        this.listModel = new DefaultListModel<>();

        // Initialize the list model with members' full names
        members.stream()
                .map(Member::getFullNameWithMiddleInitial)
                .forEach(listModel::addElement);

        // Sort the list model alphabetically
        sortListModel(listModel);

        // Initialize the attendance list
        attendanceList = new JList<>(listModel);
        attendanceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Create the attendance frame
        attendanceFrame = new JFrame("Mark Attendance");
        attendanceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        attendanceFrame.setSize(400, 300);
        attendanceFrame.setLocationRelativeTo(null);

        // Add a confirm button
        JButton confirmButton = new JButton("Confirm Attendance");
        confirmButton.addActionListener(new ConfirmButtonListener());

        // Add the attendance list and confirm button to the frame
        attendanceFrame.setLayout(new BorderLayout());
        attendanceFrame.add(new JScrollPane(attendanceList), BorderLayout.CENTER);
        attendanceFrame.add(confirmButton, BorderLayout.SOUTH);
    }
    
    public void showAttendanceWindow() {
        // Show the existing instance's window
        attendanceFrame.setVisible(true);
    }
    
    private void sortListModel(DefaultListModel<String> model) {
        List<String> elements = Collections.list(model.elements());
        elements.sort(String::compareTo);
        model.removeAllElements();
        elements.forEach(model::addElement);
    }

    private class ConfirmButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get selected members from the list
            int[] selectedIndices = attendanceList.getSelectedIndices();
            List<Member> selectedMembers = new ArrayList<>();
            for (int index : selectedIndices) {
                selectedMembers.add(members.get(index));
            }

            // Categorize members into adults, kids, and visitors
            List<Member> adults = new ArrayList<>();
            List<Member> kids = new ArrayList<>();
            List<Member> visitors = new ArrayList<>();
            List<Member> combinedCelebrants = new ArrayList<>(); // New list for combined celebrants

            for (Member member : selectedMembers) {
                int age = member.calculateAge();

                if (age >= 18) {
                    adults.add(member);
                } else {
                    kids.add(member);
                }

                // Check if the member is added on the same day
                if (isAddedOnSameDay(member)) {
                    // Ensure visitors are unique from kids and adults
                    if (!isAdult(member) && !isKid(member)) {
                        visitors.add(member);
                    }

                    // Check if the member has the same date for birthday and anniversary
                    if (isBirthdayAndAnniversarySameDate(member)) {
                        combinedCelebrants.add(member);
                    }
                }
            }

            // Display the categorized results along with the total number of people
            displayCategorizedResults(adults, kids, visitors, combinedCelebrants, selectedMembers.size());

            // Close the attendance window
            attendanceFrame.dispose();
        }

        private boolean isBirthdayAndAnniversarySameDate(Member member) {
            LocalDate birthdayDate = LocalDate.ofInstant(member.getBirthday().toInstant(), member.getZoneId());
            LocalDate anniversaryDate = LocalDate.ofInstant(member.getWeddingAnniversary().toInstant(), member.getZoneId());
            return birthdayDate.equals(anniversaryDate);
        }
    }

        private boolean isAddedOnSameDay(Member member) {
            LocalDate currentDate = LocalDate.now();
            LocalDate memberAddedDate = LocalDate.ofInstant(member.getJoinDate().toInstant(), member.getZoneId());
            return currentDate.isEqual(memberAddedDate);
        }

        private boolean isAdult(Member member) {
            return member.calculateAge() >= 18;
        }

        private boolean isKid(Member member) {
            return member.calculateAge() < 18;
        }

	private void displayCategorizedResults(List<Member> adults, List<Member> kids, List<Member> visitors, List<Member> combinedCelebrants, int totalPresent) {
	    // You can customize how to display the categorized results
	    JOptionPane.showMessageDialog(null,
	            "Attendance confirmed!\n\n" +
	                    "Adults (18+): " + adults.size() + "\n" +
	                    "Kids: " + kids.size() + "\n" +
	                    "Visitors: " + visitors.size() + "\n" +
	                    "Combined Celebrants: " + combinedCelebrants.size() + "\n" +
	                    "Total Present: " + totalPresent,
	            "Attendance Result",
	            JOptionPane.INFORMATION_MESSAGE);
	}



    private void displayCategorizedResults(List<Member> adults, List<Member> kids, List<Member> visitors) {
        // You can customize how to display the categorized results
        JOptionPane.showMessageDialog(null,
                "Attendance confirmed!\n\n" +
                        "Adults (18+): " + adults.size() + "\n" +
                        "Kids: " + kids.size() + "\n" +
                        "Visitors: " + visitors.size(),
                "Attendance Result",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
