/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package menugenetico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Usuario
 */
public class MenuGenetico {

    /**
     * @param args the command line arguments
     */
    
    static List<Plato> catalogo = new ArrayList<>();
    static List<Menu> poblacion = new ArrayList<>();
    static int maxGeneracionesSinMejora = 10;
    
    public static void main(String[] args) {
        // TODO code application logic here
        inicializarCatalogo();
        inicializarPoblacion();
        ejecutarAlgoritmoGenetico();
    }
    
    public static void inicializarCatalogo() {
        catalogo.add(new Plato(1, "Arroz con pollo", 2.5, 25, 80));
        catalogo.add(new Plato(2, "Ensalada Cesar", 1.8, 10, 65));
        catalogo.add(new Plato(3, "Sopa de lentejas", 2.0, 20, 70));
        catalogo.add(new Plato(4, "Lasana", 3.0, 30, 90));
        catalogo.add(new Plato(5, "Pollo al horno", 2.7, 35, 75));
        catalogo.add(new Plato(6, "Pasta Alfredo", 2.6, 22, 85));
        catalogo.add(new Plato(7, "Fruta picada", 1.2, 5, 60));
        catalogo.add(new Plato(8, "Jugo natural", 1.0, 3, 95));
    }
    
    public static void inicializarPoblacion() {
        Random rand = new Random();
        while (poblacion.size() < 5) {
            Set<Integer> indices = new HashSet<>();
            while (indices.size() < 2 + rand.nextInt(2)) {
                indices.add(rand.nextInt(catalogo.size()));
            }
            List<Plato> seleccionados = new ArrayList<>();
            for (int i : indices) seleccionados.add(catalogo.get(i));
            poblacion.add(new Menu(seleccionados));
        }
    }
    
    public static void ejecutarAlgoritmoGenetico() {
        int generacionesSinMejora = 0;
        double mejorFitness = obtenerMejor().fitness;

        while (generacionesSinMejora < maxGeneracionesSinMejora) {
            Menu padre1 = seleccionarPadre();
            Menu padre2 = seleccionarPadre();

            List<Menu> hijos = cruzar(padre1, padre2);
            mutar(hijos.get(0));
            mutar(hijos.get(1));

            hijos.get(0).calcularFitness();
            hijos.get(1).calcularFitness();

            reemplazo(hijos);

            double nuevoMejorFitness = obtenerMejor().fitness;
            if (nuevoMejorFitness > mejorFitness) {
                mejorFitness = nuevoMejorFitness;
                generacionesSinMejora = 0;
            } else {
                generacionesSinMejora++;
            }
        }

        System.out.println("Mejor menu encontrado:");
        imprimirMenu(obtenerMejor());
    }
    
    public static Menu seleccionarPadre() {
        return poblacion.stream()
                .sorted((m1, m2) -> Double.compare(m2.fitness, m1.fitness))
                .limit(2)
                .skip(new Random().nextInt(2))
                .findFirst().get();
    }
    
    public static List<Menu> cruzar(Menu p1, Menu p2) {
        Set<Plato> hijo1 = new HashSet<>();
        Set<Plato> hijo2 = new HashSet<>();
        hijo1.addAll(p1.platos.subList(0, 1));
        hijo1.addAll(p2.platos.subList(1, p2.platos.size()));
        hijo2.addAll(p2.platos.subList(0, 1));
        hijo2.addAll(p1.platos.subList(1, p1.platos.size()));
        return Arrays.asList(new Menu(new ArrayList<>(hijo1)), new Menu(new ArrayList<>(hijo2)));
    }

    public static void mutar(Menu menu) {
        Random rand = new Random();
        if (rand.nextDouble() < 0.8) { // alta probabilidad para diversidad
            List<Plato> nuevos = new ArrayList<>(menu.platos);
            int pos = rand.nextInt(nuevos.size());
            Plato nuevo;
            do {
                nuevo = catalogo.get(rand.nextInt(catalogo.size()));
            } while (nuevos.contains(nuevo));
            nuevos.set(pos, nuevo);
            menu.platos = nuevos;
        }
    }

    public static void reemplazo(List<Menu> hijos) {
        poblacion.sort(Comparator.comparingDouble(m -> m.fitness));
        for (Menu hijo : hijos) {
            if (hijo.fitness > poblacion.get(0).fitness) {
                poblacion.set(0, hijo);
                poblacion.sort(Comparator.comparingDouble(m -> m.fitness));
            }
        }
    }
    
    public static Menu obtenerMejor() {
        return poblacion.stream().max(Comparator.comparingDouble(m -> m.fitness)).get();
    }

    public static void imprimirMenu(Menu menu) {
        for (Plato p : menu.platos) {
            System.out.println(p.nombre);
        }
        System.out.printf("Fitness: %.2f\n", menu.fitness);
    }
}
