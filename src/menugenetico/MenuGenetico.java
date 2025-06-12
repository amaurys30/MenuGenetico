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
    // Catálogo de platos disponibles
    static List<Plato> catalogo = new ArrayList<>();
    // Población de menús actuales
    static List<Menu> poblacion = new ArrayList<>();
    // Criterio de parada: número máximo de generaciones sin mejorar
    static int maxGeneracionesSinMejora = 200;
    //Ajuste del tamaño de la población inicial
    static int tamanioPoblacion = 40; 
    
    public static void main(String[] args) {
        // TODO code application logic here
        inicializarCatalogo(); // Paso 1: Cargar platos al catálogo
        
        long inicioTiempo = System.currentTimeMillis();
        long memoriaAntes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        inicializarPoblacion(); // Paso 2: Generar población inicial
        ejecutarAlgoritmoGenetico(); // Paso 3: Ejecutar el ciclo evolutivo
        
        long finTiempo = System.currentTimeMillis();
        long memoriaDespues = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoriaUsada = memoriaDespues - memoriaAntes;

        System.out.println("Tiempo de ejecucion: " + (finTiempo - inicioTiempo) + " ms");
        System.out.println("Memoria usada (bytes): " + memoriaUsada);
    }
    
    // Inicializa el catálogo con platos predefinidos
    public static void inicializarCatalogo() {
        catalogo.add(new Plato(1, "Arroz con pollo", 2.5, 25, 80));
        catalogo.add(new Plato(2, "Ensalada Cesar", 1.8, 10, 65));
        catalogo.add(new Plato(3, "Sopa de lentejas", 2.0, 20, 70));
        catalogo.add(new Plato(4, "Lasana", 3.0, 30, 90));
        catalogo.add(new Plato(5, "Pollo al horno", 2.7, 35, 75));
        catalogo.add(new Plato(6, "Pasta Alfredo", 2.6, 22, 85));
        catalogo.add(new Plato(7, "Fruta picada", 1.2, 5, 60));
        catalogo.add(new Plato(8, "Jugo natural", 1.0, 3, 95));
        
        catalogo.add(new Plato(9, "Sandwich de atun", 1.2, 8, 95));
        catalogo.add(new Plato(10, "Arroz cubano", 2.2, 20, 70));
        catalogo.add(new Plato(11, "Aguapanela", 0.6, 2, 80));
        catalogo.add(new Plato(12, "Té helado", 0.8, 2, 50));
        
        catalogo.add(new Plato(13, "Arepa con queso", 1.5, 7, 85));
        catalogo.add(new Plato(14, "Crema de ahuyama", 2.0, 18, 78));
        catalogo.add(new Plato(15, "Empanadas mixtas", 1.7, 12, 88));
        
        // Nuevos platos agregados
        /*catalogo.add(new Plato(16, "Sancocho de gallina", 3.5, 40, 92));
        catalogo.add(new Plato(17, "Batido de frutas", 1.5, 4, 83));
        catalogo.add(new Plato(18, "Hamburguesa casera", 2.8, 25, 87));
        catalogo.add(new Plato(19, "Tamales", 2.4, 30, 75));
        catalogo.add(new Plato(20, "Pan de bono", 1.0, 6, 65));*/
        
        // 10 platos adicionales
        /*catalogo.add(new Plato(21, "Pescado frito", 3.2, 28, 82));
        catalogo.add(new Plato(22, "Arequipe con galleta", 1.1, 2, 70));
        catalogo.add(new Plato(23, "Torta de zanahoria", 2.0, 15, 88));
        catalogo.add(new Plato(24, "Ajiaco", 3.0, 38, 85));
        catalogo.add(new Plato(25, "Bandeja paisa", 3.8, 40, 93));
        catalogo.add(new Plato(26, "Ensalada de frutas", 1.5, 6, 90));
        catalogo.add(new Plato(27, "Papas rellenas", 1.9, 14, 77));
        catalogo.add(new Plato(28, "Arroz chino", 2.3, 25, 84));
        catalogo.add(new Plato(29, "Chorizo con arepa", 2.0, 10, 79));
        catalogo.add(new Plato(30, "Brownie con helado", 2.5, 8, 91));*/
    }
    
    // Crea una población inicial de menús aleatorios del tamaño especificado en tamanioPoblacion
    public static void inicializarPoblacion() {
        Random rand = new Random();
        while (poblacion.size() < tamanioPoblacion) {
            Set<Integer> indices = new HashSet<>();
            while (indices.size() < 2 + rand.nextInt(2)) {
                indices.add(rand.nextInt(catalogo.size()));
            }
            List<Plato> seleccionados = new ArrayList<>();
            for (int i : indices) seleccionados.add(catalogo.get(i));
            poblacion.add(new Menu(seleccionados));
        }
    }
    
    // Ejecuta el algoritmo genético principal
    public static void ejecutarAlgoritmoGenetico() {
        int generacionesSinMejora = 0;
        double mejorFitness = obtenerMejor().fitness;
        // Repite mientras no se superen las 10 generaciones sin mejorar
        while (generacionesSinMejora < maxGeneracionesSinMejora) {
            // Selección de padres
            Menu padre1 = seleccionarPadre();
            Menu padre2 = seleccionarPadre();

            // Cruze y mutación
            List<Menu> hijos = cruzar(padre1, padre2);
            mutar(hijos.get(0));
            mutar(hijos.get(1));
            // Evaluar fitness de los hijos
            hijos.get(0).calcularFitness();
            hijos.get(1).calcularFitness();
            // Reemplazo en la población si corresponde
            reemplazo(hijos);
            // Evaluación de mejora
            double nuevoMejorFitness = obtenerMejor().fitness;
            if (nuevoMejorFitness > mejorFitness) {
                mejorFitness = nuevoMejorFitness;
                generacionesSinMejora = 0;
            } else {
                generacionesSinMejora++;
            }
        }
        // Imprimir la mejor solución encontrada
        System.out.println("Mejor menu encontrado:");
        imprimirMenu(obtenerMejor());
    }
    
    // Selecciona uno de los dos mejores padres aleatoriamente
    public static Menu seleccionarPadre() {
        return poblacion.stream()
                .sorted((m1, m2) -> Double.compare(m2.fitness, m1.fitness))
                .limit(2)
                .skip(new Random().nextInt(2))
                .findFirst().get();
    }
    
    // Realiza cruce entre dos menús (padres)
    public static List<Menu> cruzar(Menu p1, Menu p2) {
        Set<Plato> hijo1 = new HashSet<>();
        Set<Plato> hijo2 = new HashSet<>();
        hijo1.addAll(p1.platos.subList(0, 1));
        hijo1.addAll(p2.platos.subList(1, p2.platos.size()));
        hijo2.addAll(p2.platos.subList(0, 1));
        hijo2.addAll(p1.platos.subList(1, p1.platos.size()));
        return Arrays.asList(new Menu(new ArrayList<>(hijo1)), new Menu(new ArrayList<>(hijo2)));
    }

    // Aplica mutación a un menú (reemplaza un plato por otro distinto)
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
    
    // Reemplaza los peores individuos por hijos si estos son mejores
    public static void reemplazo(List<Menu> hijos) {
        poblacion.sort(Comparator.comparingDouble(m -> m.fitness));
        for (Menu hijo : hijos) {
            if (hijo.fitness > poblacion.get(0).fitness) {
                poblacion.set(0, hijo);
                poblacion.sort(Comparator.comparingDouble(m -> m.fitness));
            }
        }
    }
    
    // Devuelve el menú con mayor fitness de la población
    public static Menu obtenerMejor() {
        return poblacion.stream().max(Comparator.comparingDouble(m -> m.fitness)).get();
    }

     // Imprime los platos de un menú y su fitness
    public static void imprimirMenu(Menu menu) {
        for (Plato p : menu.platos) {
            System.out.println(p.nombre);
        }
        System.out.printf("Fitness: %.2f\n", menu.fitness);
    }
}
