/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package formularios;

import clases.Usuario;
import clases.MovimientoDTO;

/**
 *
 * @author win11
 */
public class frmReportes extends javax.swing.JFrame {
    
    private Usuario usuarioActual;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmReportes.class.getName());

    /**
     * Creates new form frmReportes
     */
    public frmReportes() {
        initComponents();
    }
    
    public frmReportes(Usuario usuario){
        this.usuarioActual = usuario;
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        cargarDatosReporte();
    }
    public void cargarDatosReporte(){
        if(this.usuarioActual == null) return;
        cargarHistorial();
    }
    
    public void cargarHistorial(){
        pnlHistorial.removeAll();
        pnlHistorial.setLayout(new javax.swing.BoxLayout(pnlHistorial, javax.swing.BoxLayout.Y_AXIS));
        
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        String fechaDesde = txtDesde.getText().trim();
        String fechaHasta = txtHasta.getText().trim();
        
        java.util.List<MovimientoDTO> listaMovimientos = new java.util.ArrayList<>();
        
        try(java.sql.Connection cn = conexion.ConexionBD.getConexion()){
            
            String sqlIngresos = "select concepto, cantidad, fecha from ingresos where id_usuario = ?";
            try(java.sql.PreparedStatement ps = cn.prepareStatement(sqlIngresos)){
                ps.setInt(1, usuarioActual.getId());
                try(java.sql.ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        listaMovimientos.add(new MovimientoDTO(
                        "Ingresos",
                        rs.getString("concepto"),
                        rs.getDouble("cantidad"),
                        rs.getString("fecha")
                        ));
                    }
                }
            }
            String sqlEgresos = "select concepto, cantidad, fecha from egresos where id_usuario = ?";
            try(java.sql.PreparedStatement ps = cn.prepareStatement(sqlEgresos)){
                ps.setInt(1, usuarioActual.getId());
                try(java.sql.ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        listaMovimientos.add(new MovimientoDTO(
                        "Egresos",
                        rs.getString("concepto"),
                        rs.getDouble("cantidad"),
                        rs.getString("fecha")
                        ));
                    }
                }
            }
        }catch(java.sql.SQLException e){
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar historial desde BD", e);
        }
        
       listaMovimientos.sort((m1, m2) -> m2.fecha.compareTo(m1.fecha));
       
       double totalIngresos = 0;
       double totalEgresos = 0;
       
       for(MovimientoDTO m : listaMovimientos){
           boolean coincideBusqueda = m.concepto.toLowerCase().contains(busqueda) || m.tipo.toLowerCase().contains(busqueda);
           boolean coincideDesde = fechaDesde.isEmpty() || m.fecha.compareTo(fechaDesde) >= 0;
           boolean coincideHasta = fechaHasta.isEmpty() || m.fecha.compareTo(fechaHasta) <= 0;
           
           if(coincideBusqueda && coincideDesde && coincideHasta){
               if(m.tipo.equals("Ingresos")){
                   totalIngresos += m.cantidad;
               }else{
                   totalEgresos += m.cantidad;
               }
                javax.swing.JPanel pnlItem = crearTarjetaMovimiento(m.tipo, m.concepto, m.cantidad, m.fecha);
                pnlHistorial.add(pnlItem);
                pnlHistorial.add(javax.swing.Box.createVerticalStrut(8));
           }
       }
       actualizarTotales(totalIngresos, totalEgresos);
       
       pnlHistorial.revalidate();
       pnlHistorial.repaint();
    }
    
    private javax.swing.JPanel crearTarjetaMovimiento(String tipo, String concepto, double monto, String fecha){
        javax.swing.JPanel panel =  new javax.swing.JPanel();
        panel.setBackground(new java.awt.Color(30, 41, 59));
        panel.setLayout(new java.awt.BorderLayout(10, 10));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 50));

        javax.swing.JLabel lblConcepto = new javax.swing.JLabel(concepto + " (" + fecha + ")");
        lblConcepto.setForeground(java.awt.Color.WHITE);
        lblConcepto.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        boolean esIngreso = tipo.equalsIgnoreCase("Ingresos");
        String signo = esIngreso ? "+$" : "-$";
        javax.swing.JLabel lblMonto = new javax.swing.JLabel(signo + String.format("%.2f", monto));
        lblMonto.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        lblMonto.setForeground(esIngreso ? new java.awt.Color(0, 230, 118) : new java.awt.Color(255, 82, 82));

        panel.add(lblConcepto, java.awt.BorderLayout.WEST);
        panel.add(lblMonto, java.awt.BorderLayout.EAST);

        return panel;
    }
    
    private void actualizarTotales(double ingresos, double egresos){
        double balance = ingresos - egresos;
        
        lblIngresos.setText(String.format("$%.2f", ingresos)); 
        lblEgresos.setText(String.format("$%.2f", egresos));  
        lblBalance.setText(String.format("$%.2f", balance));
        
        if(balance >= 0){
            lblBalance.setForeground(new java.awt.Color(0, 230, 118));
        }else{
            lblBalance.setForeground(new java.awt.Color(255, 82, 82));
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

        pnlBalanceo = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnInicio = new javax.swing.JButton();
        btnIngresos = new javax.swing.JButton();
        btnGastos = new javax.swing.JButton();
        btnAhorro = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pnlIngresosVista = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblIngresos = new javax.swing.JLabel();
        pnlEgresosVista = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblEgresos = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblBalance = new javax.swing.JLabel();
        pnlBusqueda = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        lblDesde = new javax.swing.JLabel();
        lblHasta = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        txtDesde = new javax.swing.JTextField();
        txtHasta = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlHistorial = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlBalanceo.setBackground(new java.awt.Color(15, 23, 42));

        jPanel2.setBackground(new java.awt.Color(11, 17, 32));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Control Total");

        btnInicio.setForeground(new java.awt.Color(156, 163, 175));
        btnInicio.setText("Inicio");
        btnInicio.setBorderPainted(false);
        btnInicio.setContentAreaFilled(false);
        btnInicio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInicio.addActionListener(this::btnInicioActionPerformed);

        btnIngresos.setBackground(new java.awt.Color(156, 163, 175));
        btnIngresos.setForeground(new java.awt.Color(156, 163, 175));
        btnIngresos.setText("Ingresos");
        btnIngresos.setBorderPainted(false);
        btnIngresos.setContentAreaFilled(false);
        btnIngresos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnIngresos.addActionListener(this::btnIngresosActionPerformed);

        btnGastos.setForeground(new java.awt.Color(156, 163, 175));
        btnGastos.setText("Egresos");
        btnGastos.setBorderPainted(false);
        btnGastos.setContentAreaFilled(false);
        btnGastos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnGastos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnGastos.addActionListener(this::btnGastosActionPerformed);

        btnAhorro.setForeground(new java.awt.Color(156, 163, 175));
        btnAhorro.setText("Metas");
        btnAhorro.setBorderPainted(false);
        btnAhorro.setContentAreaFilled(false);
        btnAhorro.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAhorro.addActionListener(this::btnAhorroActionPerformed);

        btnHistorial.setForeground(new java.awt.Color(16, 185, 129));
        btnHistorial.setText("Reporte");
        btnHistorial.setBorderPainted(false);
        btnHistorial.setContentAreaFilled(false);
        btnHistorial.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel8.setIcon(new javax.swing.ImageIcon("C:\\Users\\win11\\Documents\\logo2Integra.jpg")); // NOI18N

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setText("Cerrar Sesion");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInicio)
                    .addComponent(btnIngresos)
                    .addComponent(btnGastos)
                    .addComponent(btnAhorro)
                    .addComponent(btnHistorial))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1))
                    .addComponent(jButton1))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(btnInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnIngresos)
                .addGap(18, 18, 18)
                .addComponent(btnGastos)
                .addGap(18, 18, 18)
                .addComponent(btnAhorro)
                .addGap(18, 18, 18)
                .addComponent(btnHistorial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(24, 24, 24))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Reportes Financieros");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(148, 163, 184));
        jLabel4.setText("Historial completo de tus movimientos registrados");

        pnlIngresosVista.setBackground(new java.awt.Color(30, 41, 59));

        jLabel5.setForeground(new java.awt.Color(148, 163, 184));
        jLabel5.setText("Ingresos en Vista");

        lblIngresos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblIngresos.setForeground(new java.awt.Color(0, 230, 118));
        lblIngresos.setText("$12,000");

        javax.swing.GroupLayout pnlIngresosVistaLayout = new javax.swing.GroupLayout(pnlIngresosVista);
        pnlIngresosVista.setLayout(pnlIngresosVistaLayout);
        pnlIngresosVistaLayout.setHorizontalGroup(
            pnlIngresosVistaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIngresosVistaLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblIngresos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlIngresosVistaLayout.setVerticalGroup(
            pnlIngresosVistaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIngresosVistaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlIngresosVistaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblIngresos))
                .addGap(75, 75, 75))
        );

        pnlEgresosVista.setBackground(new java.awt.Color(30, 41, 59));

        jLabel6.setForeground(new java.awt.Color(148, 163, 184));
        jLabel6.setText("Egresos en vista");

        lblEgresos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblEgresos.setForeground(new java.awt.Color(255, 82, 82));
        lblEgresos.setText("$5600");

        javax.swing.GroupLayout pnlEgresosVistaLayout = new javax.swing.GroupLayout(pnlEgresosVista);
        pnlEgresosVista.setLayout(pnlEgresosVistaLayout);
        pnlEgresosVistaLayout.setHorizontalGroup(
            pnlEgresosVistaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEgresosVistaLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEgresos)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        pnlEgresosVistaLayout.setVerticalGroup(
            pnlEgresosVistaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEgresosVistaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEgresosVistaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblEgresos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(30, 41, 59));

        jLabel7.setForeground(new java.awt.Color(148, 163, 184));
        jLabel7.setText("Balanceo");

        lblBalance.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblBalance.setForeground(new java.awt.Color(255, 82, 82));
        lblBalance.setText("$1800");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblBalance)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblBalance))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBusqueda.setBackground(new java.awt.Color(30, 41, 59));

        lblBuscar.setForeground(new java.awt.Color(148, 163, 184));
        lblBuscar.setText("BUSCAR:");

        lblDesde.setForeground(new java.awt.Color(148, 163, 184));
        lblDesde.setText("DESDE:");

        lblHasta.setForeground(new java.awt.Color(148, 163, 184));
        lblHasta.setText("HASTA:");

        txtBuscar.setBackground(new java.awt.Color(15, 23, 42));
        txtBuscar.setForeground(new java.awt.Color(255, 255, 255));
        txtBuscar.addActionListener(this::txtBuscarActionPerformed);

        txtDesde.setBackground(new java.awt.Color(15, 23, 42));
        txtDesde.setForeground(new java.awt.Color(255, 255, 255));
        txtDesde.addActionListener(this::txtDesdeActionPerformed);

        txtHasta.setBackground(new java.awt.Color(15, 23, 42));
        txtHasta.setForeground(new java.awt.Color(255, 255, 255));
        txtHasta.addActionListener(this::txtHastaActionPerformed);

        javax.swing.GroupLayout pnlBusquedaLayout = new javax.swing.GroupLayout(pnlBusqueda);
        pnlBusqueda.setLayout(pnlBusquedaLayout);
        pnlBusquedaLayout.setHorizontalGroup(
            pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBusquedaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBusquedaLayout.createSequentialGroup()
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBusquedaLayout.createSequentialGroup()
                        .addComponent(lblBuscar)
                        .addGap(67, 67, 67)
                        .addComponent(lblDesde)
                        .addGap(71, 71, 71)
                        .addComponent(lblHasta)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBusquedaLayout.setVerticalGroup(
            pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBusquedaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar)
                    .addComponent(lblDesde)
                    .addComponent(lblHasta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(null);

        pnlHistorial.setBackground(new java.awt.Color(15, 23, 42));
        pnlHistorial.setLayout(new javax.swing.BoxLayout(pnlHistorial, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(pnlHistorial);

        javax.swing.GroupLayout pnlBalanceoLayout = new javax.swing.GroupLayout(pnlBalanceo);
        pnlBalanceo.setLayout(pnlBalanceoLayout);
        pnlBalanceoLayout.setHorizontalGroup(
            pnlBalanceoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBalanceoLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBalanceoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBalanceoLayout.createSequentialGroup()
                        .addComponent(pnlIngresosVista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlEgresosVista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(pnlBalanceoLayout.createSequentialGroup()
                        .addComponent(pnlBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pnlBalanceoLayout.createSequentialGroup()
                        .addGroup(pnlBalanceoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlBalanceoLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );
        pnlBalanceoLayout.setVerticalGroup(
            pnlBalanceoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBalanceoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBalanceoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlIngresosVista, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlEgresosVista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(pnlBalanceoLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBalanceo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBalanceo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresosActionPerformed
        // TODO add your handling code here:
        controlTotal ingreso = new controlTotal(this.usuarioActual);
        ingreso.setVisible(true);
        ingreso.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnIngresosActionPerformed

    private void btnGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGastosActionPerformed
        // TODO add your handling code here:
        frmEgresos egresos = new frmEgresos(this.usuarioActual);
        egresos.setVisible(true);
        egresos.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnGastosActionPerformed

    private void btnAhorroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAhorroActionPerformed
        // TODO add your handling code here:
        frmMetas ahorro = new frmMetas(this.usuarioActual);
        ahorro.setVisible(true);
        ahorro.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnAhorroActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
        cargarHistorial();
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void txtDesdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDesdeActionPerformed
        // TODO add your handling code here:
        cargarHistorial();
    }//GEN-LAST:event_txtDesdeActionPerformed

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        // TODO add your handling code here:
        frmMenuPrincipal inicio = new frmMenuPrincipal(this.usuarioActual);
        inicio.setLocationRelativeTo(null);
        inicio.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnInicioActionPerformed

    private void txtHastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHastaActionPerformed
        // TODO add your handling code here:
        cargarHistorial();
    }//GEN-LAST:event_txtHastaActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new frmReportes().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAhorro;
    private javax.swing.JButton btnGastos;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnIngresos;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblDesde;
    private javax.swing.JLabel lblEgresos;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JLabel lblIngresos;
    private javax.swing.JPanel pnlBalanceo;
    private javax.swing.JPanel pnlBusqueda;
    private javax.swing.JPanel pnlEgresosVista;
    private javax.swing.JPanel pnlHistorial;
    private javax.swing.JPanel pnlIngresosVista;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDesde;
    private javax.swing.JTextField txtHasta;
    // End of variables declaration//GEN-END:variables
}
