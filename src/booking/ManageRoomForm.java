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
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Gitu
 */
public class ManageRoomForm extends javax.swing.JFrame {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;
    private Statement st;
    
    /**
     * Creates new form AddRoomForm
     */
    public ManageRoomForm() {
        initComponents();
        displayRoomRecords();
        customInit();
    }
    
    private void customInit() {
        btnEdit.setEnabled(false);
        tblRoomRecords.getTableHeader().setFont(new Font("Corbel", Font.PLAIN, 16));
    }
    
    private void enableEditing() {
        String userRole = currentUser.getRole().toLowerCase();
        switch (userRole) {
            case "admin":
            case "general":
                txtRoomName.setEditable(true);
                numOfBedsCombo.setEnabled(true);
                cottageNameCombo.setEnabled(true);
                break;
            case "audit":
                txtRoomName.setEditable(false);
                numOfBedsCombo.setEnabled(false);
                cottageNameCombo.setEnabled(false);
                break;
        }
    }
    private void clearTextboxes() {
        txtRoomId.setText(null);
        txtRoomName.setText(null);
        
        String userRole = currentUser.getRole().toLowerCase();
        switch (userRole) {
            case "admin":
            case "general":
                btnEdit.setEnabled(false);
                btnEdit.setBackground(new Color(240, 240, 240));
                btnAdd.setEnabled(true);
                btnAdd.setBackground(new Color(105, 205, 130));
                break;
            case "audit":
                btnEdit.setEnabled(false);
                btnEdit.setBackground(new Color(240, 240, 240));
                btnAdd.setEnabled(false);
                break;
        }
    }
    
    // ********** get cottage names *********** 
    private ArrayList<Cottage> getCottages() {  
        ArrayList<Cottage> cottageList = new ArrayList();
       
        try {
            String query = "SELECT * FROM cottage";
            st = con.createStatement();
            rs = st.executeQuery(query);

            Cottage cottage;
            while (rs.next()) {
               cottage = new Cottage(rs.getInt("cottage_id"), rs.getString("cottage_name"), rs.getInt("num_of_rooms"), 
                       rs.getBoolean("is_available"));
               cottageList.add(cottage);
           }
        }
       catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return cottageList;
    }
    
    private ArrayList<String> getCottageNames() {  
        ArrayList<Cottage> list = getCottages();
        ArrayList<String> cottageList = new ArrayList();
        HashSet hs = new HashSet();
        
        try {
            for (int i=0; i<list.size(); i++) {
                cottageList.add(list.get(i).getCottageName());
            }
            hs.addAll(cottageList);
            cottageList.clear();
            cottageList.addAll(hs);
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot get type. " + ex);
        }
        
        return cottageList;
    }
    // ********** end get cottage names *********** 
    
    // ********** get room records *********** 
    private ArrayList<HashMap> getRooms() {  
        ArrayList<HashMap> roomList = new ArrayList();
       
        try {
            String query = "SELECT room.*, cottage.cottage_name FROM `room` LEFT JOIN cottage ON room.cottage_id=cottage.cottage_id";
            st = con.createStatement();
            rs = st.executeQuery(query);

            HashMap<Object, Object> room;
            while (rs.next()) {
                int i = 1;
                room = new HashMap<>();
                
                room.put("roomId", rs.getInt(i++));
                room.put("roomName", rs.getString(i++));
                room.put("numOfBeds", rs.getInt(i++));
                room.put("isAvailable", rs.getBoolean(i++));
                room.put("cottageId", rs.getInt(i++));
                room.put("cottageName", rs.getString(i++));
                
                roomList.add(room);
           }
        }
       catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return roomList;
    }
    
