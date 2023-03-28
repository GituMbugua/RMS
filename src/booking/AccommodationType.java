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
public class AccommodationType {
    private int accomodation_type_id;
    private String type;
    //private double rate;
    
    AccommodationType() {
        
    }
    
    AccommodationType(int accomodation_type_id, String type) {
        this.accomodation_type_id = accomodation_type_id;
        this.type = type;
        //this.rate = rate;
    }
    
    public void setId(int accomodation_type_id) {
        this.accomodation_type_id = accomodation_type_id;
    }
    public void setAccomodationType(String type) {
        this.type = type;
    }
    
    
    public int getId() {
        return accomodation_type_id;
    }
    
    public String getType() {
        return type;
    }
    
    
}
