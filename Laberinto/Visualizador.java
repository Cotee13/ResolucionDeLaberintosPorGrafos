package Laberinto;

import java.util.List;
import java.util.Scanner;

public class Visualizador {

    private char[][] laberinto;
    private Grafo grafo;
    private Scanner sc;

    public Visualizador(char[][] laberinto, Grafo grafo, Scanner sc) {
        this.laberinto = laberinto;
        this.grafo = grafo;
        this.sc = sc;
    }
        //mostrar las tres soluciones sin animacion, para comparar los resultados de cada algoritmo
    public void mostrarTresSoluciones() {

        System.out.println("\n===== COMPARACION DE SOLUCIONES =====");

        // Primera solucion: DFS
        System.out.println("\n--- DFS ---");
        List<Vertice> visitasDFS = grafo.DFS();
        mostrarResultado(visitasDFS);

        // Segunda solucion: BFS
        System.out.println("\n--- BFS ---");
        List<Vertice> visitasBFS = grafo.BFS();
        mostrarResultado(visitasBFS);

        // Tercera solucion: Dijkstra
        System.out.println("\n--- DIJKSTRA ---");
        List<Vertice> visitasDijkstra = grafo.caminoMasCorto();
        mostrarResultado(visitasDijkstra);
    }

    
    private void mostrarResultado(List<Vertice> ordenVisita) {

        boolean[][] visitados =
                new boolean[laberinto.length][laberinto[0].length];

        // Marcar los vertices visitados
        for (Vertice vertice : ordenVisita) {
            visitados[vertice.getFila()][vertice.getColumna()] = true;
        }

        // Mostrar el laberinto sin animacion
        mostrar(visitados, null, true);

        System.out.println("Vertices visitados: " + ordenVisita.size());

        if (grafo.isSolucionEncontrada()) {

            int movimientos = grafo.getCaminoFinal().size() - 1;

            System.out.println("Camino encontrado.");
            System.out.println("Longitud: " + movimientos + " movimientos");

        } else {
            System.out.println("No existe camino entre la entrada y la salida.");
        }
    }

    // Mostrar el laberinto paso a paso con animacion
    public void despliegaLaberinto(List<Vertice> ordenVisita) {
         boolean[][] visitados = new boolean[laberinto.length][laberinto[0].length];

        System.out.println("\nLeyenda: E = entrada, S = salida, @ = actual,");
        System.out.println(". = visitado, * = camino final, # = transitable original");
        System.out.println("Presiona Enter para avanzar en cada paso.");

        for (int i = 0; i < ordenVisita.size(); i++) {

            Vertice actual = ordenVisita.get(i);
            visitados[actual.getFila()][actual.getColumna()] = true;

            System.out.println("\nPaso " + (i + 1) + " de " + ordenVisita.size());

            mostrar(visitados, actual, false);

            if (i < ordenVisita.size() - 1) {
                sc.nextLine();
            }
        }

        System.out.println("\n--- Resultado final ---");
        mostrar(visitados, null, true);

        System.out.println("Vertices visitados: " + ordenVisita.size());

        if (grafo.isSolucionEncontrada()) {
            System.out.println("Camino encontrado.");
            System.out.println("Longitud: "
                    + (grafo.getCaminoFinal().size() - 1)
                    + " movimientos");
        } else {
            System.out.println("No existe camino entre la entrada y la salida.");
        }
    }

    private void mostrar(boolean[][] visitados, Vertice actual, boolean finalizado) {

        boolean[][] camino = new boolean[laberinto.length][laberinto[0].length];

        if (finalizado && grafo.isSolucionEncontrada()) {
            for (Vertice vertice : grafo.getCaminoFinal()) {
                camino[vertice.getFila()][vertice.getColumna()] = true;
            }
        }

        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[i].length; j++) {

                if (actual != null
                && i == actual.getFila()
                && j == actual.getColumna()) {
                System.out.print('@');

                } else if (i == grafo.getEntrada().getFila()
                && j == grafo.getEntrada().getColumna()) {
                System.out.print('E');

                } else if (i == grafo.getSalida().getFila()
                && j == grafo.getSalida().getColumna()) {
                System.out.print('S');

                } else if (camino[i][j]) {
                System.out.print('*');

                } else if (visitados[i][j]) {
                System.out.print('.');

                } else {
                  System.out.print(laberinto[i][j]);
}

                }
                System.out.println();
        }
    }

}
    

