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
public class PaymentsForm extends javax.swing.JDialog {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;
    private String selectedGuestName;
    private int reservationId;
    /**
     * Creates new form PaymentsForm
     */
    public PaymentsForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public PaymentsForm(java.awt.Dialog parent, boolean modal, String selectedGuestName, int reservationId) {
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
        txtAmount.setText(null);
        btnEdit.setEnabled(false);
        btnAdd.setEnabled(true);
    }
    
    private void setValues() {
        txtGuestName.setText(selectedGuestName);
    }
    
    private HashMap<Object, Object> getSelected() {
        DefaultTableModel model = (DefaultTableModel) tblPayments.getModel();
        int selectedRowIndex = tblPayments.getSelectedRow();
        HashMap<Object, Object> selected = null;
        
        clearInputs();
        if (tblPayments.getSelectedRowCount() != 0) {

            int payment_id = Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString());
            String account = model.getValueAt(selectedRowIndex, 2).toString();
            String modeOfPayment = model.getValueAt(selectedRowIndex, 3).toString();
            String amount = model.getValueAt(selectedRowIndex, 4).toString();
            
            selected = new HashMap<>();
            selected.put("payment_id", payment_id);
            selected.put("account", account);
            selected.put("modeOfPayment", modeOfPayment);
            selected.put("amount", amount);
            
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
    
    private ArrayList<HashMap> getPayments() {
        ArrayList<HashMap> paymentsList = new ArrayList();
        try {
            Statement st;
        
            String query = "SELECT `payment_id`, `reservation_id`, `date`, `account`, `mode_of_payment`, `amount` FROM `payments` WHERE reservation_id='" + reservationId + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            HashMap<Object, Object> payment;
            
            while (rs.next()) {
                int i = 1;
                payment = new HashMap<>();
                
                payment.put("payment_id", rs.getInt(i++));
                payment.put("reservation_id", rs.getInt(i++));
                payment.put("date", rs.getString(i++));
                payment.put("account", rs.getString(i++));
                payment.put("modeOfPayment", rs.getString(i++));
                payment.put("amount", rs.getDouble(i++));

                paymentsList.add(payment);
            }
        }
        catch (Exception ex) {
            System.out.println("Error. " + ex);
        }
        return paymentsList;
    }
    
    private void displayRecords() {
        ArrayList<HashMap> list = getPayments();
        DefaultTableModel model = (DefaultTableModel) tblPayments.getModel();
        if (list.size() != 0) {
            model.setRowCount(0);
            Object[] col = new Object[6];
            for (int i=0; i<list.size(); i++) {
                col[0] = list.get(i).get("payment_id");
                col[1] = list.get(i).get("date");
                col[2] = list.get(i).get("account");
                col[3] = list.get(i).get("modeOfPayment");
                col[4] = list.get(i).get("amount");

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
    
    /*
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
        jLabel4 = new javax.swing.JLabel();
        accountCombo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        modeOfPaymentCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPayments = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel1.setText("Payments for:");

        txtGuestName.setEditable(false);
        txtGuestName.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel6.setText("ID");

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel4.setText("Account");

        accountCombo.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        accountCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M-Pesa", "TF Dollar", "TF KSH", "Rondo" }));

        jLabel5.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel5.setText("Mode of Payment");

        modeOfPaymentCombo.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        modeOfPaymentCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bank Transfer", "Cash", "Cheque", "M-Pesa" }));

        jLabel2.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel2.setText("Amount");

        txtAmount.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

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
                .addGap(119, 119, 119)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(accountCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(modeOfPaymentCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                            .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(39, 39, 39)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(33, 33, 33)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtGuestName, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGuestName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accountCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modeOfPaymentCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(77, 77, 77)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(35, 67, 67));

        tblPayments.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        tblPayments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "Account", "Mode of Payment", "Amount"
            }
        ));
        tblPayments.setRowHeight(25);
        tblPayments.setShowVerticalLines(false);
        tblPayments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPaymentsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPayments);
        if (tblPayments.getColumnModel().getColumnCount() > 0) {
            tblPayments.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        jLabel3.setFont(new java.awt.Font("Corbel", 1, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Payments List");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearInputs();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        int payment_id = Integer.parseInt(txtId.getText());
        String account = accountCombo.getSelectedItem().toString();
        String paymentMode = modeOfPaymentCombo.getSelectedItem().toString();
        double amount = Double.parseDouble(txtAmount.getText());

        if (payment_id != 0) {
            try {
                //myConnection();
                PreparedStatement p = con.prepareStatement("UPDATE `payments` SET account=?, mode_of_payment=?, amount=? WHERE payment_id=?");

                p.setString(1, account);
                p.setString(2, paymentMode);
                p.setDouble(3, amount);
                p.setInt(4, payment_id);

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

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        txtId.setText(null);
        try {
            PreparedStatement p;
            String account = accountCombo.getSelectedItem().toString();
            String modeOfPayment = modeOfPaymentCombo.getSelectedItem().toString();
            double amount = Double.parseDouble(txtAmount.getText());

            String newSqlRow = "INSERT INTO `payments`(`reservation_id`, `account`, `mode_of_payment`, `amount`) VALUES (?,?,?,?)";
            p = con.prepareStatement(newSqlRow);
            p.setInt(1, reservationId);
            p.setString(2, account);
            p.setString(3, modeOfPayment);
            p.setDouble(4, amount);
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
        displayRecords();
        clearInputs();
    }//GEN-LAST:event_btnAddActionPerformed

    private void tblPaymentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPaymentsMouseClicked
        HashMap<Object, Object> selected = getSelected();

        txtId.setText(selected.get("payment_id").toString());
        accountCombo.setSelectedItem(selected.get("account").toString());
        modeOfPaymentCombo.setSelectedItem(selected.get("modeOfPayment").toString());
        txtAmount.setText(selected.get("amount").toString());
        btnEdit.setEnabled(true);
        btnAdd.setEnabled(false);
    }//GEN-LAST:event_tblPaymentsMouseClicked

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
            java.util.logging.Logger.getLogger(PaymentsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PaymentsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PaymentsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PaymentsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PaymentsForm dialog = new PaymentsForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox accountCombo;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox modeOfPaymentCombo;
    private javax.swing.JTable tblPayments;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtGuestName;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
}
