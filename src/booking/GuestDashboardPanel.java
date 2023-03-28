/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import static booking.LoginForm.currentUser;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gitu
 */
public class GuestDashboardPanel extends javax.swing.JPanel {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;   
    private Statement st;
    private ArrayList<Guest> guestList;
    
    /**
     * Creates new form GuestDashboardPanel
     */
    public GuestDashboardPanel() {
        initComponents();
        customInit();
    }
    
    private void customInit() {
        String userRole = currentUser.getRole().toLowerCase();
        switch (userRole) {
            case "admin":
            case "general":
                btnAddNew.setEnabled(true);
                break;
            case "audit":
                btnAddNew.setEnabled(false);
                btnAddNew.setBackground(new Color(240, 240, 240));
                break;
        }
        btnEdit.setEnabled(false);
        lblFname.setEnabled(false);
        lblSurname.setEnabled(false);
        searchFirstName.setEnabled(false);
        searchSurname.setEnabled(false);
        btnSearch.setEnabled(false);
        tblGuestRecords.getTableHeader().setFont(new Font("Corbel", Font.PLAIN, 18));
    }
    
    private ArrayList<Guest> getGuestRecords() {
        try {
            guestList = new ArrayList();
                
            String query = "SELECT * FROM guest";
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            Guest guest;
            while (rs.next()) {
                guest = new Guest(rs.getString("first_name"), rs.getString("middle_name"), rs.getString("surname"), rs.getString("date_of_birth"), rs.getString("phone_number"), rs.getString("address"), rs.getString("email"), rs.getString("national_id_num"), rs.getString("passport_num"), rs.getString("nationality"), rs.getString("country_of_residence"), rs.getString("occupation"), rs.getString("health_considerations"), rs.getString("preferred_room"));
                guest.setId(rs.getInt("guest_id"));
                guestList.add(guest);
            }
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        return guestList;
    }
    private ArrayList<Guest> searchGuest() {                                          
        try {
            PreparedStatement p = null;
            String query = "";
            guestList = new ArrayList();

            String firstName, surname;
            firstName = searchFirstName.getText();
            surname = searchSurname.getText();
            
            if (!firstName.isEmpty() && !surname.isEmpty()) {
                query = "SELECT * FROM `guest` WHERE `first_name` = ? AND `surname` = ?";
                p = con.prepareStatement(query);
                p.setString(1, firstName);
                p.setString(2, surname);
            }
            else if (firstName.isEmpty() && !surname.isEmpty()) {
                query = "SELECT * FROM `guest` WHERE `surname` = ?";
                p = con.prepareStatement(query);
                p.setString(1, surname);
            }
            else if (!firstName.isEmpty() && surname.isEmpty()) {
                query = "SELECT * FROM `guest` WHERE `first_name` = ?";
                p = con.prepareStatement(query);
                p.setString(1, firstName);
            }
            else
                JOptionPane.showMessageDialog(null, "No input detected!");

            rs = p.executeQuery();
            
            Guest guest;
            while (rs.next()) {
                guest = new Guest(rs.getString("first_name"), rs.getString("middle_name"), rs.getString("surname"), rs.getString("date_of_birth"), rs.getString("phone_number"), rs.getString("address"), rs.getString("email"), rs.getString("national_id_num"), rs.getString("passport_num"), rs.getString("nationality"), rs.getString("country_of_residence"), rs.getString("occupation"), rs.getString("health_considerations"), rs.getString("preferred_room"));
                guest.setId(rs.getInt("guest_id"));
                guestList.add(guest);
            }
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        return guestList;
    } 
    
    private void displayGuestRecords(ArrayList<Guest> guestList) {
        DefaultTableModel model = (DefaultTableModel) tblGuestRecords.getModel();
        if (!guestList.isEmpty()) {
            model.setRowCount(0);
            Object[] col = new Object[15];
            for (int i=0; i<guestList.size(); i++) {
                col[0] = guestList.get(i).getId();
                col[1] = guestList.get(i).getFirstName();
                col[2] = guestList.get(i).getMiddleName();
                col[3] = guestList.get(i).getSurname();
                col[4] = guestList.get(i).getDob();
                col[5] = guestList.get(i).getPhoneNumber();
                col[6] = guestList.get(i).getAddress();
                col[7] = guestList.get(i).getEmail();
                col[8] = guestList.get(i).getIdNum();
                col[9] = guestList.get(i).getPassportNum();
                col[10] = guestList.get(i).getNationality();
                col[11] = guestList.get(i).getCountryofResidence();
                col[12] = guestList.get(i).getOccupation();
                col[13] = guestList.get(i).getHealth();
                col[14] = guestList.get(i).getPreferredRoom();
                model.addRow(col);
            }            
        } else {
            String noRecords = "No Records.";
            model.setRowCount(0);
            Object[] col = new Object[1];
            col[0] = noRecords;

            model.addRow(col);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlsPanel = new javax.swing.JPanel();
        searchSurname = new javax.swing.JTextField();
        searchFirstName = new javax.swing.JTextField();
        lblSurname = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        lblFname = new javax.swing.JLabel();
        searchByCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnAddNew = new javax.swing.JButton();
        tablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGuestRecords = new javax.swing.JTable();

        setBackground(new java.awt.Color(240, 240, 250));
        setMaximumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(1240, 450));

        controlsPanel.setBackground(new java.awt.Color(255, 255, 255));

        searchSurname.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

        searchFirstName.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

        lblSurname.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        lblSurname.setText("Surname");

        btnSearch.setBackground(new java.awt.Color(250, 250, 250));
        btnSearch.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("Search");
        btnSearch.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        lblFname.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        lblFname.setText("First Name");

        searchByCombo.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        searchByCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None Selected", "All Records", "Guest Name" }));
        searchByCombo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        searchByCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchByComboActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel1.setText("Search By");

        btnEdit.setBackground(new java.awt.Color(250, 250, 250));
        btnEdit.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnAddNew.setBackground(new java.awt.Color(105, 205, 130));
        btnAddNew.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnAddNew.setForeground(new java.awt.Color(255, 255, 255));
        btnAddNew.setText("Add  New");
        btnAddNew.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlsPanelLayout = new javax.swing.GroupLayout(controlsPanel);
        controlsPanel.setLayout(controlsPanelLayout);
        controlsPanelLayout.setHorizontalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchByCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFname, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(76, 76, 76)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addComponent(lblSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addComponent(searchSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
        );
        controlsPanelLayout.setVerticalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchByCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        tablePanel.setBackground(new java.awt.Color(255, 255, 255));
        tablePanel.setPreferredSize(new java.awt.Dimension(1240, 450));
        tablePanel.setLayout(new java.awt.GridLayout(1, 1));

        tblGuestRecords.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        tblGuestRecords.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        tblGuestRecords.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Guest ID", "First Name", "Middle Name", "Surname", "Date of Birth", "Phone Number", "Address", "Email", "National ID Number", "Passport Number", "Nationality", "Country of Residence", "Occupation", "Health Considerations", "Preferred Room"
            }
        ));
        tblGuestRecords.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblGuestRecords.setRowHeight(25);
        tblGuestRecords.setSelectionBackground(new java.awt.Color(105, 205, 130));
        tblGuestRecords.setShowVerticalLines(false);
        tblGuestRecords.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGuestRecordsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGuestRecords);
        if (tblGuestRecords.getColumnModel().getColumnCount() > 0) {
            tblGuestRecords.getColumnModel().getColumn(0).setPreferredWidth(50);
        }

