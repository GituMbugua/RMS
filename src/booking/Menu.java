/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author Gitu
 */
public class Menu extends javax.swing.JFrame {
//    private GridBagLayout layout = new GridBagLayout();
    private BorderLayout layout = new BorderLayout();
    private GridBagConstraints c = new GridBagConstraints();
    private Dimension size;
    
    static DashboardPanel dashPanel;
    static ReservationDashPanel resPanel;
    static GuestDashboardPanel guestPanel;
    static RoomDashboardPanel roomPanel;
    static AdminSettingsPanel adminPanel;
    
    /**
     * Creates new form Menu
     */
    public Menu() {
        
        initComponents();
        
        dashPanel = new DashboardPanel();
        resPanel = new ReservationDashPanel();
        guestPanel = new GuestDashboardPanel();
        roomPanel = new RoomDashboardPanel();
        adminPanel = new AdminSettingsPanel();
        
        customInit();
        
        addPanel(dashPanel);
    }

    private void customInit() {
        JButton[] btns = {btnDashboard, btnReservations, btnGuestManagement, btnRoomManagement, btnAdminSettings, btnLogout};
        Color pressedColor = new Color(157, 80, 80);
        Color rolloverColor = new Color(157, 55, 55);
        Color normalColor = new Color(35, 67, 67);
        for (JButton btn : btns) {
            btn.setBackground(new Color(35, 67, 67));
            btn.setUI(new BasicButtonUI());
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.getModel().addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent ce) {
                    if (btn.getModel().isPressed()) {
                        btn.setBackground(pressedColor);
                    } else if (btn.getModel().isRollover()) {
                        btn.setBackground(rolloverColor);
                    } else {
                        btn.setBackground(normalColor);
                    }
                }
            });
        }
        
        menuDynamicPanel.setLayout(layout);
