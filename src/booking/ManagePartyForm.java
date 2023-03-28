/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import static booking.Guest.currentGuest;
import static booking.GuestDetailsForm.guestRegistered;
import static booking.SearchGuestPanel.guestConfirmValue;
import static booking.SearchGuestPanel.selectedGuest;
import java.awt.Color;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gitu
 */
public class ManagePartyForm extends javax.swing.JDialog {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;   
    private Statement st;
    private Guest registeredGuest;
    private String action;
    private ArrayList<String> guestIdList;
    private ArrayList<HashMap> guestList;
    private static HashMap<Object, Object> selectedParty;
    /**
     * Creates new form ManagePartyForm
     */
    public ManagePartyForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        action = "new";
        customInit();
    }
    
    public ManagePartyForm(java.awt.Frame parent, boolean modal, HashMap party) {
        super(parent, modal);
        initComponents();
        selectedParty = party;
        getGuestRecords(Integer.parseInt(selectedParty.get("partyId").toString()));
        displayGuestDetails();
        action = "edit";
        customInit();
    }
    public ManagePartyForm(java.awt.Dialog parent, boolean modal, HashMap party) {
        super(parent, modal);
        initComponents();
        selectedParty = party;
        getGuestRecords(Integer.parseInt(selectedParty.get("partyId").toString()));
        displayGuestDetails();
        action = "edit";
        customInit();
    }
    
    private void customInit() {
        guestIdList = new ArrayList();
        btnRemove.setEnabled(false);
        switch (action) {
            case "new":
                selectedParty = new HashMap();
                selectedParty.put("partyName", currentGuest.getFirstName() + currentGuest.getSurname() + java.time.LocalDate.now() + "");
                txtPartyName.setText(selectedParty.get("partyName").toString());
                break;
            case "edit":
                txtPartyName.setText(selectedParty.get("partyName").toString());
        }
    }
    
    private ArrayList getGuestRecords(int partyId) {
        guestList = new ArrayList<>();
        try {
            Statement st;
        
            String query = "SELECT guest_party.party_id, guest.guest_id, guest.first_name, guest.middle_name, guest.surname, guest.national_id_num, guest.passport_num FROM `guest_party` JOIN guest ON guest.guest_id=guest_party.guest_id WHERE party_id='" + partyId + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);

            HashMap<Object, Object> res;
            while (rs.next()) {
                int i = 1;
                
                res = new HashMap<>();
                res.put("partyId", rs.getInt(i++));
                res.put("guestId", rs.getInt(i++));
                res.put("guestFirstName", rs.getString(i++));
                res.put("guestMiddleName", rs.getString(i++));
                res.put("guestSurname", rs.getString(i++));
                res.put("natIdNum", rs.getString(i++));
                res.put("passportNum", rs.getString(i++));
                
                guestList.add(res);
            }  
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        return guestList;
    }
    
    private void displayGuestDetails() {
        DefaultTableModel model = (DefaultTableModel)tblPartyMembers.getModel();
        if (!guestList.isEmpty()) {
            model.setRowCount(0);
            Object[] col = new Object[7];
            for (int i=0; i<guestList.size(); i++) {
                col[0] = guestList.get(i).get("guestId");
                col[1] = guestList.get(i).get("guestFirstName");
                col[2] = guestList.get(i).get("guestMiddleName");
                col[3] = guestList.get(i).get("guestSurname");
                col[4] = guestList.get(i).get("natIdNum");
                col[5] = guestList.get(i).get("passportNum");;
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
    
    private void displayGuestDetails(Guest guest) {
        DefaultTableModel model = (DefaultTableModel)tblPartyMembers.getModel();
        Object[] col = new Object[7];
        col[0] = guest.getId();
        col[1] = guest.getFirstName();
        col[2] = guest.getMiddleName();
        col[3] = guest.getSurname();
        col[4] = guest.getIdNum();
        col[5] = guest.getPassportNum();
        model.addRow(col);
    }
    
    private int getSelectedRecordId() {
        DefaultTableModel model = (DefaultTableModel)tblPartyMembers.getModel();
        int selectedRowIndex = tblPartyMembers.getSelectedRow();
        int column = 0;
        String value = model.getValueAt(selectedRowIndex, column).toString();
        
        return Integer.parseInt(value);
    }
    
    private void removeSelectedRecord(int id) {
        DefaultTableModel model = (DefaultTableModel)tblPartyMembers.getModel();
        int selectedRowIndex = tblPartyMembers.getSelectedRow();
       
        guestIdList.remove(String.valueOf(id));
        model.removeRow(selectedRowIndex);
        btnRemove.setEnabled(false);
    }
    
    private void deleteGuestFromParty(int guestId) {
        try {
            PreparedStatement p;

            String query = "DELETE FROM `guest_party` WHERE party_id=? AND guest_id=?";
            
            p = con.prepareStatement(query);
            p.setInt(1, Integer.parseInt(selectedParty.get("partyId").toString()));
            p.setInt(2, guestId);
            
            int updatedRows = p.executeUpdate();
            
            if (updatedRows > 0)
                JOptionPane.showMessageDialog(null, "Record Deleted Successfully!");
            else
                JOptionPane.showMessageDialog(null, "Error! Unable to add.");
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        btnRemove.setEnabled(false);
    }
    
    private void createParty() {
        try {
            PreparedStatement p;

            String newRowSql = "INSERT INTO `party`(`party_name`, `guest_id`) VALUES (?,?)";
            
            p = con.prepareStatement(newRowSql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, selectedParty.get("partyName").toString());
            p.setInt(2, currentGuest.getId());
            
            int updatedRows = p.executeUpdate();
            
            rs = p.getGeneratedKeys();
            if (rs != null && rs.next() )
                selectedParty.put("partyId", rs.getInt(1));
            else
                System.out.println("Cannot get result set.");
            
            if (updatedRows <= 0)
                JOptionPane.showMessageDialog(null, "Error! Unable to create party.");
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
    }
    /*
    private boolean searchParty() {
        boolean exists = false;
        try {
            Statement st;

            String query = "SELECT * FROM `party` WHERE `party_name`='" + selectedPartyName + "'";
 
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                exists = true;
                selectedParty.put("partyId", rs.getInt(1));
            }
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        return exists;
    }
    */
    private void addGuests() {
        try {
            PreparedStatement p = null;
            String newRowSql = "INSERT INTO `guest_party`(`party_id`, `guest_id`) VALUES (?,?)";
            p = con.prepareStatement(newRowSql);
            con.setAutoCommit(false);

            for (String guestId : guestIdList) {
                p.setInt(1, Integer.parseInt(selectedParty.get("partyId").toString()));
                p.setInt(2, Integer.parseInt(guestId));
                p.addBatch();
            }

            int[] updatedRows = p.executeBatch();
            con.commit();
            con.setAutoCommit(true);

            if (updatedRows.length > 0)
                JOptionPane.showMessageDialog(null, updatedRows.length + " Records Added Successfully!");
            else
                JOptionPane.showMessageDialog(null, "Error! Unable to add.");
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
    }
    
    public static HashMap getSelectedParty() {
        return selectedParty;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPartyMembers = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnRemove = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tblPartyMembers.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblPartyMembers.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        tblPartyMembers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "First Name", "Middle Name", "Surname", "National ID Number", "Passport Number"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblPartyMembers.setRowHeight(25);
        tblPartyMembers.setSelectionBackground(new java.awt.Color(105, 205, 130));
        tblPartyMembers.setShowVerticalLines(false);
        tblPartyMembers.getTableHeader().setReorderingAllowed(false);
        tblPartyMembers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPartyMembersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPartyMembers);

        jLabel2.setFont(new java.awt.Font("Corbel", 0, 24)); // NOI18N
        jLabel2.setText("Guests:");

        txtPartyName.setEditable(false);
        txtPartyName.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtPartyName.setToolTipText("Search Guest...");

        jLabel1.setFont(new java.awt.Font("Corbel", 1, 24)); // NOI18N
        jLabel1.setText("Party Name");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 886, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(txtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE))
        );

        btnRemove.setBackground(new java.awt.Color(80, 170, 195));
        btnRemove.setFont(new java.awt.Font("Corbel", 1, 22)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setText("Remove");
        btnRemove.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(80, 170, 195));
        btnAdd.setFont(new java.awt.Font("Corbel", 1, 22)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Corbel", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(60, 60, 60));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Options");

        btnSave.setBackground(new java.awt.Color(105, 205, 130));
        btnSave.setFont(new java.awt.Font("Corbel", 1, 22)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save & Exit");
        btnSave.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(245, 108, 108));
        btnCancel.setFont(new java.awt.Font("Corbel", 1, 22)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRemove, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName("Manage Party Form");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        int selectedRecordId = getSelectedRecordId();

        switch (action) {
            case "new":
                removeSelectedRecord(selectedRecordId);
                break;
            case "edit":
                deleteGuestFromParty(selectedRecordId);
                getGuestRecords(Integer.parseInt(selectedParty.get("partyId").toString()));
                displayGuestDetails();
        }
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        ReservationForm resForm = new ReservationForm((Frame) this.getOwner().getParent(), true, "party");
        resForm.setTitle("Guest Details");
        resForm.setLocationRelativeTo(resForm.getParent());
        resForm.setVisible(true);
        resForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        resForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if (guestRegistered == 1 || guestConfirmValue == 0) {
                    registeredGuest = selectedGuest;
                    //System.out.println("(MPF) Guest ID List: " + guestIdList);
                    if (guestIdList.isEmpty() || !guestIdList.contains(String.valueOf(registeredGuest.getId())) ) {
                        guestIdList.add(String.valueOf(registeredGuest.getId()));
                        displayGuestDetails(registeredGuest);
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Selected Guest Already Added.");
                }
            }
        });
    }//GEN-LAST:event_btnAddActionPerformed

    private void tblPartyMembersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPartyMembersMouseClicked
        btnRemove.setEnabled(true);
        btnRemove.setBackground(new Color(157, 55, 55));
    }//GEN-LAST:event_tblPartyMembersMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (!guestIdList.isEmpty()) {
                switch (action) {
                case "new":
                    createParty();
                    addGuests();
                    break;
                case "edit":
                    addGuests();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "No Records To Add.");
        this.dispose();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
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
            java.util.logging.Logger.getLogger(ManagePartyForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagePartyForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagePartyForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagePartyForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ManagePartyForm dialog = new ManagePartyForm(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblPartyMembers;
    private javax.swing.JTextField txtPartyName;
    // End of variables declaration//GEN-END:variables
}
