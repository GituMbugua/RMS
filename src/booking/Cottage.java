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
public class Cottage {
    int cottageId, numOfRooms;
    String cottageName;
    boolean isAvailable;
    
    Cottage() {
        
    }
    Cottage(int cottageId, String cottageName, int numOfRooms, boolean isAvailable) {
        this.cottageId = cottageId;
        this.cottageName = cottageName;
        this.numOfRooms = numOfRooms;
        this.isAvailable = isAvailable;
    }
    
    public void setCottageId(int cottageId) {
        this.cottageId = cottageId;
    }
    public void setCottageName(String cottageName) {
        this.cottageName = cottageName;
    }
    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public int getCottageId() {
        return cottageId;
    }
    public String getCottageName() {
        return cottageName;
    }
    public int getNumOfRooms() {
        return numOfRooms;
    }
    public boolean getAvailability() {
        return isAvailable;
    }
    
}
