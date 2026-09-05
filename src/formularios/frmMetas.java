/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package formularios;

import conexion.ConexionBD;
import clases.Usuario;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
/**
 *
 * @author win11
 */
public class frmMetas extends javax.swing.JFrame {
    
    private Usuario usuarioActual;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmMetas.class.getName());

    /**
     * Creates new form frmMetas
     */
    public frmMetas() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    }
    
    public frmMetas(Usuario usuario){
        this.usuarioActual = usuario;
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        cargarMetas();
    }
    
    public void cargarMetas(){
        if(this.usuarioActual == null){
            pnlListaMetas.revalidate();
            pnlListaMetas.repaint();
            return;
        }
        
        String sql = "select nombre_meta, monto_objetivo, monto_actual, fecha_limite from metas where id_usuario = ?";
        
        try(Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setInt(1, this.usuarioActual.getId());
            
            try(ResultSet rs = ps.executeQuery()){
                boolean hayMetas = false;
                
                while(rs.next()){
                    hayMetas = true;
                    String nombre = rs.getString("nombre_meta");
                    double objetivo = rs.getDouble("monto_objetivo");
                    double actual = rs.getDouble("monto_actual");
                    String fecha = rs.getString("fecha_limite");
                    
                    //crear tarjetas
                    JPanel card = new JPanel();
                    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                    card.setBackground(new Color (30, 41, 59));
                    card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 65, 85), 1),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
                    ));
                    
                    card.setMaximumSize(new Dimension(390, 100));
                    
                    //titulo de meta
                    JLabel lblNombre = new JLabel(nombre);
                    lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    lblNombre.setForeground(Color.WHITE);
                    lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
                    
                    int porcentaje = objetivo > 0 ?(int)((actual / objetivo) * 100): 0;
                    if (porcentaje > 100) porcentaje = 100;
                    
                    JLabel lblDetalles =  new JLabel(String.format("$%.2f de $%.2f (%d%%)", actual, objetivo, porcentaje));
                    lblDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    lblDetalles.setForeground(new Color(148,163,184));
                    lblDetalles.setAlignmentX(Component.LEFT_ALIGNMENT);
                    
                    //barra de progreso
                    JProgressBar progressBar = new JProgressBar(0, 100);
                    progressBar.setValue(porcentaje);
                    progressBar.setStringPainted(false);
                    progressBar.setForeground(new Color(16, 185, 129));
                    progressBar.setBackground(new Color(15, 23, 42));
                    progressBar.setBorderPainted(false);
                    progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
                    progressBar.setMaximumSize(new Dimension(380, 8));
                    
                    //agregar los elemento a la tajeta 
                    card.add(lblNombre);
                    card.add(Box.createRigidArea(new Dimension(0, 4)));
                    card.add(lblDetalles);
                    card.add(Box.createRigidArea(new Dimension(0, 8)));
                    card.add(progressBar);
                    
                    //agregar al panel contenedor
                    pnlListaMetas.add(card);
                    pnlListaMetas.add(Box.createRigidArea(new Dimension(0, 10)));
                }
                if(!hayMetas){
                    JLabel lblVacio = new JLabel("no tienes metas registrada aún.");
                    lblVacio.setForeground(new Color(148, 163, 184));
                    lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
                    pnlListaMetas.add(lblVacio);
                }
            }
        }catch(SQLException e){
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar metas", e);
            JOptionPane.showMessageDialog(this, "Error al cargar metas: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
        pnlListaMetas.revalidate();
        pnlListaMetas.repaint();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnMenuPrincoal = new javax.swing.JButton();
        btnIngresos = new javax.swing.JButton();
        btnEgresos = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnCrearMeta = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlListaMetas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(15, 23, 42));

        jPanel2.setBackground(new java.awt.Color(11, 17, 32));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Control Total");

        jLabel7.setText("jLabel7");

        btnMenuPrincoal.setBackground(new java.awt.Color(11, 17, 32));
        btnMenuPrincoal.setForeground(new java.awt.Color(148, 163, 184));
        btnMenuPrincoal.setText("Inicio");
        btnMenuPrincoal.setBorderPainted(false);
        btnMenuPrincoal.setContentAreaFilled(false);
        btnMenuPrincoal.setFocusPainted(false);
        btnMenuPrincoal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMenuPrincoal.addActionListener(this::btnMenuPrincoalActionPerformed);

        btnIngresos.setBackground(new java.awt.Color(11, 17, 32));
        btnIngresos.setForeground(new java.awt.Color(148, 163, 184));
        btnIngresos.setText("Ingresos");
        btnIngresos.setBorderPainted(false);
        btnIngresos.setContentAreaFilled(false);
        btnIngresos.setFocusPainted(false);
        btnIngresos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnIngresos.addActionListener(this::btnIngresosActionPerformed);

        btnEgresos.setBackground(new java.awt.Color(11, 17, 32));
        btnEgresos.setForeground(new java.awt.Color(148, 163, 184));
        btnEgresos.setText("Egresos");
        btnEgresos.setBorderPainted(false);
        btnEgresos.setContentAreaFilled(false);
        btnEgresos.setFocusPainted(false);
        btnEgresos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEgresos.addActionListener(this::btnEgresosActionPerformed);

        jButton4.setBackground(new java.awt.Color(11, 17, 32));
        jButton4.setForeground(new java.awt.Color(16, 185, 129));
        jButton4.setText("Metas");
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setDefaultCapable(false);
        jButton4.setFocusPainted(false);
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.addActionListener(this::jButton4ActionPerformed);

        btnHistorial.setBackground(new java.awt.Color(11, 17, 32));
        btnHistorial.setForeground(new java.awt.Color(148, 163, 184));
        btnHistorial.setText("Reporte");
        btnHistorial.setBorderPainted(false);
        btnHistorial.setContentAreaFilled(false);
        btnHistorial.setFocusPainted(false);
        btnHistorial.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHistorial.addActionListener(this::btnHistorialActionPerformed);

        jLabel5.setIcon(new javax.swing.ImageIcon("C:\\Users\\win11\\Documents\\logo2Integra.jpg")); // NOI18N

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setText("Cerrar Sesion");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnHistorial)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7))
                            .addComponent(btnMenuPrincoal)
                            .addComponent(btnIngresos)
                            .addComponent(btnEgresos)
                            .addComponent(jButton4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1))
                .addGap(28, 28, 28)
                .addComponent(btnMenuPrincoal)
                .addGap(18, 18, 18)
                .addComponent(btnIngresos)
                .addGap(18, 18, 18)
                .addComponent(btnEgresos)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(btnHistorial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(22, 22, 22))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Metas Financieras");

        jLabel4.setForeground(new java.awt.Color(148, 163, 184));
        jLabel4.setText("Establece y controla tus objetivos de ahorro");

        btnCrearMeta.setBackground(new java.awt.Color(0, 230, 118));
        btnCrearMeta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCrearMeta.setForeground(new java.awt.Color(255, 255, 255));
        btnCrearMeta.setText("+Crea una nueva meta");
        btnCrearMeta.addActionListener(this::btnCrearMetaActionPerformed);

        jScrollPane1.setBackground(new java.awt.Color(15, 23, 42));
        jScrollPane1.setBorder(null);
        jScrollPane1.setOpaque(false);

        pnlListaMetas.setBackground(new java.awt.Color(15, 23, 42));
        pnlListaMetas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlListaMetasMouseClicked(evt);
            }
        });
        pnlListaMetas.setLayout(new javax.swing.BoxLayout(pnlListaMetas, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(pnlListaMetas);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(33, 33, 33)
                        .addComponent(btnCrearMeta)
                        .addContainerGap(35, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btnCrearMeta))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEgresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEgresosActionPerformed
        // TODO add your handling code here:
        frmEgresos egreso = new frmEgresos(this.usuarioActual);
        egreso.setLocationRelativeTo(null);
        egreso.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnEgresosActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        // TODO add your handling code here:
        frmReportes reportes = new frmReportes(this.usuarioActual);
        reportes.setLocationRelativeTo(null);
        reportes.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void btnMenuPrincoalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuPrincoalActionPerformed
        // TODO add your handling code here:
        frmMenuPrincipal inicio = new frmMenuPrincipal(this.usuarioActual);
        inicio.setLocationRelativeTo(null);
        inicio.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMenuPrincoalActionPerformed

    private void btnCrearMetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearMetaActionPerformed
        // TODO add your handling code here:
        if (this.usuarioActual == null) {
            JOptionPane.showMessageDialog(this, "No hay una sesión de usuario activa en frmMetas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        frmMetasNueva meta = new frmMetasNueva(this, true, this.usuarioActual);
        meta.setLocationRelativeTo(null);
        meta.setVisible(true);
        cargarMetas();
    }//GEN-LAST:event_btnCrearMetaActionPerformed

    private void btnIngresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresosActionPerformed
        // TODO add your handling code here:
        controlTotal ingresos = new controlTotal(this.usuarioActual);
        ingresos.setLocationRelativeTo(null);
        ingresos.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnIngresosActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void pnlListaMetasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlListaMetasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlListaMetasMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         frmLogin log = new frmLogin();
        log.setLocationRelativeTo(null);
        log.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmMetas().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCrearMeta;
    private javax.swing.JButton btnEgresos;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnIngresos;
    private javax.swing.JButton btnMenuPrincoal;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlListaMetas;
    // End of variables declaration//GEN-END:variables
}
