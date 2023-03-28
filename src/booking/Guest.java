/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

/**
 *
 * @author Gitu
 */
public class Guest extends Person {
    private String idNum, passportNum, nationality, countryOfResidence, occupation, healthConsiderations, preferredRoom;
    static Guest currentGuest = new Guest();
    Guest() {
        
    }
    
    Guest(String firstName, String middleName, String surname, String dateOfBirth, 
            String phoneNumber, String address, String email, String idNum, String passportNum, 
             String nationality, String countryOfResidence, String occupation, String healthConsiderations, String preferredRoom) {
        
        super(firstName, middleName, surname, dateOfBirth, phoneNumber, address, email);
        this.idNum = idNum;
        this.passportNum = passportNum;
        this.nationality = nationality;
        this.countryOfResidence = countryOfResidence;
        this.occupation = occupation;
        this.healthConsiderations = healthConsiderations;
        this.preferredRoom = preferredRoom;
    }
    
    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }
    public void setPassportNum(String passportNum) {
        this.passportNum = passportNum;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public void setCountryofResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public void setHealth(String healthConsiderations) {
        this.healthConsiderations = healthConsiderations;
    }
    public void setPreferredRoom(String preferredRoom) {
        this.preferredRoom = preferredRoom;
    }
    
    public String getIdNum() {
        return idNum;
    }
    public String getPassportNum() {
        return passportNum;
    }
    public String getNationality() {
        return nationality;
    }
    public String getCountryofResidence() {
        return countryOfResidence;
    }
    public String getOccupation() {
        return occupation;
    }
    public String getHealth() {
        return healthConsiderations;
    }
    public String getPreferredRoom() {
        return preferredRoom;
    }
}
