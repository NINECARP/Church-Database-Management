package com.FCCAJMAN;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String middleName;
    private String lastName;
    private String contactNumber;
    private String whatsappNumber;
    private Date birthday;
    private Date weddingAnniversary;
    private boolean baptized;
    private boolean cgo;
    private boolean isActive; // New field for "Is Active?"
    private static final String DEFAULT_DATE = "N/A";
    private String formattedBirthday;
    private String formattedAnniversary;

    public Member(String firstName, String middleName, String lastName,
            String contactNumber, String whatsappNumber,
            Date birthday, Date weddingAnniversary,
            boolean baptized, boolean cgo, boolean isActive) {
	  this.firstName = firstName;
	  this.middleName = middleName;
	  this.lastName = lastName;
	  this.contactNumber = contactNumber;
	  this.whatsappNumber = whatsappNumber;
	  this.birthday = birthday;
	  this.weddingAnniversary = weddingAnniversary;
	  this.baptized = baptized;
	  this.cgo = cgo;
	  this.isActive = isActive;
	
	  // Initialize formatted dates
	  setFormattedBirthday(birthday);
	  setFormattedAnniversary(weddingAnniversary != null ? weddingAnniversary : birthday); // Use birthday if anniversary is null
	}

    // Getters and Setters
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Date getWeddingAnniversary() {
        return weddingAnniversary;
    }

    public String getBaptizedStatus() {
        return baptized ? "yes" : "no";
    }

    public String getCgoStatus() {
        return cgo ? "done" : "not yet";
    }

    public String getActiveStatus() {
        return isActive ? "yes" : "no";
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getFormattedBirthday() {
        return formattedBirthday;
    }

    public String getFormattedAnniversary() {
        return formattedAnniversary;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public void setBaptized(boolean baptized) {
        this.baptized = baptized;
    }

    public void setCgo(boolean cgo) {
        this.cgo = cgo;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
        setFormattedBirthday(birthday);
    }

    public void setWeddingAnniversary(Date weddingAnniversary) {
        this.weddingAnniversary = weddingAnniversary;
        setFormattedAnniversary(weddingAnniversary);
    }
    
    public boolean hasBirthday() {
        return birthday != null;
    }

    public boolean hasAnniversary() {
        return weddingAnniversary != null;
    }


    // Additional Methods

 // Inside the Member class
    public int calculateAge() {
        if (birthday == null) {
            return 0;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = birthday.toInstant().atZone(getZoneId()).toLocalDate();
        return Period.between(birthDate, currentDate).getYears();
    }



    public String getFullNameWithMiddleInitial() {
        StringBuilder fullName = new StringBuilder();

        // Append first name
        fullName.append(firstName);

        // Append middle initial if present
        if (middleName != null && !middleName.isEmpty()) {
            fullName.append(" ").append(middleName.charAt(0)).append(".");
        }

        // Append last name
        fullName.append(" ").append(lastName);

        return fullName.toString();
    }

    public ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }

    public Date getJoinDate() {
        // For demonstration purposes, using the current date and time
        LocalDateTime joinDateTime = LocalDateTime.now();
        return Date.from(joinDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Updated method to set formatted dates
    private void setFormattedBirthday(Date birthday) {
        this.formattedBirthday = formatDate(birthday);
    }

    private void setFormattedAnniversary(Date anniversary) {
        if (anniversary != null) {
            this.formattedAnniversary = formatDate(anniversary);
        } else {
            this.formattedAnniversary = DEFAULT_DATE;
        }
        System.out.println("Formatted Anniversary: " + this.formattedAnniversary);
    }


    // Updated method to format a date
    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            return dateFormat.format(date);
        } else {
            return DEFAULT_DATE;
        }
    }

    // Updated method to update member details
    public void updateDetails(String firstName, String middleName, String lastName,
                              String contactNumber, String whatsappNumber,
                              Date birthday, Date weddingAnniversary,
                              boolean baptized, boolean cgo, boolean isActive) {
        // Update the details, including first name and last name
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.whatsappNumber = whatsappNumber;
        this.birthday = birthday;
        this.weddingAnniversary = weddingAnniversary;
        this.baptized = baptized;
        this.cgo = cgo;
        this.isActive = isActive;

        // Update the formatted birthday
        setFormattedBirthday(birthday);

        // Update the formatted anniversary
        setFormattedAnniversary(weddingAnniversary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return Objects.equals(getFirstName(), member.getFirstName()) &&
               Objects.equals(getLastName(), member.getLastName()) &&
               Objects.equals(getFormattedBirthday(), member.getFormattedBirthday()) &&
               Objects.equals(getFormattedAnniversary(), member.getFormattedAnniversary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFormattedBirthday(), getFormattedAnniversary());
    }

}
