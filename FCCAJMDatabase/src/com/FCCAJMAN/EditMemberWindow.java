package com.FCCAJMAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditMemberWindow extends JDialog {
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField contactNumberField;
    private JTextField whatsappNumberField;
    private JTextField birthdayField;
    private JTextField anniversaryField;
    private JCheckBox baptizedCheckBox;
    private JCheckBox cgoCheckBox;
    private JCheckBox activeCheckBox;

    private Member originalMember;
    private ActionListener saveButtonListener;

    private static final String DEFAULT_DATE_STRING = "N/A";

    public EditMemberWindow(Member originalMember, ActionListener saveButtonListener) {
        this.originalMember = originalMember;
        this.saveButtonListener = saveButtonListener;

        setTitle("Edit Member");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setModal(true);

        initComponents();
        populateFields(originalMember);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        firstNameField = new JTextField();
        middleNameField = new JTextField();
        lastNameField = new JTextField();
        contactNumberField = new JTextField();
        whatsappNumberField = new JTextField();
        birthdayField = new JTextField();
        anniversaryField = new JTextField();
        baptizedCheckBox = new JCheckBox("Baptized");
        cgoCheckBox = new JCheckBox("CGO");
        activeCheckBox = new JCheckBox("Is Active?");

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::saveButtonClicked);

        setLayout(new GridLayout(0, 2));

        add(new JLabel("First Name:"));
        add(firstNameField);

        add(new JLabel("Middle Name:"));
        add(middleNameField);

        add(new JLabel("Last Name:"));
        add(lastNameField);

        add(new JLabel("Contact Number:"));
        add(contactNumberField);

        add(new JLabel("WhatsApp Number:"));
        add(whatsappNumberField);

        add(new JLabel("Birthday:"));
        add(birthdayField);

        add(new JLabel("Anniversary:"));
        add(anniversaryField);

        add(new JLabel("Baptized:"));
        add(baptizedCheckBox);

        add(new JLabel("CGO:"));
        add(cgoCheckBox);

        // Add the new checkbox for "Is Active?"
        add(new JLabel("Is Active?:"));
        add(activeCheckBox);

        add(new JLabel(""));
        add(saveButton);
    }

    private void populateFields(Member member) {
        firstNameField.setText(member.getFirstName());
        middleNameField.setText(member.getMiddleName());
        lastNameField.setText(member.getLastName());
        contactNumberField.setText(member.getContactNumber());
        whatsappNumberField.setText(member.getWhatsappNumber());
        birthdayField.setText(member.getFormattedBirthday());
        anniversaryField.setText(member.getFormattedAnniversary());
        baptizedCheckBox.setSelected(member.getBaptizedStatus().equals("yes"));
        cgoCheckBox.setSelected(member.getCgoStatus().equals("done"));
        activeCheckBox.setSelected(member.getActiveStatus().equals("yes"));
    }

    private void saveButtonClicked(ActionEvent e) {
        Member member = originalMember;

        // Update the member details
        member.setFirstName(firstNameField.getText());
        member.setMiddleName(middleNameField.getText());
        member.setLastName(lastNameField.getText());
        member.setContactNumber(contactNumberField.getText());
        member.setWhatsappNumber(whatsappNumberField.getText());
        member.setBaptized(baptizedCheckBox.isSelected());
        member.setCgo(cgoCheckBox.isSelected());
        member.setActive(activeCheckBox.isSelected());

        // Validate and set the formatted birthday
        String birthdayString = birthdayField.getText();
        if (!birthdayString.trim().isEmpty()) {
            try {
                member.setBirthday(DateUtils.parseDate(birthdayString));
            } catch (ParseException ex) {
                // Handle the parsing exception appropriately
                ex.printStackTrace();
                return;
            }
        } else {
            // Set the birthday to null if it is empty
            member.setBirthday(null);
        }

        // Validate and set the formatted anniversary
        String anniversaryString = anniversaryField.getText();
        if (!anniversaryString.trim().isEmpty()) {
            try {
                member.setWeddingAnniversary(DateUtils.parseDate(anniversaryString));
            } catch (ParseException ex) {
                // Handle the parsing exception appropriately
                ex.printStackTrace();
                return;
            }
        } else {
            // Set the anniversary to null if it is empty
            member.setWeddingAnniversary(null);
        }

        // Notify the listener that the save button is clicked
        saveButtonListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));

        // Close the EditMemberWindow after saving
        dispose();
    }

    private Date parseDate(String dateString) throws ParseException {
        if (dateString == null || dateString.trim().isEmpty() || dateString.equals(DEFAULT_DATE_STRING)) {
            // Return null if the date string is empty or "N/A"
            return null;
        }

        try {
            // Try parsing with commas first
            return DateUtils.parseDate(dateString);
        } catch (ParseException e) {
            // If parsing with commas fails, try parsing without commas
            return parseDateWithoutCommas(dateString);
        }
    }

    private Date parseDateWithoutCommas(String dateString) throws ParseException {
        // Remove commas from the date string
        dateString = dateString.replace(",", "");

        // Check for empty or "N/A" date string
        if (dateString.trim().isEmpty() || dateString.equalsIgnoreCase(DEFAULT_DATE_STRING)) {
            return null;
        }

        SimpleDateFormat dateFormatWithoutCommas = new SimpleDateFormat("dd MMM yyyy");
        return dateFormatWithoutCommas.parse(dateString);
    }

}