        tablePanel.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(controlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    private void searchByComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchByComboActionPerformed
        int value = searchByCombo.getSelectedIndex();
        switch (value) {
            case 1:
                lblFname.setEnabled(false);
                lblSurname.setEnabled(false);
                searchFirstName.setEnabled(false);
                searchSurname.setEnabled(false);
                btnSearch.setEnabled(false);
                btnSearch.setBackground(new Color(250, 250, 250));
                
                displayGuestRecords(getGuestRecords());
                break;
            case 2:
                lblFname.setEnabled(true);
                lblSurname.setEnabled(true);
                searchFirstName.setEnabled(true);
                searchSurname.setEnabled(true);
                btnSearch.setEnabled(true);
                btnSearch.setBackground(new Color(105, 205, 130));
                
                break;
        }
    }//GEN-LAST:event_searchByComboActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        displayGuestRecords(searchGuest());
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tblGuestRecordsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGuestRecordsMouseClicked
        btnEdit.setEnabled(true);
        btnEdit.setBackground(new Color(80, 170, 195));
        /*
        String userRole = currentUser.getRole().toLowerCase();
        switch (userRole) {
            case "admin":
                break;
            case "general":
                btnEdit.setEnabled(true);
                btnEdit.setBackground(new Color(80, 170, 195));
                break;
            case "audit":
                btnEdit.setEnabled(false);
                btnEdit.setBackground(new Color(240, 240, 240));
                break;
        }*/
    }//GEN-LAST:event_tblGuestRecordsMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblGuestRecords.getModel();
        int selectedRowIndex = tblGuestRecords.getSelectedRow();
        
        if (tblGuestRecords.getSelectedRowCount() != 0) {
            Guest guestToEdit;
            int guestId;
            String firstName, middleName, surname, dateOfBirth, phoneNumber, address, email, idNum, passportNum, nationality, countryOfResidence, occupation, healthConsiderations, preferredRoom;
            
            guestId = Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString());
            firstName = model.getValueAt(selectedRowIndex, 1).toString();
            middleName = model.getValueAt(selectedRowIndex, 2).toString();
            surname = model.getValueAt(selectedRowIndex, 3).toString();
            dateOfBirth = model.getValueAt(selectedRowIndex, 4).toString();
            phoneNumber = model.getValueAt(selectedRowIndex, 5).toString();
            address = model.getValueAt(selectedRowIndex, 6).toString();
            email = model.getValueAt(selectedRowIndex, 7).toString();
            idNum = model.getValueAt(selectedRowIndex, 8).toString();
            passportNum = model.getValueAt(selectedRowIndex, 9).toString();
            nationality = model.getValueAt(selectedRowIndex, 10).toString();
            countryOfResidence = model.getValueAt(selectedRowIndex, 11).toString();
            occupation = model.getValueAt(selectedRowIndex, 12).toString();
            healthConsiderations = model.getValueAt(selectedRowIndex, 13).toString();
            preferredRoom = model.getValueAt(selectedRowIndex, 14).toString();

            guestToEdit = new Guest(firstName, middleName, surname, dateOfBirth, phoneNumber, address, email, idNum, passportNum, nationality, countryOfResidence, occupation, healthConsiderations, preferredRoom);
            guestToEdit.setId(guestId);
            
            GuestDetailsForm editGuest = new GuestDetailsForm((JFrame)this.getRootPane().getParent(), true, guestToEdit);
            editGuest.setTitle("Guest Details Form");
            editGuest.setLocationRelativeTo(editGuest.getParent());
            editGuest.setVisible(true);
            editGuest.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            editGuest.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    displayGuestRecords(getGuestRecords());
                }
            });
        } else {
            JOptionPane opt = new JOptionPane("No selected records to edit.", JOptionPane.ERROR_MESSAGE);
                final JDialog jdg = opt.createDialog("Error");
                new Thread(new Runnable(){
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            jdg.dispose();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EditReservationForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
                jdg.setVisible(true);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewActionPerformed
        GuestDetailsForm newGuest = new GuestDetailsForm((JFrame)this.getRootPane().getParent(), true);
        newGuest.setTitle("Guest Details Form");
        newGuest.setLocationRelativeTo(newGuest.getParent());
        newGuest.setVisible(true);
        newGuest.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newGuest.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                displayGuestRecords(getGuestRecords());
            }
        });
    }//GEN-LAST:event_btnAddNewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNew;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFname;
    private javax.swing.JLabel lblSurname;
    private javax.swing.JComboBox searchByCombo;
    private javax.swing.JTextField searchFirstName;
    private javax.swing.JTextField searchSurname;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTable tblGuestRecords;
    // End of variables declaration//GEN-END:variables
}
