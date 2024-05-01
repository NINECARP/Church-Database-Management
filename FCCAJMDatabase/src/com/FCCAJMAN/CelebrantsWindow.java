package com.FCCAJMAN;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CelebrantsWindow extends JFrame {
    private JPanel birthdayPanel;
    private JPanel anniversaryPanel;
    private JPanel combinedPanel;
    private CelebrantsManager celebrantsManager;

    public CelebrantsWindow(CelebrantsManager celebrantsManager) {
        this.celebrantsManager = celebrantsManager;
        setTitle("Celebrants of the Month");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 3));

        initComponents();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void refresh() {
        getContentPane().removeAll();
        initComponents();
        revalidate();
        repaint();
    }

    private void initComponents() {
        birthdayPanel = new JPanel();
        anniversaryPanel = new JPanel();
        combinedPanel = new JPanel();

        // Add category labels as titles on each panel
        birthdayPanel.setBorder(BorderFactory.createTitledBorder("Birthday Celebrants"));
        anniversaryPanel.setBorder(BorderFactory.createTitledBorder("Anniversary Celebrants"));
        combinedPanel.setBorder(BorderFactory.createTitledBorder("Combined Celebrants"));

        categorizeCelebrants();

        add(new JScrollPane(birthdayPanel));
        add(new JScrollPane(anniversaryPanel));
        add(new JScrollPane(combinedPanel));
    }


    private void categorizeCelebrants() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");

        for (Member member : celebrantsManager.getMembers()) {
            String fullName = member.getFullNameWithMiddleInitial();

            if (isBirthdayCelebrant(member) && isAnniversaryCelebrant(member)) {
                LocalDate birthDate = member.getBirthday().toInstant().atZone(member.getZoneId()).toLocalDate();
                LocalDate anniversaryDate = member.getWeddingAnniversary().toInstant().atZone(member.getZoneId()).toLocalDate();

                // Check if the birthday and anniversary are in the same month
                if (birthDate.getMonth() == anniversaryDate.getMonth()) {
                    String combinedInfo = String.format("%s, Birthday - %s, Anniversary - %s", fullName,
                            birthDate.format(formatter), anniversaryDate.format(formatter));
                    combinedPanel.add(new JLabel(combinedInfo));
                }
            } else {
                // If not a combined celebrant, check for individual categories
                if (isBirthdayCelebrant(member)) {
                    LocalDate birthDate = member.getBirthday().toInstant().atZone(member.getZoneId()).toLocalDate();
                    String birthdayInfo = String.format("%s, Birthday - %s", fullName, getFormattedDate(birthDate));
                    birthdayPanel.add(new JLabel(birthdayInfo));
                }

                if (isAnniversaryCelebrant(member)) {
                    LocalDate anniversaryDate = member.getWeddingAnniversary().toInstant().atZone(member.getZoneId()).toLocalDate();
                    String anniversaryInfo = String.format("%s, Anniversary - %s", fullName, getFormattedDate(anniversaryDate));
                    anniversaryPanel.add(new JLabel(anniversaryInfo));
                }
            }
        }
    }

    private String getFormattedDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd MMM"));
    }

    private boolean isBirthdayCelebrant(Member member) {
        LocalDate currentDate = LocalDate.now();
        Date birthday = member.getBirthday();

        if (birthday != null) {
            LocalDate birthdayDate = LocalDate.ofInstant(birthday.toInstant(), member.getZoneId());

            // Check if it's a leap year and the birthday is on February 29th
            if (currentDate.isLeapYear() && birthdayDate.getMonth() == Month.FEBRUARY && birthdayDate.getDayOfMonth() == 29) {
                return true;
            }

            // Check if the birthday is in the current month
            return birthdayDate.getMonth() == currentDate.getMonth();
        }
        return false;
    }

    private boolean isAnniversaryCelebrant(Member member) {
        LocalDate currentDate = LocalDate.now();
        Date anniversary = member.getWeddingAnniversary();

        if (anniversary != null) {
            LocalDate anniversaryDate = LocalDate.ofInstant(anniversary.toInstant(), member.getZoneId());

            // Check if it's a leap year and the anniversary is on February 29th
            if (currentDate.isLeapYear() && anniversaryDate.getMonth() == Month.FEBRUARY && anniversaryDate.getDayOfMonth() == 29) {
                return true;
            }

            // Check if the anniversary is in the current month
            return anniversaryDate.getMonth() == currentDate.getMonth();
        }
        return false;
    }
}