    private void displayRoomRecords() {
        ArrayList<HashMap> list = getRooms();
        DefaultTableModel model = (DefaultTableModel)tblRoomRecords.getModel();
        if (!list.isEmpty()) {
            model.setRowCount(0);
            Object[] col = new Object[5];
            for (int i=0; i<list.size(); i++) {
                col[0] = list.get(i).get("roomId");
                col[1] = list.get(i).get("roomName");
                col[2] = list.get(i).get("numOfBeds");
                col[3] = list.get(i).get("isAvailable");
                col[4] = list.get(i).get("cottageName");

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
    // ********** get room records *********** 
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leftPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtRoomId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtRoomName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        numOfBedsCombo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        cottageNameCombo = new javax.swing.JComboBox();
        btnClear = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        isAvailableCombo = new javax.swing.JComboBox();
        rightPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRoomRecords = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 655));

        leftPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel1.setText("Room ID");

        txtRoomId.setEditable(false);
        txtRoomId.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtRoomId.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel2.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel2.setText("Room Name");

        txtRoomName.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtRoomName.setMargin(new java.awt.Insets(2, 5, 2, 5));
        txtRoomName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRoomNameKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel3.setText("Number of Beds");

        numOfBedsCombo.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        numOfBedsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));

        jLabel5.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel5.setText("Cottage Name");

        cottageNameCombo.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        cottageNameCombo.setModel(new javax.swing.DefaultComboBoxModel(getCottageNames().toArray()));

        btnClear.setBackground(new java.awt.Color(120, 120, 120));
        btnClear.setFont(new java.awt.Font("Corbel", 0, 22)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Clear");
        btnClear.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Corbel", 0, 22)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(105, 205, 130));
        btnAdd.setFont(new java.awt.Font("Corbel", 0, 22)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel6.setText("Availability");

        isAvailableCombo.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        isAvailableCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "true", "false" }));

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(isAvailableCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(leftPanelLayout.createSequentialGroup()
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(leftPanelLayout.createSequentialGroup()
                            .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(53, 53, 53)
                            .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtRoomId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtRoomName)
                                .addComponent(numOfBedsCombo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(leftPanelLayout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(53, 53, 53)
                            .addComponent(cottageNameCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRoomId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numOfBedsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(isAvailableCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cottageNameCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(77, 77, 77)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(112, Short.MAX_VALUE))
        );

        rightPanel.setBackground(new java.awt.Color(35, 67, 67));

        tblRoomRecords.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        tblRoomRecords.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Room ID", "Room Name", "Number of Beds", "Availability", "Cottage Name"
            }
        ));
        tblRoomRecords.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblRoomRecords.setRowHeight(25);
        tblRoomRecords.setSelectionBackground(new java.awt.Color(105, 205, 130));
        tblRoomRecords.setShowVerticalLines(false);
        tblRoomRecords.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRoomRecordsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblRoomRecords);

        jLabel4.setFont(new java.awt.Font("Corbel", 1, 32)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Rooms List");

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(rightPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblRoomRecordsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRoomRecordsMouseClicked
        enableEditing();
        DefaultTableModel model = (DefaultTableModel)tblRoomRecords.getModel();
        int selectedRowIndex = tblRoomRecords.getSelectedRow();
        String roomId = null, roomName = null, numOfBeds = null, cottageName = null, isAvailable = null;
        int column = 0;
        
        
        roomId = model.getValueAt(selectedRowIndex, column).toString();
        roomName = model.getValueAt(selectedRowIndex, column+1).toString();
        numOfBeds = model.getValueAt(selectedRowIndex, column+2).toString(); 
        isAvailable = model.getValueAt(selectedRowIndex, column+3).toString(); 
        cottageName = model.getValueAt(selectedRowIndex, column+4).toString(); 
        txtRoomId.setText(roomId);
        txtRoomName.setText(roomName);
        numOfBedsCombo.setSelectedItem(numOfBeds);
        isAvailableCombo.setSelectedItem(isAvailable);
        cottageNameCombo.setSelectedItem(cottageName);
        btnAdd.setEnabled(false);
        btnAdd.setBackground(new Color(240, 240, 240));
        btnEdit.setEnabled(true);
        btnEdit.setBackground(new Color(80, 170, 195));
    }//GEN-LAST:event_tblRoomRecordsMouseClicked

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearTextboxes();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int roomId = Integer.parseInt(txtRoomId.getText());
        String roomName = txtRoomName.getText();
        int numOfBeds = Integer.parseInt(numOfBedsCombo.getSelectedItem().toString());
        boolean isAvailable = Boolean.parseBoolean(isAvailableCombo.getSelectedItem().toString());
        String cottageName = cottageNameCombo.getSelectedItem().toString();
        int cottageId = 0;
        try {
            String query = "SELECT `cottage_id` FROM `cottage` WHERE cottage_name='" + cottageName + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
               cottageId = rs.getInt("cottage_id");
            }
        }
        catch (Exception ex) {
            System.out.println("Error. " + ex);
            JOptionPane.showMessageDialog(null, "Error!" + ex);
        }
        
        if (roomId != 0 && cottageId != 0) {
            try {
                PreparedStatement p = con.prepareStatement("UPDATE `room` SET room_name=?, num_of_beds=?, is_available=?, cottage_id=? WHERE room_id=?");
                p.setString(1, roomName);
                p.setInt(2, numOfBeds);
                p.setBoolean(3, isAvailable);
                p.setInt(4, cottageId);
                p.setInt(5, roomId);
                
                int updatedRows = p.executeUpdate();
                if (updatedRows > 0)
                    JOptionPane.showMessageDialog(null, "Edit Successful!");
                else
                    JOptionPane.showMessageDialog(null, "Error! Unable to update.");
            }
            catch (Exception ex) {
                System.out.println("Error. " + ex);
                JOptionPane.showMessageDialog(null, "Error!" + ex);
            }
            displayRoomRecords();
            clearTextboxes();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String roomName, cottageName;
        int numOfBeds, cottageId = 0;

        roomName = txtRoomName.getText();
        numOfBeds = Integer.parseInt(numOfBedsCombo.getSelectedItem().toString());
        cottageName = cottageNameCombo.getSelectedItem().toString();
        try {
            String query = "SELECT `cottage_id` FROM `cottage` WHERE cottage_name='" + cottageName + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
               cottageId = rs.getInt("cottage_id");
            }
        }
        catch (Exception ex) {
            System.out.println("Error. " + ex);
            JOptionPane.showMessageDialog(null, "Error!" + ex);
        }
        if (roomName != null && cottageId != 0) {
            try {
                PreparedStatement p;

                String newSqlRow = "INSERT INTO `room`(`room_name`, `num_of_beds`, `cottage_id`) VALUES (?,?,?)";
                p = con.prepareStatement(newSqlRow);
                p.setString(1, roomName);
                p.setInt(2, numOfBeds);
                p.setInt(3, cottageId);
                int updatedRows = p.executeUpdate();

                if (updatedRows > 0)
                    JOptionPane.showMessageDialog(null, "Record Saved Successfully!");
                else
                    JOptionPane.showMessageDialog(null, "Error! Unable to add.");
            }
            catch (Exception ex) {
                System.out.println("Error. " + ex);
                JOptionPane.showMessageDialog(null, "Error!" + ex);
            }
        }
        displayRoomRecords();
        clearTextboxes();
    }//GEN-LAST:event_btnAddActionPerformed

    private void txtRoomNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRoomNameKeyPressed
        String userRole = currentUser.getRole().toLowerCase();
        switch (userRole) {
            case "admin":
            case "general":
                btnAdd.setEnabled(true);
                btnAdd.setBackground(new Color(105, 205, 130));
                break;
            case "audit":
                break;
        }
    }//GEN-LAST:event_txtRoomNameKeyPressed

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
            java.util.logging.Logger.getLogger(ManageRoomForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageRoomForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageRoomForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageRoomForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageRoomForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JComboBox cottageNameCombo;
    private javax.swing.JComboBox isAvailableCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JComboBox numOfBedsCombo;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTable tblRoomRecords;
    private javax.swing.JTextField txtRoomId;
    private javax.swing.JTextField txtRoomName;
    // End of variables declaration//GEN-END:variables
}
