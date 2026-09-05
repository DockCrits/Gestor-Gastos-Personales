/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author win11
 */
public class Admin extends Usuario{
    public Admin(){
        super();
        this.setRol("admin");
    }
    
    public Admin(int id, String nombre, String correo, String contrasena) {
        super();
        this.setId(id);
        this.setNombre(nombre);
        this.setCorreo(correo);
        this.setContrasena(contrasena);
        this.setRol("admin");
    }
}
