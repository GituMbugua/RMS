/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import static booking.Guest.currentGuest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author Gitu
 */
public class GuestDetailsForm extends javax.swing.JDialog {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;
    static int guestRegistered  = 0;
    private Guest guestDetails;
    private String action;
    /**
     * Creates new form RegisterGuestForm
     */
    public GuestDetailsForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        action = "new";
    }
    public GuestDetailsForm(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        action = "new";
    }
    public GuestDetailsForm(java.awt.Frame parent, boolean modal, Guest guestDetails) {
        super(parent, modal);
        initComponents();
        
        this.guestDetails = guestDetails;
        setDetails();
        action = "edit";
    }
    
    private ArrayList<String> generateDays() {
        ArrayList<String> days_tmp = new ArrayList<>();
        for (int days = 1; days<=31; days++) {
            days_tmp.add(days+"");
        }
        return days_tmp;
    }
    
    private ArrayList<String> generateYears() {
        ArrayList<String> years_tmp = new ArrayList<>();
        for (int yrs = 1950; yrs<=Calendar.getInstance().get(Calendar.YEAR); yrs++) {
            years_tmp.add(yrs+"");
        }
        return years_tmp;
    }
    
    private String getDob() throws ParseException {
        String day, month, year;
        day = (String) cmbDobDay.getSelectedItem();
        month = String.valueOf(cmbDobMonth.getSelectedIndex()+1);
        year = (String) cmbDobYear.getSelectedItem();
        String dobString = year+ "-" + month+ "-" + day;
  
        return dobString;
    } 
    private void setDob(String date) {
        String[] arrOfStr = date.split("-", -2); 
  
        cmbDobDay.setSelectedItem(arrOfStr[2]);
        cmbDobMonth.setSelectedItem(arrOfStr[1]);
        cmbDobYear.setSelectedItem(arrOfStr[0]);
    }
    private void setDetails() {
        txtFname.setText(guestDetails.getFirstName());
        txtMname.setText(guestDetails.getMiddleName());
        txtSname.setText(guestDetails.getSurname());
        setDob(guestDetails.getDob());
        txtPhoneNumber.setText(guestDetails.getPhoneNumber());
        txtAddress.setText(guestDetails.getAddress());
        txtEmail.setText(guestDetails.getEmail());
        txtNationalId.setText(guestDetails.getIdNum());
        txtPassport.setText(guestDetails.getPassportNum());
        txtNationality.setText(guestDetails.getNationality());
        txtCOR.setText(guestDetails.getCountryofResidence());
        txtOccupation.setText(guestDetails.getOccupation());
        txtHealth.setText(guestDetails.getHealth());
        txtPreferredRoom.setText(guestDetails.getPreferredRoom());
    }
    
    private void registerNewGuest() {
        try {
            //myConnection();
            PreparedStatement p;
            String firstName, middleName, surname, dateOfBirth, phoneNumber, address, email, idNum, passportNum, nationality, countryOfResidence, occupation, healthConsiderations, preferredRoom;

            // take all input
            firstName = txtFname.getText();
            middleName = txtMname.getText();
            surname = txtSname.getText();
            dateOfBirth = getDob();
            phoneNumber = txtPhoneNumber.getText();
            address = txtAddress.getText();
            email = txtEmail.getText();
            idNum = txtNationalId.getText();
            passportNum = txtPassport.getText();
            nationality = txtNationality.getText();
            countryOfResidence = txtCOR.getText();
            occupation = txtOccupation.getText();
            healthConsiderations = txtHealth.getText();
            preferredRoom = txtPreferredRoom.getText();
            
            String newRowSql = "INSERT INTO `guest`(`first_name`, `middle_name`, `surname`, `date_of_birth`, `phone_number`, `address`, `email`, `national_id_num`, `passport_num`, `nationality`, `country_of_residence`, `occupation`, `health_considerations`, `preferred_room`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            p = con.prepareStatement(newRowSql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, firstName);
            p.setString(2, middleName);
            p.setString(3, surname);
            p.setString(4, dateOfBirth);
            p.setString(5, phoneNumber);
            p.setString(6, address);
            p.setString(7, email);
            p.setString(8, idNum);
            p.setString(9, passportNum);
            p.setString(10, nationality);
            p.setString(11, countryOfResidence);
            p.setString(12, occupation);
            p.setString(13, healthConsiderations);
            p.setString(14, preferredRoom);

            int updatedRows = p.executeUpdate();
            
            rs = p.getGeneratedKeys();
            if (rs != null && rs.next() ) {
                currentGuest.setId(rs.getInt(1));
            } else {
                System.out.println("Cannot get result set.");
            }
            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(null, "Record Saved Successfully!");
                
                currentGuest.setFirstName(firstName);
                currentGuest.setMiddleName(middleName);
                currentGuest.setSurname(surname);
                currentGuest.setDob(dateOfBirth);
                currentGuest.setPhoneNumber(phoneNumber);
                currentGuest.setAddress(address);
                currentGuest.setEmail(email);
                currentGuest.setIdNum(idNum);
                currentGuest.setPassportNum(passportNum);

                guestRegistered = 1;
                
            }
            else
                JOptionPane.showMessageDialog(null, "Error! Unable to add.");
            
            this.dispose();
        } catch(Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error! " + ex.getMessage());
        }
    }
    
    private void editGuestDetails() {
        try {
            //myConnection();
            PreparedStatement p;
            int guestId;
            String firstName, middleName, surname, dateOfBirth, phoneNumber, address, email, idNum, passportNum, nationality, countryOfResidence, occupation, healthConsiderations, preferredRoom;

            // take all input
            guestId = guestDetails.getId();
            firstName = txtFname.getText();
            middleName = txtMname.getText();
            surname = txtSname.getText();
            dateOfBirth = getDob();
            phoneNumber = txtPhoneNumber.getText();
            address = txtAddress.getText();
            email = txtEmail.getText();
            idNum = txtNationalId.getText();
            passportNum = txtPassport.getText();
            nationality = txtNationality.getText();
            countryOfResidence = txtCOR.getText();
            occupation = txtOccupation.getText();
            healthConsiderations = txtHealth.getText();
            preferredRoom = txtPreferredRoom.getText();
            
            String newRowSql = "UPDATE `guest` SET `first_name`=?,`middle_name`=?,`surname`=?,`date_of_birth`=?,`phone_number`=?,`address`=?,`email`=?,`national_id_num`=?,`passport_num`=?,`nationality`=?,`country_of_residence`=?,`occupation`=?,`health_considerations`=?, `preferred_room`=? WHERE `guest_id`=?";

            p = con.prepareStatement(newRowSql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, firstName);
            p.setString(2, middleName);
            p.setString(3, surname);
            p.setString(4, dateOfBirth);
            p.setString(5, phoneNumber);
            p.setString(6, address);
            p.setString(7, email);
            p.setString(8, idNum);
            p.setString(9, passportNum);
            p.setString(10, nationality);
            p.setString(11, countryOfResidence);
            p.setString(12, occupation);
            p.setString(13, healthConsiderations);
            p.setString(14, preferredRoom);
            p.setInt(15, guestId);

            int updatedRows = p.executeUpdate();
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
                this.dispose();
                success.setVisible(true);
            }
            else
                JOptionPane.showMessageDialog(null, "Error! Unable to add record.");
        } 
        catch(Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error! " + ex.getMessage());
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

        jPanel2 = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFname = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMname = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSname = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbDobDay = new javax.swing.JComboBox();
        cmbDobMonth = new javax.swing.JComboBox();
        cmbDobYear = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        txtPhoneNumber = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtPassport = new javax.swing.JTextField();
        rightPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtNationalId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNationality = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtCOR = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtOccupation = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtHealth = new javax.swing.JTextArea();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jLabelPR = new javax.swing.JLabel();
        txtPreferredRoom = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1200, 655));

        leftPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel2.setText("First Name");

        txtFname.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtFname.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel3.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel3.setText("Middle Name");

        txtMname.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtMname.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel4.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel4.setText("Surname");

        txtSname.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtSname.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel7.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel7.setText("Date of Birth");

        cmbDobDay.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        cmbDobDay.setModel(new javax.swing.DefaultComboBoxModel( generateDays().toArray() ));

        cmbDobMonth.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        cmbDobMonth.setModel(new javax.swing.DefaultComboBoxModel(Month.values()));

        cmbDobYear.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        cmbDobYear.setModel(new javax.swing.DefaultComboBoxModel(generateYears().toArray()));

        jLabel8.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel8.setText("Phone Number");

        txtPhoneNumber.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtPhoneNumber.setMargin(new java.awt.Insets(2, 5, 2, 5));

        txtAddress.setColumns(20);
        txtAddress.setRows(5);
        txtAddress.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jScrollPane2.setViewportView(txtAddress);

        jLabel9.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel9.setText("Address");

        jLabel11.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel11.setText("Email");

        txtEmail.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtEmail.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel12.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel12.setText("Passport Number");

        txtPassport.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtPassport.setMargin(new java.awt.Insets(2, 5, 2, 5));

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPassport, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(txtFname, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(cmbDobDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(cmbDobMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(cmbDobYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52)
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSname, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMname, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(52, 52, 52)
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(149, Short.MAX_VALUE))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDobDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDobMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDobYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassport, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        rightPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel5.setText("National ID");

        txtNationalId.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtNationalId.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel6.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel6.setText("Nationality");

        txtNationality.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtNationality.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel13.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel13.setText("Country of Residence");

        txtCOR.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtCOR.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel14.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel14.setText("Occupation");

        txtOccupation.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtOccupation.setMargin(new java.awt.Insets(2, 5, 2, 5));

        jLabel10.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel10.setText("Health Considerations");

        txtHealth.setColumns(20);
        txtHealth.setRows(5);
        txtHealth.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jScrollPane1.setViewportView(txtHealth);

        btnCancel.setBackground(new java.awt.Color(245, 108, 108));
        btnCancel.setFont(new java.awt.Font("Corbel", 0, 20)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(105, 205, 130));
        btnSave.setFont(new java.awt.Font("Corbel", 0, 20)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jLabelPR.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabelPR.setText("Preferred Room");

        txtPreferredRoom.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtPreferredRoom.setMargin(new java.awt.Insets(2, 5, 2, 5));

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtOccupation, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rightPanelLayout.createSequentialGroup()
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNationality, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addComponent(txtCOR))))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNationalId, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addComponent(jLabelPR, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtPreferredRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNationalId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNationality, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCOR, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtOccupation, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPreferredRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPR, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(rightPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        switch (action) {
            case "new":
                registerNewGuest();
                break;
            case "edit":
                editGuestDetails();
        }
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
            java.util.logging.Logger.getLogger(GuestDetailsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuestDetailsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuestDetailsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuestDetailsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GuestDetailsForm registerGuest = new GuestDetailsForm(new javax.swing.JFrame(), true);
                registerGuest.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                registerGuest.setVisible(true);
                registerGuest.setLocationRelativeTo(null);
                registerGuest.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbDobDay;
    private javax.swing.JComboBox cmbDobMonth;
    private javax.swing.JComboBox cmbDobYear;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelPR;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JTextField txtCOR;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFname;
    private javax.swing.JTextArea txtHealth;
    private javax.swing.JTextField txtMname;
    private javax.swing.JTextField txtNationalId;
    private javax.swing.JTextField txtNationality;
    private javax.swing.JTextField txtOccupation;
    private javax.swing.JTextField txtPassport;
    private javax.swing.JTextField txtPhoneNumber;
    private javax.swing.JTextField txtPreferredRoom;
    private javax.swing.JTextField txtSname;
    // End of variables declaration//GEN-END:variables
}
