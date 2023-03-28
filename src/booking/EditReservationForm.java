/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import static booking.Guest.currentGuest;
import java.awt.Color;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Gitu
 */
public class EditReservationForm extends javax.swing.JDialog {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;   
    private Statement st;
    private JDatePickerImpl arrivalPicker;
    private JDatePickerImpl departurePicker;
    private DateFormatter df;
    private String guestName;
    private Reservation resToEdit;
    private NewReservationPanel nrPanel = new NewReservationPanel();
    private ArrayList availableRoomList = new ArrayList();
    private ArrayList selectedRoomList = new ArrayList();
    private Guest selectedGuest;
    /**
     * Creates new form EditReservationForm
     */
    public EditReservationForm(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        arrivalDatePicker();
        departureDatePicker();
        customInit();
    }

    public EditReservationForm(java.awt.Dialog parent, boolean modal, String guestName, Reservation resToEdit) {
        super(parent, modal);
        initComponents();
        
        this.resToEdit = resToEdit;
        this.guestName = guestName;
        arrivalDatePicker();
        departureDatePicker();
        availableRoomList = getRoomNames(getAvailableRooms());
        selectedRoomList = getRoomNames(getReservedRooms());
        customInit();
    }

    private void customInit() {
        selectedGuest = currentGuest;
        if (resToEdit != null) {
            txtCurrentGuest.setText(guestName);
            guestCategoryCombo.setSelectedItem(searchCategory(resToEdit.getGuestCategory()));
            accommodationTypeCombo.setSelectedItem(searchAccommodationType(resToEdit.getAccommodationType()));
            numOfAdultsSpinner.setValue(resToEdit.getNumOfAdults());
            numOfChildrenU12Spinner.setValue(resToEdit.getNumOfChildrenU12());
            numOfChildrenTeenSpinner.setValue(resToEdit.getNumOfChildrenTeen());
            statusCombo.setSelectedItem(resToEdit.getStatus());
            
            availableRoomSelector.setModel(new javax.swing.AbstractListModel() {
                Object[] strings = availableRoomList.toArray();
                @Override
                public int getSize() { return strings.length; }
                @Override
                public Object getElementAt(int i) { return strings[i]; }
            });
            reservedRoomSelector.setModel(new javax.swing.AbstractListModel() {
                Object[] strings = selectedRoomList.toArray();
                @Override
                public int getSize() { return strings.length; }
                @Override
                public Object getElementAt(int i) { return strings[i]; }
            });
        }
        if (resToEdit.getPartyId() == null) {
            btnManageParty.setVisible(false);
        } else {
            btnManageParty.setVisible(true);
        }
        if (reservedRoomSelector.getModel().getSize() == 0) {
            btnAddRoom.setEnabled(true);
            btnRemoveRoom.setEnabled(true);
        } else {
            btnAddRoom.setEnabled(false);
            btnRemoveRoom.setEnabled(false);
        }
            
    }
    
    public void currentGuestDetails() {
        txtCurrentGuest.setText(currentGuest.getFirstName() + " " + currentGuest.getSurname());
    }
    // ********** date pickers **********
    private void arrivalDatePicker() {
        String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimePattern);
        UtilDateModel model  = new UtilDateModel();
        Date date = null;
        JDatePanelImpl datePanel;
            
        try {
            date = formatter.parse(resToEdit.getArrivalDate());
        } catch (ParseException ex) {
            Logger.getLogger(EditReservationForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);
        arrivalPicker = new JDatePickerImpl(datePanel, new DateFormatter());
        arrivalPicker.setBounds(270, 125, 200, 35);
        arrivalPicker.setBackground(Color.WHITE);
        model.setValue(date);
        arrivalPicker.setVisible(true);
        leftPanel.add(arrivalPicker);
        
    }
    
    private void departureDatePicker() {
        String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimePattern);
        UtilDateModel model = new UtilDateModel();
        Date date = null;
        JDatePanelImpl datePanel;

