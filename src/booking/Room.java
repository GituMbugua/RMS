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
public class Room {
    int roomId, numOfBeds, cottageId;
    String roomName;
    boolean isAvailable = false;
    
    Room() {
        
    }
    Room(int roomId, String roomName, int numOfBeds, boolean isAvailable, int cottageId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.numOfBeds = numOfBeds;
        this.isAvailable = isAvailable;
        this.cottageId = cottageId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public void setNumOfBeds(int numOfBeds) {
        this.numOfBeds = numOfBeds;
    }
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public void setCottageId(int cottageId) {
        this.cottageId = cottageId;
    }
    
    public int getRoomId() {
        return roomId;
    }
    public String getRoomName() {
        return roomName;
    }
    public int getNumOfBeds() {
        return numOfBeds;
    }
    public boolean getAvailability() {
        return isAvailable;
    }
    public int getCottageId() {
        return cottageId;
    }

}
