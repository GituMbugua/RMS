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
public class Rates {
    private int rateId, guestCategoryId, accomodationTypeId;
    private double rate;
    
    Rates(int rateId, int guestCategoryId, int accomodationTypeId, double rate) {
        this.rateId = rateId;
        this.guestCategoryId = guestCategoryId;
        this.accomodationTypeId = accomodationTypeId;
        this.rate = rate;
    }
    public void setRateId(int rateId) {
        this.rateId = rateId;
    }
    public void setGuestCategoryId(int guestCategoryId) {
        this.guestCategoryId = guestCategoryId;
    }
    public void setAccomodationTypeId(int accomodationTypeId) {
        this.accomodationTypeId = accomodationTypeId;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    public int getRateId() {
        return rateId;
    }
    
    public int getGuestCategoryId() {
        return guestCategoryId;
    }
    
    public int getAccomodationTypeId() {
        return accomodationTypeId;
    }
    
    public double getRate() {
        return rate;
    }
}
