/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package formularios;

import conexion.ConexionBD;
import clases.Usuario;

/**
 *
 * @author win11
 */
public class frmMenuPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmMenuPrincipal.class.getName());
    private Usuario usuarioActual; //guarda el usuario de la sesion actual 

    /**
     * Creates new form frmMenuPrincipal
     */
    public frmMenuPrincipal() {
        initComponents(); 
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    }
    
    public frmMenuPrincipal(Usuario usuario){
        this.usuarioActual = usuario;
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        cargarDatosUsuario();
    }
    private void cargarDatosUsuario(){
        if(usuarioActual !=null){
             lblSaludo.setText("¡Hola, " + usuarioActual.getNombre() + "!");
             
             //consultar ingreso egresos y saldo del MySQL 
             cargarResumenFinanciero(usuarioActual.getId());
             cargarFechaActual();
             cargarMetasActivas();
             cargarActividadReciente();
        }
    }
    
    public void cargarMetasActivas(){
        if(this.usuarioActual == null) return;
        
        pnlMetas.removeAll();
        pnlMetas.setLayout(new javax.swing.BoxLayout(pnlMetas, javax.swing.BoxLayout.Y_AXIS));
        
        javax.swing.JLabel lblTitulo =  new javax.swing.JLabel("Metas Activas");
        lblTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblTitulo.setForeground(new java.awt.Color(255,255,255));
        lblTitulo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        
        pnlMetas.add(lblTitulo);
        pnlMetas.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 8)));
        
        String sql = "select nombre_meta, monto_objetivo, monto_actual from metas where id_usuario = ? limit 2";
        try(java.sql.Connection cn = ConexionBD.getConexion();
                java.sql.PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setInt(1, this.usuarioActual.getId());
            
            try(java.sql.ResultSet rs = ps.executeQuery()){
                boolean hayMetas = false;
                
                while(rs.next()){
                    hayMetas = true;
                    String nombre = rs.getString("nombre_meta");
                    double objetivo = rs.getDouble("monto_objetivo");
                    double actual = rs.getDouble("monto_actual");
                    
                    int porcentaje = objetivo > 0 ? (int) ((actual / objetivo) * 100): 0;
                    if(porcentaje > 100) porcentaje = 100;
                    
                    javax.swing.JLabel lblNombre = new javax.swing.JLabel(nombre);
                    lblNombre.setForeground(new java.awt.Color(16, 185, 129));
                    lblNombre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
                    lblNombre.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    
                    javax.swing.JProgressBar bar = new javax.swing.JProgressBar(0, 100);
                    bar.setValue(porcentaje);
                    bar.setString(porcentaje + "%");
                    bar.setStringPainted(true);
                    bar.setForeground(new java.awt.Color(16, 185, 129));
                    bar.setBackground(new java.awt.Color(15, 23, 42));
                    bar.setBorderPainted(false);
                    bar.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    bar.setMaximumSize(new java.awt.Dimension(180, 18));

                    pnlMetas.add(lblNombre);
                    pnlMetas.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 4)));
                    pnlMetas.add(bar);
                    pnlMetas.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 10)));
                }
                if(!hayMetas){
                    javax.swing.JLabel lblVacio = new javax.swing.JLabel("Sin metas registradas");
                    lblVacio.setForeground(new java.awt.Color(148,163,184));
                    lblVacio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
                    lblVacio.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    pnlMetas.add(lblVacio);
                }
            }
        }catch(java.sql.SQLException e){
            System.err.println("Error al cargar metas activas: " + e.getMessage());
        }
        pnlMetas.revalidate();
        pnlMetas.repaint();
    }
    
    public void cargarActividadReciente() {
    if (this.usuarioActual == null) return;

    pnlActividad.removeAll();
    pnlActividad.setLayout(new javax.swing.BoxLayout(pnlActividad, javax.swing.BoxLayout.Y_AXIS));

    javax.swing.JLabel lblTitulo = new javax.swing.JLabel("Actividad reciente");
    lblTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
    lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
    lblTitulo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    
    pnlActividad.add(lblTitulo);
    pnlActividad.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 10)));

    java.util.List<clases.MovimientoDTO> lista = new java.util.ArrayList<>();

    try (java.sql.Connection cn = conexion.ConexionBD.getConexion()) {
        
        String sqlIng = "SELECT concepto, cantidad, fecha FROM ingresos WHERE id_usuario = ? ORDER BY fecha DESC LIMIT 3";
        try (java.sql.PreparedStatement ps = cn.prepareStatement(sqlIng)) {
            ps.setInt(1, usuarioActual.getId());
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new clases.MovimientoDTO("Ingreso", rs.getString("concepto"), rs.getDouble("cantidad"), rs.getString("fecha")));
                }
            }
        }

        String sqlEgr = "SELECT concepto, cantidad, fecha FROM egresos WHERE id_usuario = ? ORDER BY fecha DESC LIMIT 3";
        try (java.sql.PreparedStatement ps = cn.prepareStatement(sqlEgr)) {
            ps.setInt(1, usuarioActual.getId());
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new clases.MovimientoDTO("Egreso", rs.getString("concepto"), rs.getDouble("cantidad"), rs.getString("fecha")));
                }
            }
        }
    } catch (java.sql.SQLException e) {
        System.err.println("Error al cargar actividad reciente: " + e.getMessage());
    }

    lista.sort((m1, m2) -> m2.fecha.compareTo(m1.fecha));

    int limite = Math.min(lista.size(), 3);
    for (int i = 0; i < limite; i++) {
        clases.MovimientoDTO m = lista.get(i);
        
        javax.swing.JPanel fila = new javax.swing.JPanel(new java.awt.BorderLayout(5, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new java.awt.Dimension(200, 20));
        fila.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        javax.swing.JLabel lblConcepto = new javax.swing.JLabel(m.concepto);
        lblConcepto.setForeground(new java.awt.Color(148, 163, 184));
        lblConcepto.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

        boolean esIngreso = m.tipo.equalsIgnoreCase("Ingreso");
        String signo = esIngreso ? "+" : "-";
        
        javax.swing.JLabel lblMonto = new javax.swing.JLabel(signo + String.format("%.0f", m.cantidad));
        lblMonto.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblMonto.setForeground(esIngreso ? new java.awt.Color(16, 185, 129) : new java.awt.Color(255, 51, 51));

        fila.add(lblConcepto, java.awt.BorderLayout.WEST);
        fila.add(lblMonto, java.awt.BorderLayout.EAST);

        pnlActividad.add(fila);
        pnlActividad.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 6)));
    }

    if (lista.isEmpty()) {
        javax.swing.JLabel lblVacio = new javax.swing.JLabel("Sin movimientos recientes");
        lblVacio.setForeground(new java.awt.Color(148, 163, 184));
        lblVacio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        lblVacio.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        pnlActividad.add(lblVacio);
    }

    pnlActividad.revalidate();
    pnlActividad.repaint();
}
    
    public void cargarFechaActual(){
        //obtener fecha actual de sistema 
        java.time.LocalDate fechaActual = java.time.LocalDate.now();
        
        //formato en español 
        java.time.format.DateTimeFormatter formato = java.time.format.DateTimeFormatter
            .ofPattern("EEEE d MMMM, yyyy", new java.util.Locale("es", "ES"));
        //formatear texto
        String fechaFormateada = fechaActual.format(formato);
        
        //primera letra de del dia de la seman 
        fechaFormateada = fechaFormateada.substring(0, 1).toUpperCase() + fechaFormateada.substring(1);
        
        //asignar al jlabel
        lblFecha.setText(fechaFormateada);
        
        
}
    
    public void cargarResumenFinanciero(int idUsuario){
        String sqlIngresos = "select sum(cantidad) as total from ingresos where id_usuario = ? ";
        String sqlEgresos = "select sum(cantidad) as total from egresos where id_usuario = ? ";
        
        double totalIngresos = 0.0;
        double totalEgresos = 0.0;
        
        try(java.sql.Connection cn = ConexionBD.getConexion()){
            try(java.sql.PreparedStatement ps = cn.prepareStatement(sqlIngresos)){
                ps.setInt(1, idUsuario);
                try(java.sql.ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        totalIngresos = rs.getDouble("total");
                    }
                }
            }
            
            try(java.sql.PreparedStatement ps = cn.prepareStatement(sqlEgresos)){
                ps.setInt(1, idUsuario);
                try(java.sql.ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        totalEgresos = rs.getDouble("total");
                    }
                }
            }
            double saldoDisponible = totalIngresos - totalEgresos;
            
            lblMontoIngreso.setText(String.format("$%.2f", totalIngresos));
            lblMontoEgreso.setText(String.format("$%.2f", totalEgresos));
            lblSaldoMonto.setText(String.format("$%.2f", saldoDisponible));
        }catch(java.sql.SQLException e){
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error al cargar datos financieros: " + e.getMessage(),
                "Error SQL",
                javax.swing.JOptionPane.ERROR_MESSAGE);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnInicio = new javax.swing.JButton();
        btnIngresos = new javax.swing.JButton();
        btnGastos = new javax.swing.JButton();
        btnAhorro = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblFecha = new javax.swing.JLabel();
        lblSaludo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        pnlCardSaldo = new javax.swing.JPanel();
        lblSaldoTitulo = new javax.swing.JLabel();
        lblSaldoMonto = new javax.swing.JLabel();
        pnlCardIngreso = new javax.swing.JPanel();
        lblIngresoTitulo = new javax.swing.JLabel();
        lblMontoIngreso = new javax.swing.JLabel();
        pnlCardEgreso = new javax.swing.JPanel();
        lblEgresoTitulo = new javax.swing.JLabel();
        lblMontoEgreso = new javax.swing.JLabel();
        pnlMetas = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        barMeta1 = new javax.swing.JProgressBar();
        jLabel13 = new javax.swing.JLabel();
        barMeta2 = new javax.swing.JProgressBar();
        pnlActividad = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        pnlAcciones = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        btnAgregarIngreso = new javax.swing.JButton();
        btnAgregarEgreso = new javax.swing.JButton();
        btnNuevaMeta = new javax.swing.JButton();
        btnReportes = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(15, 23, 42));

        jPanel2.setBackground(new java.awt.Color(11, 17, 32));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Control Total");

        btnInicio.setForeground(new java.awt.Color(16, 185, 129));
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

        btnHistorial.setForeground(new java.awt.Color(156, 163, 175));
        btnHistorial.setText("Reporte");
        btnHistorial.setBorderPainted(false);
        btnHistorial.setContentAreaFilled(false);
        btnHistorial.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHistorial.addActionListener(this::btnHistorialActionPerformed);

        jLabel3.setIcon(new javax.swing.ImageIcon("C:\\Users\\win11\\Documents\\logo2Integra.jpg")); // NOI18N

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
                            .addComponent(btnInicio)
                            .addComponent(btnIngresos)
                            .addComponent(btnGastos)
                            .addComponent(btnAhorro)
                            .addComponent(btnHistorial))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(44, 44, 44))
        );

        lblFecha.setForeground(new java.awt.Color(148, 163, 184));
        lblFecha.setText("Martes 11 Agosto, 2026");
        lblFecha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFechaMouseClicked(evt);
            }
        });

        lblSaludo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblSaludo.setForeground(new java.awt.Color(255, 255, 255));
        lblSaludo.setText("Hola, Cristobal");
        lblSaludo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSaludoMouseClicked(evt);
            }
        });

        lblSubtitulo.setForeground(new java.awt.Color(140, 163, 184));
        lblSubtitulo.setText("Aqui el resumen de tus finanzas");

        pnlCardSaldo.setBackground(new java.awt.Color(30, 41, 59));

        lblSaldoTitulo.setBackground(new java.awt.Color(148, 163, 184));
        lblSaldoTitulo.setForeground(new java.awt.Color(148, 163, 184));
        lblSaldoTitulo.setText("Saldo disponible");

        lblSaldoMonto.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblSaldoMonto.setForeground(new java.awt.Color(16, 185, 129));
        lblSaldoMonto.setText("$0.00");

        javax.swing.GroupLayout pnlCardSaldoLayout = new javax.swing.GroupLayout(pnlCardSaldo);
        pnlCardSaldo.setLayout(pnlCardSaldoLayout);
        pnlCardSaldoLayout.setHorizontalGroup(
            pnlCardSaldoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardSaldoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardSaldoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSaldoTitulo)
                    .addComponent(lblSaldoMonto))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCardSaldoLayout.setVerticalGroup(
            pnlCardSaldoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardSaldoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSaldoMonto)
                .addGap(18, 18, 18)
                .addComponent(lblSaldoTitulo)
                .addContainerGap())
        );

        pnlCardIngreso.setBackground(new java.awt.Color(30, 41, 59));

        lblIngresoTitulo.setForeground(new java.awt.Color(148, 163, 180));
        lblIngresoTitulo.setText("Total Ingresos");

        lblMontoIngreso.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblMontoIngreso.setForeground(new java.awt.Color(99, 102, 241));
        lblMontoIngreso.setText("$0.00");

        javax.swing.GroupLayout pnlCardIngresoLayout = new javax.swing.GroupLayout(pnlCardIngreso);
        pnlCardIngreso.setLayout(pnlCardIngresoLayout);
        pnlCardIngresoLayout.setHorizontalGroup(
            pnlCardIngresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardIngresoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardIngresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIngresoTitulo)
                    .addComponent(lblMontoIngreso))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCardIngresoLayout.setVerticalGroup(
            pnlCardIngresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardIngresoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMontoIngreso)
                .addGap(18, 18, 18)
                .addComponent(lblIngresoTitulo)
                .addContainerGap())
        );

        pnlCardEgreso.setBackground(new java.awt.Color(30, 41, 59));

        lblEgresoTitulo.setForeground(new java.awt.Color(148, 163, 184));
        lblEgresoTitulo.setText("Total Egresos");

        lblMontoEgreso.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblMontoEgreso.setForeground(new java.awt.Color(245, 158, 11));
        lblMontoEgreso.setText("$0.00");

        javax.swing.GroupLayout pnlCardEgresoLayout = new javax.swing.GroupLayout(pnlCardEgreso);
        pnlCardEgreso.setLayout(pnlCardEgresoLayout);
        pnlCardEgresoLayout.setHorizontalGroup(
            pnlCardEgresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardEgresoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardEgresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEgresoTitulo)
                    .addComponent(lblMontoEgreso))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCardEgresoLayout.setVerticalGroup(
            pnlCardEgresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardEgresoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMontoEgreso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEgresoTitulo)
                .addContainerGap())
        );

        pnlMetas.setBackground(new java.awt.Color(30, 41, 59));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Metas Activas");

        jLabel12.setForeground(new java.awt.Color(16, 185, 129));
        jLabel12.setText("Fondo de emergencia");

        barMeta1.setForeground(new java.awt.Color(16, 185, 129));
        barMeta1.setValue(62);
        barMeta1.setStringPainted(true);

        jLabel13.setForeground(new java.awt.Color(99, 102, 241));
        jLabel13.setText("Viaje a Europa");

        barMeta2.setForeground(new java.awt.Color(99, 102, 241));
        barMeta2.setValue(24);
        barMeta2.setStringPainted(true);

        javax.swing.GroupLayout pnlMetasLayout = new javax.swing.GroupLayout(pnlMetas);
        pnlMetas.setLayout(pnlMetasLayout);
        pnlMetasLayout.setHorizontalGroup(
            pnlMetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMetasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(barMeta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(barMeta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlMetasLayout.setVerticalGroup(
            pnlMetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMetasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barMeta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(barMeta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlActividad.setBackground(new java.awt.Color(30, 41, 59));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Actividad reciente");

        jLabel15.setForeground(new java.awt.Color(148, 163, 148));
        jLabel15.setText("Salario mensual");

        jLabel16.setForeground(new java.awt.Color(16, 185, 129));
        jLabel16.setText("+15,000");

        jLabel17.setForeground(new java.awt.Color(148, 163, 184));
        jLabel17.setText("Renta apartamento");

        jLabel18.setForeground(new java.awt.Color(255, 51, 51));
        jLabel18.setText("-4,500");

        jLabel19.setForeground(new java.awt.Color(148, 163, 184));
        jLabel19.setText("Super mercado");

        jLabel20.setForeground(new java.awt.Color(255, 102, 51));
        jLabel20.setText("-1,800");

        javax.swing.GroupLayout pnlActividadLayout = new javax.swing.GroupLayout(pnlActividad);
        pnlActividad.setLayout(pnlActividadLayout);
        pnlActividadLayout.setHorizontalGroup(
            pnlActividadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlActividadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlActividadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(pnlActividadLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16))
                    .addGroup(pnlActividadLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18))
                    .addGroup(pnlActividadLayout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlActividadLayout.setVerticalGroup(
            pnlActividadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlActividadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addGroup(pnlActividadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlActividadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlActividadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlAcciones.setBackground(new java.awt.Color(30, 41, 59));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Acciones rapidas");

        btnAgregarIngreso.setBackground(new java.awt.Color(37, 45, 74));
        btnAgregarIngreso.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarIngreso.setText("+Agregar ingreso");
        btnAgregarIngreso.setFocusPainted(false);
        btnAgregarIngreso.addActionListener(this::btnAgregarIngresoActionPerformed);

        btnAgregarEgreso.setBackground(new java.awt.Color(42, 38, 51));
        btnAgregarEgreso.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarEgreso.setText("-Agregar egreso");
        btnAgregarEgreso.addActionListener(this::btnAgregarEgresoActionPerformed);

        btnNuevaMeta.setBackground(new java.awt.Color(30, 58, 58));
        btnNuevaMeta.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevaMeta.setText("Nueva meta");
        btnNuevaMeta.addActionListener(this::btnNuevaMetaActionPerformed);

        btnReportes.setBackground(new java.awt.Color(37, 45, 74));
        btnReportes.setForeground(new java.awt.Color(255, 255, 255));
        btnReportes.setText("Ver reportes");
        btnReportes.addActionListener(this::btnReportesActionPerformed);

        javax.swing.GroupLayout pnlAccionesLayout = new javax.swing.GroupLayout(pnlAcciones);
        pnlAcciones.setLayout(pnlAccionesLayout);
        pnlAccionesLayout.setHorizontalGroup(
            pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(btnAgregarIngreso)
                    .addComponent(btnAgregarEgreso)
                    .addComponent(btnNuevaMeta)
                    .addComponent(btnReportes))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlAccionesLayout.setVerticalGroup(
            pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccionesLayout.createSequentialGroup()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregarIngreso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregarEgreso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNuevaMeta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReportes)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblFecha)
                            .addComponent(lblSaludo)
                            .addComponent(lblSubtitulo))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pnlCardSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlCardIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlCardEgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pnlMetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlActividad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFecha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSaludo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSubtitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlCardEgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlCardSaldo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlCardIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlActividad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlMetas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(pnlAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
  
    
    private void btnGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGastosActionPerformed
        // TODO add your handling code here:
        frmEgresos egresos = new frmEgresos(this.usuarioActual);
        egresos.setVisible(true);
        egresos.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnGastosActionPerformed

    private void btnIngresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresosActionPerformed
        // TODO add your handling code here:
        controlTotal ingreso = new controlTotal(this.usuarioActual);
        ingreso.setVisible(true);
        ingreso.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnIngresosActionPerformed

    private void btnAhorroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAhorroActionPerformed
        // TODO add your handling code here:
        frmMetas ahorro = new frmMetas(this.usuarioActual);
        ahorro.setVisible(true);
        ahorro.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnAhorroActionPerformed

    private void btnAgregarEgresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEgresoActionPerformed
        // TODO add your handling code here:
        frmNuevoEgreso egresos = new frmNuevoEgreso(this.usuarioActual);
        egresos.setLocationRelativeTo(null);
        egresos.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAgregarEgresoActionPerformed

    private void btnReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesActionPerformed
        // TODO add your handling code here:
        frmReportes reportes = new frmReportes(this.usuarioActual);
        reportes.setVisible(true);
        reportes.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnReportesActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        // TODO add your handling code here:
        frmReportes reportes = new frmReportes(this.usuarioActual);
        reportes.setVisible(true);
        reportes.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        // TODO add your handling code here:
        frmMenuPrincipal menu = new  frmMenuPrincipal();
        menu.setVisible(true);
        menu.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnInicioActionPerformed

    private void lblSaludoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSaludoMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_lblSaludoMouseClicked

    private void lblFechaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFechaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblFechaMouseClicked

    private void btnAgregarIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarIngresoActionPerformed
        // TODO add your handling code here:
        frmNuevoIngreso ingresos = new frmNuevoIngreso(this.usuarioActual);
        ingresos.setLocationRelativeTo(null);
        ingresos.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAgregarIngresoActionPerformed

    private void btnNuevaMetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaMetaActionPerformed
        // TODO add your handling code here:
        frmMetasNueva metas = new frmMetasNueva(this.usuarioActual);
        metas.setLocationRelativeTo(null);
        metas.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnNuevaMetaActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new frmMenuPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barMeta1;
    private javax.swing.JProgressBar barMeta2;
    private javax.swing.JButton btnAgregarEgreso;
    private javax.swing.JButton btnAgregarIngreso;
    private javax.swing.JButton btnAhorro;
    private javax.swing.JButton btnGastos;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnIngresos;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnNuevaMeta;
    private javax.swing.JButton btnReportes;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblEgresoTitulo;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblIngresoTitulo;
    private javax.swing.JLabel lblMontoEgreso;
    private javax.swing.JLabel lblMontoIngreso;
    private javax.swing.JLabel lblSaldoMonto;
    private javax.swing.JLabel lblSaldoTitulo;
    private javax.swing.JLabel lblSaludo;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlActividad;
    private javax.swing.JPanel pnlCardEgreso;
    private javax.swing.JPanel pnlCardIngreso;
    private javax.swing.JPanel pnlCardSaldo;
    private javax.swing.JPanel pnlMetas;
    // End of variables declaration//GEN-END:variables
}