        try {
            date = formatter.parse(resToEdit.getDepartureDate());
        } catch (ParseException ex) {
            Logger.getLogger(EditReservationForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        datePanel = new JDatePanelImpl(model, p);
        departurePicker = new JDatePickerImpl(datePanel, new DateFormatter());
        departurePicker.setBounds(270, 195, 200, 35);
        departurePicker.setBackground(Color.WHITE);
        model.setValue(date);
        departurePicker.setVisible(true);
        leftPanel.add(departurePicker);
    }
    
 
    // ********** end date pickers **********
    
    private String getArrivalDate() throws ParseException {
        String arrivalDate, hour, minute;
        df = new DateFormatter();

        Date selectedArrivalDate = (Date) arrivalPicker.getModel().getValue();
        
        hour = arrivalHour.getValue().toString();
        minute = arrivalMinute.getValue().toString();
        arrivalDate = df.convertToLocalDate(selectedArrivalDate) +" "+ hour +":"+ minute;
 
        return arrivalDate;
    }
  
    private String getDepartureDate() throws ParseException {
        String departureDate;
        df = new DateFormatter();

        Date selectedDepartureDate = (Date) departurePicker.getModel().getValue();
        departureDate = df.convertToLocalDate(selectedDepartureDate) + "";

        return departureDate;
    }
         
    public double calculateCharges(long numOfNights, String guestCategory, String accommodationType) { 
        String cat;
        int numOfAdults, numOfChildrenU12, numOfChildrenTeen, guestCategoryId, accommodationTypeId;
        double rateAdults, rateChildrenU12 = 0, rateChildrenTeen = 0;
        
        guestCategoryId = nrPanel.searchCategoryId(guestCategory);
        accommodationTypeId = nrPanel.searchAccommodationId(accommodationType);
        cat = guestCategory.toLowerCase();
        
        numOfAdults = Integer.parseInt(numOfAdultsSpinner.getValue().toString());
        numOfChildrenU12 = Integer.parseInt(numOfChildrenU12Spinner.getValue().toString());
        numOfChildrenTeen = Integer.parseInt(numOfChildrenTeenSpinner.getValue().toString());
        
        rateAdults = nrPanel.searchRate(guestCategoryId, accommodationTypeId) * numOfAdults;
        if (cat.contains("non-resident adult")) {
            rateChildrenU12 = nrPanel.searchRate(nrPanel.searchCategoryId("Non-Resident Child U12"), accommodationTypeId) * numOfChildrenU12;
            rateChildrenTeen = nrPanel.searchRate(nrPanel.searchCategoryId("Non-Resident Child 12+"), accommodationTypeId) * numOfChildrenTeen;
        } else if (cat.contains("resident adult")) {
            rateChildrenU12 = nrPanel.searchRate(nrPanel.searchCategoryId("Resident Child U12"), accommodationTypeId) * numOfChildrenU12;
            rateChildrenTeen = nrPanel.searchRate(nrPanel.searchCategoryId("Resident Child 12+"), accommodationTypeId) * numOfChildrenTeen;
        } else {
            rateChildrenU12 = nrPanel.searchRate(nrPanel.searchCategoryId("Complementary"), accommodationTypeId) * numOfChildrenU12;
            rateChildrenTeen = nrPanel.searchRate(nrPanel.searchCategoryId("Complementary"), accommodationTypeId) * numOfChildrenTeen;
        }
        
        return (rateAdults + rateChildrenU12 + rateChildrenTeen) * numOfNights;
    }
    
    private String searchCategory(int search) {
        ArrayList<GuestCategory> categoryList = nrPanel.getGuestCategory();
        String category = null;
        for(GuestCategory cat : categoryList){
            if(cat.getCategoryId() != 0 && cat.getCategoryId() == search) {
                category = cat.getCategory();
            }
        }
        return category; 
    }
    
    private String searchAccommodationType(int search) {
        ArrayList<AccommodationType> typeList = nrPanel.getAccommodationType();
        String accommodationType = null;
        for(AccommodationType acc : typeList){
            if(acc.getId()!= 0 && acc.getId() == search) {
                accommodationType = acc.getType();
            }
        }
        return accommodationType; 
    }

    private long calculateNumOfNights(String arrivalDate, String departureDate) {
        long daysBetween, nights = 0;
        try {
            LocalDateTime arrival = df.stringToLocalDateTime(arrivalDate);
            LocalDateTime departure = df.stringToLocalDate(departureDate).atStartOfDay();
            daysBetween = Duration.between(arrival, departure).toDays();
            nights = daysBetween+1;
        } catch (ParseException | NullPointerException ex) {
            Logger.getLogger(EditReservationForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nights;
    }
    
     //get list of all rooms from database
    private ArrayList<Room> getAvailableRooms() {  
        ArrayList<Room> roomList = new ArrayList();
        try {
            //myConnection();
            String query = "SELECT * FROM `room` WHERE is_available=true";
            st = con.createStatement();
            rs = st.executeQuery(query);

            Room room;
            while (rs.next()) {
                room = new Room(rs.getInt("room_id"), rs.getString("room_name"), rs.getInt("num_of_beds"), rs.getBoolean("is_available"), rs.getInt("cottage_id"));
                
                roomList.add(room);
           }
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return roomList;
    }
    
    private ArrayList<Room> getReservedRooms() {
        ArrayList<Room> roomList = new ArrayList();
        int resId = 0;
        if (resToEdit != null)
            resId = resToEdit.getReservationId();
        try {
            //myConnection();
            PreparedStatement p;
            String query = "SELECT reserved_rooms.id, room.* FROM `reserved_rooms` JOIN room ON reserved_rooms.room_id=room.room_id WHERE `reservation_id`=?";

            p = con.prepareStatement(query);
            p.setInt(1, resId);

            rs = p.executeQuery();

            Room room;
            while (rs.next()) {
                room = new Room(rs.getInt("room_id"), rs.getString("room_name"), rs.getInt("num_of_beds"), rs.getBoolean("is_available"), rs.getInt("cottage_id"));
                
                roomList.add(room);
            }    
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot fetch. Problem... " + ex);
        }
        return roomList;
    }
    
    private ArrayList<String> getRoomNames(ArrayList<Room> list) {  
        //ArrayList<Room> list = getAvailableRooms();
        ArrayList<String> roomList = new ArrayList();
        HashSet hs = new HashSet();
        
        try {
            for (int i=0; i<list.size(); i++) {
                roomList.add(list.get(i).getRoomName());
            }
            hs.addAll(roomList);
            roomList.clear();
            roomList.addAll(hs);
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot get type. " + ex);
        }
        
        return roomList;
    }
    
    private void addRooms() {
        List rooms = availableRoomSelector.getSelectedValuesList();
        //int updatedRows = 0;
        for (Object x : rooms) {
            int roomId = 0;
            String room = x.toString();
            availableRoomList.remove(x);
            selectedRoomList.add(x);
            /*
            try {
                //myConnection();
                PreparedStatement p;

                String query = "SELECT `room_id` FROM `room` WHERE `room_name`=?";
                
                p = con.prepareStatement(query);
                p.setString(1, room);
                
                rs = p.executeQuery();

                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }    
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot fetch. " + ex);
            }
            
            try {
                PreparedStatement p;

                String newRowSql = "UPDATE `room` SET `is_available`=? WHERE room_id=?";
                p = con.prepareStatement(newRowSql);
                
                p.setBoolean(1, false);
                p.setInt(2, roomId);
                
                p.executeUpdate();
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot update record. " + ex);
             }*/
        }
        availableRoomSelector.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = availableRoomList.toArray();
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
        reservedRoomSelector.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = selectedRoomList.toArray();
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
    }
    
    private void removeRooms() {
        List rooms = reservedRoomSelector.getSelectedValuesList();
        //int updatedRows = 0;
        for (Object x : rooms) {
            int roomId = 0;
            String room = x.toString();
            selectedRoomList.remove(x);
            availableRoomList.add(x);
            /*
            try {
                //myConnection();
                PreparedStatement p;

                String query = "SELECT `room_id` FROM `room` WHERE `room_name`=?";
                
                p = con.prepareStatement(query);
                p.setString(1, room);
                
                rs = p.executeQuery();

                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }    
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot fetch. " + ex);
            }
            
            try {
                PreparedStatement p;

                String newRowSql = "UPDATE `room` SET `is_available`=? WHERE room_id=?";
                p = con.prepareStatement(newRowSql);
                
                p.setBoolean(1, true);
                p.setInt(2, roomId);
                
                p.executeUpdate();
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot update record. " + ex);
             }*/
        }
        availableRoomSelector.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = availableRoomList.toArray();
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
        reservedRoomSelector.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = selectedRoomList.toArray();
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
    }
    
    private void clearReservedRooms() {
        //List rooms = reservedRoomsList.getSelectedValuesList();
        for (int i = 0; i<reservedRoomSelector.getModel().getSize(); i++) {
            int roomId = 0;
            try {
                //myConnection();
                PreparedStatement p;

                String query = "SELECT `room_id` FROM `room` WHERE `room_name`=?";
                
                p = con.prepareStatement(query);
                p.setString(1, reservedRoomSelector.getModel().getElementAt(i).toString());
                
                rs = p.executeQuery();

                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }    
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot fetch. " + ex);
            }
            
            try {
                PreparedStatement p;

                String newRowSql = "DELETE FROM `reserved_rooms` WHERE reserved_rooms.room_id=?";
                p = con.prepareStatement(newRowSql);

                p.setInt(1, roomId);

                p.execute();
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot update record. " + ex);
            }
            try {
                PreparedStatement p;

                String newRowSql = "UPDATE `room` SET `is_available`=? WHERE room_id=?";
                p = con.prepareStatement(newRowSql);
                
                p.setBoolean(1, true);
                p.setInt(2, roomId);
                
                p.executeUpdate();
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot update record. " + ex);
             }
            btnAddRoom.setEnabled(true);
            btnRemoveRoom.setEnabled(true);
            availableRoomList = getRoomNames(getAvailableRooms());
            selectedRoomList = getRoomNames(getReservedRooms());
            availableRoomSelector.setModel(new javax.swing.AbstractListModel() {
                Object[] strings = availableRoomList.toArray();
                @Override
                public int getSize() { return strings.length; }
                @Override
                public Object getElementAt(int i) { return strings[i]; }
            });
            reservedRoomSelector.setModel(new javax.swing.AbstractListModel() {
                Object[] strings = selectedRoomList.toArray();
                @Override
                public int getSize() { return strings.length; }
                @Override
                public Object getElementAt(int i) { return strings[i]; }
            });
        }
    }
 
    private int reserveRooms() {
        int updatedRows = 0;
        int roomsListLength = reservedRoomSelector.getModel().getSize();
        
        if (roomsListLength != 0) {
            for (int i = 0; i<roomsListLength; i++) {
                int roomId = 0;
                //String room = x.toString();
                try {
                    //myConnection();
                    PreparedStatement p;

                    String query = "SELECT `room_id` FROM `room` WHERE `room_name`=?";

                    p = con.prepareStatement(query);
                    p.setString(1, reservedRoomSelector.getModel().getElementAt(i).toString());

                    rs = p.executeQuery();

                    if (rs.next()) {
                        roomId = rs.getInt("room_id");
                    }    
                }
                catch (Exception ex) {
                     System.out.println("Error! Cannot fetch. " + ex);
                }
                try {
                    PreparedStatement p;

                    String newRowSql = "INSERT INTO `reserved_rooms`(`reservation_id`, `room_id`) VALUES (?,?) ";
                    p = con.prepareStatement(newRowSql);

                    p.setInt(1, resToEdit.getReservationId());
                    p.setInt(2, roomId);

                    updatedRows = p.executeUpdate();
                }
                catch (Exception ex) {
                     System.out.println("Error! Cannot save record. " + ex);
                 }
                try {
                    PreparedStatement p;

                    String newRowSql = "UPDATE `room` SET `is_available`=? WHERE room_id=?";
                    p = con.prepareStatement(newRowSql);

                    p.setBoolean(1, false);
                    p.setInt(2, roomId);

                    p.executeUpdate();
                }
                catch (Exception ex) {
                     System.out.println("Error! Cannot update record. " + ex);
                 }
            }
            if (updatedRows > 0) {
                JOptionPane opt = new JOptionPane("Rooms Reserved.", JOptionPane.INFORMATION_MESSAGE);
                final JDialog jdg = opt.createDialog("Success");
                new Thread(new Runnable(){
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            jdg.dispose();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EditReservationForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
                jdg.setVisible(true);
            }
        }
        return updatedRows;
    }
    
    private HashMap searchSelectedParty() {
        HashMap<Object, Object> party = null;
        Integer searchKey = resToEdit.getPartyId();
        try {
            Statement st;

            String query = "SELECT * FROM `party` WHERE `party_id`='" + searchKey + "'";
 
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                party = new HashMap();
                party.put("partyId", rs.getInt("party_id"));
                party.put("partyName", rs.getString("party_name"));
            }
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        return party;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leftPanel = new javax.swing.JPanel();
        lblGuestDetails = new javax.swing.JLabel();
        txtCurrentGuest = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        guestCategoryCombo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        accommodationTypeCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        numOfAdultsSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        numOfChildrenU12Spinner = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        numOfChildrenTeenSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        arrivalHour = new javax.swing.JSpinner();
        arrivalMinute = new javax.swing.JSpinner();
        rightPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        statusCombo = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        availableRoomSelector = new javax.swing.JList();
        btnClearReservations = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        reservedRoomSelector = new javax.swing.JList();
        btnAddRoom = new javax.swing.JButton();
        btnRemoveRoom = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnManageParty = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        leftPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblGuestDetails.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        lblGuestDetails.setText("Reservation for: ");

        txtCurrentGuest.setEditable(false);
        txtCurrentGuest.setBackground(java.awt.SystemColor.controlHighlight);
        txtCurrentGuest.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtCurrentGuest.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel2.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel2.setText("Arrival Date");

        jLabel3.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel3.setText("Departure Date");

        jLabel6.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel6.setText("Guest Category");

        guestCategoryCombo.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        guestCategoryCombo.setModel(new javax.swing.DefaultComboBoxModel(nrPanel.getCategoryList().toArray()));

        jLabel7.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel7.setText("Accommodation Type");

        accommodationTypeCombo.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        accommodationTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(nrPanel.getTypeList().toArray()));

        jLabel4.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel4.setText("Number of Adults");

        numOfAdultsSpinner.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        numOfAdultsSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 0, 50, 1));

        jLabel5.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel5.setText("Number of Children U12");

        numOfChildrenU12Spinner.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        numOfChildrenU12Spinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 50, 1));

        jLabel9.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel9.setText("12+");

        numOfChildrenTeenSpinner.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        numOfChildrenTeenSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 50, 1));

        jLabel1.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel1.setText("Arrival Time");

        arrivalHour.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        arrivalHour.setModel(new javax.swing.SpinnerNumberModel(10, 1, 23, 1));

        arrivalMinute.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        arrivalMinute.setModel(new javax.swing.SpinnerListModel(new String[] {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"}));
        arrivalMinute.setFocusCycleRoot(true);

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(numOfChildrenTeenSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(numOfChildrenU12Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtCurrentGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblGuestDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(accommodationTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(guestCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(numOfAdultsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(arrivalHour, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(arrivalMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGuestDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCurrentGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(arrivalHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(arrivalMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guestCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accommodationTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numOfAdultsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numOfChildrenU12Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numOfChildrenTeenSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        rightPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel11.setText("Status");

        statusCombo.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        statusCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Waiting", "Checked In" }));

        jLabel8.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel8.setText("Select Room(s) to Reserve ");

        jLabel10.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel10.setText("Reserved");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        availableRoomSelector.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        availableRoomSelector.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = getRoomNames(getAvailableRooms()).toArray();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(availableRoomSelector);

        btnClearReservations.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        btnClearReservations.setText("Clear");
        btnClearReservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearReservationsActionPerformed(evt);
            }
        });

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        reservedRoomSelector.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        reservedRoomSelector.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = getRoomNames(getReservedRooms()).toArray();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(reservedRoomSelector);

        btnAddRoom.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        btnAddRoom.setText(">>");
        btnAddRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRoomActionPerformed(evt);
            }
        });

        btnRemoveRoom.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        btnRemoveRoom.setText("<<");
        btnRemoveRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveRoomActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(245, 108, 108));
        btnCancel.setFont(new java.awt.Font("Corbel", 0, 22)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(105, 205, 130));
        btnSave.setFont(new java.awt.Font("Corbel", 0, 22)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnManageParty.setBackground(new java.awt.Color(80, 170, 195));
        btnManageParty.setFont(new java.awt.Font("Corbel", 1, 16)); // NOI18N
        btnManageParty.setForeground(new java.awt.Color(255, 255, 255));
        btnManageParty.setText("Manage Party");
        btnManageParty.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnManageParty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManagePartyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(rightPanelLayout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(statusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(rightPanelLayout.createSequentialGroup()
                                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnClearReservations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnAddRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRemoveRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnManageParty, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                        .addComponent(btnClearReservations, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAddRoom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRemoveRoom)
                        .addGap(15, 15, 15)))
                .addGap(60, 60, 60)
                .addComponent(btnManageParty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(leftPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            PreparedStatement p;
            String arrivalDate, departureDate, status, guestCategory, accommodationType;
            Integer reservationId, numOfAdults, numOfChildrenU12, numOfChildrenTeen, partyId;
            long numOfNights;
            double charges;
            
            reservationId = resToEdit.getReservationId();
            arrivalDate = getArrivalDate();
            departureDate = getDepartureDate();
            guestCategory = guestCategoryCombo.getSelectedItem().toString();
            accommodationType = accommodationTypeCombo.getSelectedItem().toString();
            /*numOfNights = calculateNumOfNights(arrivalDate, departureDate);
            numOfAdults = Integer.parseInt(numOfAdultsSpinner.getValue().toString());
            numOfChildrenU12 = Integer.parseInt(numOfChildrenU12Spinner.getValue().toString());
            numOfChildrenTeen = Integer.parseInt(numOfChildrenTeenSpinner.getValue().toString());
            charges = calculateCharges(numOfNights, guestCategory, accommodationType);*/
            status = statusCombo.getSelectedItem().toString();
            partyId = resToEdit.getPartyId();

            if (guestCategoryCombo.getSelectedItem().equals("Day/Walk-In")) {
                numOfNights = 0;
                numOfAdults = 0;
                numOfChildrenU12 = 0;
                numOfChildrenTeen = 0;
                charges = 0.0;
            } else {
                numOfNights = calculateNumOfNights(arrivalDate, departureDate);
                numOfAdults = Integer.parseInt(numOfAdultsSpinner.getValue().toString());
                numOfChildrenU12 = Integer.parseInt(numOfChildrenU12Spinner.getValue().toString());
                numOfChildrenTeen = Integer.parseInt(numOfChildrenTeenSpinner.getValue().toString());
                charges = calculateCharges(numOfNights, guestCategory, accommodationType);
            }
            
            String newRowSql = "UPDATE `reservation` SET `arrival_date`=?,`departure_date`=?, `num_of_nights`=?, `num_of_adults`=?, `num_of_children_u12`=?, `num_of_children_teen`=?, `charges`=?, `status`=?, `guest_category`=?, `accommodation_type`=?, `party_id`=? WHERE reservation_id=?";

            p = con.prepareStatement(newRowSql);
            p.setString(1, arrivalDate);
            p.setString(2, departureDate);
            p.setLong(3, numOfNights);
            p.setInt(4, numOfAdults);
            p.setInt(5, numOfChildrenU12);
            p.setInt(6, numOfChildrenTeen);
            p.setDouble(7, charges);
            p.setString(8, status);
            p.setInt(9, nrPanel.searchCategoryId(guestCategory));
            p.setInt(10, nrPanel.searchAccommodationId(accommodationType));
            p.setObject(11, partyId, Types.INTEGER);
            p.setInt(12, reservationId);

            int updatedRows = p.executeUpdate();
            reserveRooms();
            if (updatedRows > 0) {
                JOptionPane opt = new JOptionPane("Record Updated Successfully!", JOptionPane.INFORMATION_MESSAGE);
                final JDialog success = opt.createDialog("Success");
                new Thread(new Runnable(){
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            success.dispose();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EditReservationForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
                success.setVisible(true);
                this.dispose();
            }
            else
                JOptionPane.showMessageDialog(null, "Error! Unable to add record.");
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error! Cannot add reservation. " + ex.getMessage());
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAddRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRoomActionPerformed
        addRooms();
    }//GEN-LAST:event_btnAddRoomActionPerformed

    private void btnRemoveRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveRoomActionPerformed
        removeRooms();
    }//GEN-LAST:event_btnRemoveRoomActionPerformed

    private void btnClearReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearReservationsActionPerformed
        clearReservedRooms();
        btnAddRoom.setEnabled(true);
        btnRemoveRoom.setEnabled(true);
    }//GEN-LAST:event_btnClearReservationsActionPerformed

    private void btnManagePartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManagePartyActionPerformed
        HashMap selectedParty = searchSelectedParty();
        if (selectedParty == null) {
            ManagePartyForm manageParty = new ManagePartyForm((Frame) this.getOwner().getParent(), true);
            manageParty.setTitle("Manage Party Details");
            manageParty.setLocationRelativeTo(null);
            manageParty.setVisible(true);
            manageParty.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else {
            ManagePartyForm manageParty = new ManagePartyForm((Frame) this.getOwner().getParent(), true, selectedParty);
            manageParty.setTitle("Manage Party Details");
            manageParty.setLocationRelativeTo(null);
            manageParty.setVisible(true);
            manageParty.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }//GEN-LAST:event_btnManagePartyActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced numOfChildrenU12Spinnerable, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditReservationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditReservationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditReservationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditReservationForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EditReservationForm erForm = new EditReservationForm(new javax.swing.JDialog(), true);
                erForm.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                erForm.setTitle("Edit Reservation Form");
                erForm.setLocationRelativeTo(null);
                erForm.setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox accommodationTypeCombo;
    private javax.swing.JSpinner arrivalHour;
    private javax.swing.JSpinner arrivalMinute;
    private javax.swing.JList availableRoomSelector;
    private javax.swing.JButton btnAddRoom;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClearReservations;
    private javax.swing.JButton btnManageParty;
    private javax.swing.JButton btnRemoveRoom;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox guestCategoryCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblGuestDetails;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JSpinner numOfAdultsSpinner;
    private javax.swing.JSpinner numOfChildrenTeenSpinner;
    private javax.swing.JSpinner numOfChildrenU12Spinner;
    private javax.swing.JList reservedRoomSelector;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JComboBox statusCombo;
    private javax.swing.JTextField txtCurrentGuest;
    // End of variables declaration//GEN-END:variables
}