//        c.gridx = 0;
//        c.gridy = 0;
//        c.weightx = 1.0;
//        c.weighty = 1.0;
        this.size = menuDynamicPanel.getSize();
        dashPanel.setSize(size);
    }
    
    private void addPanel(JPanel panel) {
        menuDynamicPanel.removeAll();
        menuDynamicPanel.add(panel);
        menuDynamicPanel.revalidate();
        menuDynamicPanel.repaint();
    }
    
    private void resizePanels() {
        dashPanel.setPreferredSize(menuDynamicPanel.getSize());
        resPanel.setPreferredSize(menuDynamicPanel.getSize());
        guestPanel.setPreferredSize(menuDynamicPanel.getSize());
        roomPanel.setPreferredSize(menuDynamicPanel.getSize());
        adminPanel.setPreferredSize(menuDynamicPanel.getSize());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnGuestManagement = new javax.swing.JButton();
        btnRoomManagement = new javax.swing.JButton();
        btnReservations = new javax.swing.JButton();
        btnDashboard = new javax.swing.JButton();
        btnAdminSettings = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        menuDynamicPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground(new java.awt.Color(29, 55, 55));
        jPanel3.setForeground(new java.awt.Color(250, 250, 250));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(250, 250, 250));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Rondo Retreat Center");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1236, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1190;
        gridBagConstraints.ipady = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        getContentPane().add(jPanel3, gridBagConstraints);

        jPanel1.setBackground(new java.awt.Color(35, 67, 67));

        btnGuestManagement.setBackground(new java.awt.Color(35, 67, 67));
        btnGuestManagement.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnGuestManagement.setForeground(new java.awt.Color(240, 240, 240));
        btnGuestManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_traveler_32px.png"))); // NOI18N
        btnGuestManagement.setText("Guest Managment");
        btnGuestManagement.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 4, 20));
        btnGuestManagement.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnGuestManagement.setIconTextGap(30);
        btnGuestManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuestManagementActionPerformed(evt);
            }
        });

        btnRoomManagement.setBackground(new java.awt.Color(35, 67, 67));
        btnRoomManagement.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnRoomManagement.setForeground(new java.awt.Color(240, 240, 240));
        btnRoomManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_room_32px_1.png"))); // NOI18N
        btnRoomManagement.setText("Room Management");
        btnRoomManagement.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 4, 20));
        btnRoomManagement.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnRoomManagement.setIconTextGap(30);
        btnRoomManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRoomManagementActionPerformed(evt);
            }
        });

        btnReservations.setBackground(new java.awt.Color(35, 67, 67));
        btnReservations.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnReservations.setForeground(new java.awt.Color(240, 240, 240));
        btnReservations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_reservation_32px_2.png"))); // NOI18N
        btnReservations.setText("Reservations");
        btnReservations.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 4, 20));
        btnReservations.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnReservations.setIconTextGap(30);
        btnReservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservationsActionPerformed(evt);
            }
        });

        btnDashboard.setBackground(new java.awt.Color(35, 67, 67));
        btnDashboard.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(240, 240, 240));
        btnDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_home_32px_2.png"))); // NOI18N
        btnDashboard.setText("Dashboard");
        btnDashboard.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 4, 20));
        btnDashboard.setHideActionText(true);
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDashboard.setIconTextGap(30);
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });

        btnAdminSettings.setBackground(new java.awt.Color(35, 67, 67));
        btnAdminSettings.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnAdminSettings.setForeground(new java.awt.Color(240, 240, 240));
        btnAdminSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_settings_32px_1.png"))); // NOI18N
        btnAdminSettings.setText("Admin Settings");
        btnAdminSettings.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 4, 20));
        btnAdminSettings.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnAdminSettings.setIconTextGap(30);
        btnAdminSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminSettingsActionPerformed(evt);
            }
        });

        btnLogout.setBackground(new java.awt.Color(35, 67, 67));
        btnLogout.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(240, 240, 240));
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_exit_32px.png"))); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 4, 20));
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogout.setIconTextGap(30);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnReservations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGuestManagement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnRoomManagement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnAdminSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnLogout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReservations, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuestManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRoomManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdminSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 16;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel1, gridBagConstraints);

        menuDynamicPanel.setBackground(new java.awt.Color(255, 255, 255));
        menuDynamicPanel.setMinimumSize(new java.awt.Dimension(1237, 455));
        menuDynamicPanel.setPreferredSize(new java.awt.Dimension(1237, 455));
        menuDynamicPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                menuDynamicPanelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout menuDynamicPanelLayout = new javax.swing.GroupLayout(menuDynamicPanel);
        menuDynamicPanel.setLayout(menuDynamicPanelLayout);
        menuDynamicPanelLayout.setHorizontalGroup(
            menuDynamicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1271, Short.MAX_VALUE)
        );
        menuDynamicPanelLayout.setVerticalGroup(
            menuDynamicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        getContentPane().add(menuDynamicPanel, gridBagConstraints);
        menuDynamicPanel.getAccessibleContext().setAccessibleName("MenuDynamicPanel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        addPanel(dashPanel);
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservationsActionPerformed
        addPanel(resPanel);
    }//GEN-LAST:event_btnReservationsActionPerformed

    private void btnGuestManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuestManagementActionPerformed
        addPanel(guestPanel);
    }//GEN-LAST:event_btnGuestManagementActionPerformed

    private void btnRoomManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRoomManagementActionPerformed
        addPanel(roomPanel);
    }//GEN-LAST:event_btnRoomManagementActionPerformed

    private void btnAdminSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminSettingsActionPerformed
        addPanel(adminPanel);
    }//GEN-LAST:event_btnAdminSettingsActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        this.dispose();
        LoginForm login = new LoginForm();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void menuDynamicPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_menuDynamicPanelComponentResized
        resizePanels();
    }//GEN-LAST:event_menuDynamicPanelComponentResized

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
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Menu menu = new Menu();
                menu.setTitle("Rondo Retreat Center Management System");
                menu.setVisible(true);
                menu.setExtendedState(Menu.MAXIMIZED_BOTH);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdminSettings;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnGuestManagement;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnReservations;
    private javax.swing.JButton btnRoomManagement;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel menuDynamicPanel;
    // End of variables declaration//GEN-END:variables
}
