/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menugenetico;

/**
 *
 * @author Usuario
 */
// Clase que representa un plato con sus atributos
public class Plato {
    int id;
    String nombre;
    double costo;
    int tiempo;
    int popularidad;

    public Plato(int id, String nombre, double costo, int tiempo, int popularidad) {
        this.id = id;
        this.nombre = nombre;
        this.costo = costo;
        this.tiempo = tiempo;
        this.popularidad = popularidad;
    }
}
