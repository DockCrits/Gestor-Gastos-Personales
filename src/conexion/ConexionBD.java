/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author win 11
 */
public class ConexionBD {
    private static Connection conexion;

    private static final String URL = "jdbc:mysql://mysql-39b27ba9-cristosernas21-6d62.a.aivencloud.com:26911/defaultdb?sslMode=REQUIRED";
    //private static final String URL = "jdbc:mysql://localhost:3306/gestor_gastos";
    private static final String USUARIO = "avnadmin";
    //private static final String USUARIO = "root";
    private static final String PASSWORD = "AVNS_yy0EKmJMyUtNRMeSoPa";
    //private static final String PASSWORD = "Cristo05-21";

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión establecida correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }

    /*public static Connection conectar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }*/
    public static Connection conectar() {
        return getConexion();
    }
}
