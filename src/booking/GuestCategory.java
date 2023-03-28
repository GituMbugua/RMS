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
public class GuestCategory {
    private int categoryId;
    private String category;
    //private double rate;
    
    GuestCategory() {
        
    }
    GuestCategory(int categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
        //this.rate = rate;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public String getCategory() {
        return category;
    }
    
    
}
