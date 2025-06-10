/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menugenetico;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class Menu {
    List<Plato> platos;
    double fitness;

    public Menu(List<Plato> platos) {
        this.platos = new ArrayList<>(platos);
        calcularFitness();
    }

    public void calcularFitness() {
        double a = 0.7, b = 0.5, y = 0.3;
        double totalCosto = 0, totalTiempo = 0, totalPopularidad = 0;
        for (Plato p : platos) {
            totalCosto += p.costo;
            totalTiempo += p.tiempo;
            totalPopularidad += p.popularidad;
        }
        fitness = (a * totalPopularidad) - (b * totalCosto) - (y * totalTiempo);
    }
}
