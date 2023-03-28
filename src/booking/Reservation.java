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
public class Reservation {
    DateFormatter df;
    /*
    Instant bookingDate;
    LocalDateTime arrivalDate; 
    LocalDate departureDate;
    */
    private String bookingDate, arrivalDate, departureDate, status;
    private Integer reservationId, numOfAdults, numOfChildrenU12, numOfChildrenTeen, guestCategory, userId, guestId, accommodationType, partyId;
    long numOfNights;
    private double charges;
    
    Reservation() {
        
    }
    Reservation(int reservationId, String bookingDate, String arrivalDate, String departureDate, long numOfNights, int numOfAdults, int numOfChildrenU12, int numOfChildrenTeen, double charges, String status, int guestCategory, int userId, int guestId, int accommodationType, Integer partyId) {
        this.reservationId = reservationId;
        this.bookingDate = bookingDate;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.numOfNights = numOfNights;
        this.numOfAdults = numOfAdults;
        this.numOfChildrenU12 = numOfChildrenU12;
        this.numOfChildrenTeen = numOfChildrenTeen;
        this.charges = charges;
        this.status = status;
        this.guestCategory = guestCategory;
        this.userId = userId;
        this.guestId = guestId;
        this.accommodationType = accommodationType;
        this.partyId = partyId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
    public void setNumOfNights(long numOfNights) {
        this.numOfNights = numOfNights;
    }
    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
    }
    public void setNumOfChildrenU12(int numOfChildrenU12) {
        this.numOfChildrenU12 = numOfChildrenU12;
    }
    public void setNumOfChildrenTeen(int numOfChildrenTeen) {
        this.numOfChildrenTeen = numOfChildrenTeen;
    }
    public void setCharges(double charges) {
        this.charges = charges;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setGuestCategory(int guestCategory) {
        this.guestCategory = guestCategory;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }
    public void setAccommodationType(int accommodationType) {
        this.accommodationType = accommodationType;
    }
    public void setPartyId(Integer partyId) {
        this.partyId = partyId;
    }
    
    public int getReservationId() {
        return reservationId;
    }
    public String getBookingDate() {
        return bookingDate;
    }
    public String getArrivalDate() {
        return arrivalDate;
    }
    public String getDepartureDate() {
        return departureDate;
    }
    public long getNumOfNights() {
        return numOfNights;
    }
    public int getNumOfAdults() {
        return numOfAdults;
    }
    public int getNumOfChildrenU12() {
        return numOfChildrenU12;
    }
    public int getNumOfChildrenTeen() {
        return numOfChildrenTeen;
    }
    public double getCharges() {
        return charges;
    }
    public String getStatus() {
        return status;
    }
    public int getGuestCategory() {
        return guestCategory;
    }
    public int getUserId() {
        return userId;
    }
    public int getGuestId() {
        return guestId;
    }
    public int getAccommodationType() {
        return accommodationType;
    }
    public Integer getPartyId() {
        return partyId;
    }
}
