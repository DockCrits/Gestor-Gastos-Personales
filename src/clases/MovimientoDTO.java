/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author win11
 */
public class MovimientoDTO {
    public String tipo;
    public String concepto;
    public double cantidad;
    public String fecha;
    
    public MovimientoDTO(String tipo, String concepto, double cantidad, String fecha) {
        this.tipo = tipo;
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }
}
