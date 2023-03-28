/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gitu
 */
public class AdditionalCostsForm extends javax.swing.JDialog {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;
    private String selectedGuestName;
    private int reservationId;

    /**
     * Creates new form AdditionalCostsForm
     */
    public AdditionalCostsForm(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    public AdditionalCostsForm(java.awt.Dialog parent, boolean modal, String selectedGuestName, int reservationId) {
        super(parent, modal);
        initComponents();

        this.selectedGuestName = selectedGuestName;
        this.reservationId = reservationId;
        setValues();
        displayRecords();
        btnEdit.setEnabled(false);
    }

    private void clearInputs() {
        txtId.setText(null);
        txtCharges.setText(null);
        categoryCombo.setSelectedIndex(0);
        btnEdit.setEnabled(false);
        btnAdd.setEnabled(true);
    }
    
    private void setValues() {
        txtGuestName.setText(selectedGuestName);
    }
    
    private HashMap<Object, Object> getSelected() {
        DefaultTableModel model = (DefaultTableModel) tblAdditionalCosts.getModel();
        int selectedRowIndex = tblAdditionalCosts.getSelectedRow();
        HashMap<Object, Object> selected = null;
        
        clearInputs();
        if (tblAdditionalCosts.getSelectedRowCount() != 0) {

            int id = Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString());
            String category = model.getValueAt(selectedRowIndex, 2).toString();
            String charges = model.getValueAt(selectedRowIndex, 3).toString();
            
            selected = new HashMap<>();
            selected.put("id", id);
            selected.put("category", category);
            selected.put("charges", charges);
            
        } else {
            JOptionPane opt = new JOptionPane("No selected records to edit.", JOptionPane.ERROR_MESSAGE);
            final JDialog jdg = opt.createDialog("Error");
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
        return selected;
    }
    
    private ArrayList<HashMap> getAdditionalCosts() {
        ArrayList<HashMap> costsList = new ArrayList();
        try {
            Statement st;
        
            String query = "SELECT `id`, `reservation_id`, `date`, `category`, `charges` FROM `additional_costs` WHERE reservation_id='" + reservationId + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            HashMap<Object, Object> cost;
            
            while (rs.next()) {
                int i = 1;
                cost = new HashMap<>();
                
                cost.put("id", rs.getInt(i++));
                cost.put("reservation_id", rs.getInt(i++));
                cost.put("date", rs.getString(i++));
                cost.put("category", rs.getString(i++));
                cost.put("charges", rs.getDouble(i++));

                costsList.add(cost);
            }
        }
        catch (Exception ex) {
            System.out.println("Error. " + ex);
        }
        return costsList;
    }
    
    private void displayRecords() {
        ArrayList<HashMap> list = getAdditionalCosts();
        DefaultTableModel model = (DefaultTableModel)tblAdditionalCosts.getModel();
        if (list.size() != 0) {
            model.setRowCount(0);
            Object[] col = new Object[6];
            for (int i=0; i<list.size(); i++) {
                col[0] = list.get(i).get("id");
                col[1] = list.get(i).get("date");
                col[2] = list.get(i).get("category");
                col[3] = list.get(i).get("charges");

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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtGuestName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        categoryCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtCharges = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAdditionalCosts = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel1.setText("Additional Costs for:");

        txtGuestName.setEditable(false);
        txtGuestName.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel6.setText("ID");

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel3.setText("Category");

        categoryCombo.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        categoryCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Laundry", "Staff Tip", "Walk", "Penalty" }));
        categoryCombo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel2.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel2.setText("Charges ");

        txtCharges.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

        btnClear.setFont(new java.awt.Font("Corbel", 0, 20)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(80, 170, 195));
        btnEdit.setFont(new java.awt.Font("Corbel", 0, 20)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(105, 205, 130));
        btnAdd.setFont(new java.awt.Font("Corbel", 0, 20)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtGuestName, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(categoryCombo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCharges, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(112, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGuestName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(110, 110, 110)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(35, 67, 67));

        tblAdditionalCosts.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        tblAdditionalCosts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "Category", "Charges"
            }
        ));
        tblAdditionalCosts.setRowHeight(25);
        tblAdditionalCosts.setShowVerticalLines(false);
        tblAdditionalCosts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAdditionalCostsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAdditionalCosts);
        if (tblAdditionalCosts.getColumnModel().getColumnCount() > 0) {
            tblAdditionalCosts.getColumnModel().getColumn(0).setPreferredWidth(15);
        }

        jLabel4.setFont(new java.awt.Font("Corbel", 1, 30)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Additional Cost List");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE))
                .addGap(51, 51, 51))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearInputs();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        int id = Integer.parseInt(txtId.getText());
        String category = categoryCombo.getSelectedItem().toString();
        double charges = Double.parseDouble(txtCharges.getText());
        
        if (id != 0) {
            try {
                //myConnection();
                PreparedStatement p = con.prepareStatement("UPDATE `additional_costs` SET category=?, charges=? WHERE id=?");
                
                p.setString(1, category);
                p.setDouble(2, charges);
                p.setInt(3, id);
                
                int updatedRows = p.executeUpdate();
                if (updatedRows > 0)
                    JOptionPane.showMessageDialog(null, "Edit Successful!");
                else
                    JOptionPane.showMessageDialog(null, "Error! Unable to update.");
                //con.close();
            }
            catch (Exception ex) {
                System.out.println("Error. " + ex);
                JOptionPane.showMessageDialog(null, "Error!" + ex);
            }
            displayRecords();
            clearInputs();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void tblAdditionalCostsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAdditionalCostsMouseClicked
        // TODO add your handling code here:
        HashMap<Object, Object> selected = getSelected();
        
        txtId.setText(selected.get("id").toString());
        categoryCombo.setSelectedItem(selected.get("category").toString());
        txtCharges.setText(selected.get("charges").toString());
        btnEdit.setEnabled(true);
        btnAdd.setEnabled(false);
    }//GEN-LAST:event_tblAdditionalCostsMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        txtId.setText(null);
        try {
            //myConnection();
            PreparedStatement p;
            String category = categoryCombo.getSelectedItem().toString();
            double charges = Double.parseDouble(txtCharges.getText());


            String newSqlRow = "INSERT INTO `additional_costs`(`reservation_id`, `category`, `charges`) VALUES (?,?,?)";
            p = con.prepareStatement(newSqlRow);
            p.setInt(1, reservationId);
            p.setString(2, category);
            p.setDouble(3, charges);
           
            int updatedRows = p.executeUpdate();

            if (updatedRows > 0)
                JOptionPane.showMessageDialog(null, "Record Saved Successfully!");
            else
                JOptionPane.showMessageDialog(null, "Error! Unable to add.");
            //con.close();
        }
        catch (Exception ex) {
            System.out.println("Error. " + ex);
            JOptionPane.showMessageDialog(null, "Error!" + ex);
        }
        displayRecords();
        clearInputs();
    }//GEN-LAST:event_btnAddActionPerformed

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
            java.util.logging.Logger.getLogger(AdditionalCostsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdditionalCostsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdditionalCostsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdditionalCostsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AdditionalCostsForm acForm = new AdditionalCostsForm(new javax.swing.JDialog(), true);
                //acForm.setVisible(true);
                acForm.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                acForm.setTitle("Additional Costs Management");
                acForm.setVisible(true);
                acForm.setLocationRelativeTo(null);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JComboBox categoryCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAdditionalCosts;
    private javax.swing.JTextField txtCharges;
    private javax.swing.JTextField txtGuestName;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
}
