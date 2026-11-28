package Laberinto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Laberinto {

    static Scanner sc = new Scanner(System.in);
    static int filaEntrada, columnaEntrada, filaSalida, columnaSalida;
    static int pausaMs = 1;

    static char[][] laberinto = null;
    static Grafo grafo = null;
    static String algoritmo = "";


    static List<Vertice> visitasDFS, caminoDFS, visitasBFS, caminoBFS, visitasDijkstra, caminoDijkstra;

    public static void main(String[] args) {
        String opcion;

        do {
            System.out.println("\n1. Cargar laberinto");
            System.out.println("2. Resolver laberinto con DFS");
            System.out.println("3. Resolver laberinto con BFS");
            System.out.println("4. Resolver laberinto con Dijkstra (camino mas corto)");
            System.out.println("5. Mostrar las tres soluciones");
            System.out.println("6. Salir");
            System.out.print("Selecciona una opcion: ");
            opcion = sc.nextLine();

            if (!opcion.equals("1") && !opcion.equals("6") && laberinto == null) {
                System.out.println("Error: primero debes cargar un laberinto.");
                continue;
            }

            switch (opcion) {
                case "1":
                    seleccionarArchivo();
                    break;
                case "2":
                    algoritmo = "DFS";
                    visitasDFS = grafo.DFS();
                    caminoDFS = grafo.getCaminoFinal();
                    despliegaLaberinto(visitasDFS);
                    break;
                case "3":
                    algoritmo = "BFS";
                    visitasBFS = grafo.BFS();
                    caminoBFS = grafo.getCaminoFinal();
                    despliegaLaberinto(visitasBFS);
                    break;
                case "4":
                    algoritmo = "Dijkstra";
                    visitasDijkstra = grafo.caminoMasCorto();
                    caminoDijkstra = grafo.getCaminoFinal();
                    despliegaLaberinto(visitasDijkstra);
                    break;
                case "5":
                    mostrarTresSoluciones();
                    break;
                case "6":
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (!opcion.equals("6"));

        sc.close();
    }

    public static void seleccionarArchivo() {
        System.out.println("\n===== CARGAR LABERINTO =====");
        System.out.println("1. laberinto1.txt");
        System.out.println("2. laberinto2.txt");
        System.out.println("3. laberinto3.txt");
        System.out.println("4. Cargar otro archivo");
        System.out.print("Selecciona una opcion: ");
        String opcion = sc.nextLine();
        String ruta;

        switch (opcion) {
            case "1":
                ruta = "C:\\Users\\juanp\\OneDrive\\Documentos\\GitHub\\ResolucionDeLaberintosPorGrafos\\laberinto1_v2.txt";
                break;
            case "2":
                ruta = "C:\\Users\\juanp\\OneDrive\\Documentos\\GitHub\\ResolucionDeLaberintosPorGrafos\\laberinto2_v2.txt";
                break;
            case "3":
                ruta = "C:\\Users\\juanp\\OneDrive\\Documentos\\GitHub\\ResolucionDeLaberintosPorGrafos\\laberinto3_v2.txt";
                break;
            case "4":
                System.out.print("Ruta del archivo: ");
                ruta = sc.nextLine();
                break;
            default:
                System.out.println("Opcion no valida.");
                return;
        }

        char[][] nuevo = cargarLaberinto(ruta);
        if (nuevo != null) {
            laberinto = nuevo;
            grafo = new Grafo(laberinto, filaEntrada, columnaEntrada, filaSalida, columnaSalida);
            visitasDFS = visitasBFS = visitasDijkstra = null;

            System.out.println("\nLaberinto cargado correctamente (" + laberinto.length + "x" + laberinto[0].length + ")");
            System.out.println("Entrada: (" + filaEntrada + ", " + columnaEntrada + ") | Salida: (" + filaSalida + ", " + columnaSalida + ")\n");
            for (int i = 0; i < laberinto.length; i++) {
                System.out.println(laberinto[i]);
            }
        }
    }


    public static char[][] cargarLaberinto(String ruta) {
        ArrayList<String> lineas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }

        if (lineas.isEmpty()) {
            System.out.println("El archivo esta vacio.");
            return null;
        }

        int renglones = lineas.size();
        int columnas = lineas.get(0).length();

        if (columnas == 0) {
            System.out.println("Error: el laberinto no tiene columnas.");
            return null;
        }

        for (int i = 0; i < renglones; i++) {
            if (lineas.get(i).length() != columnas) {
                System.out.println("Error: los renglones tienen diferente longitud.");
                return null;
            }
        }

        char[][] matriz = new char[renglones][columnas];
        int cantidadE = 0, cantidadS = 0;
        int entradaFila = -1, entradaColumna = -1;
        int salidaFila = -1, salidaColumna = -1;
        int aberturas = 0;
        int primeraFila = -1, primeraColumna = -1;
        int segundaFila = -1, segundaColumna = -1;

        for (int i = 0; i < renglones; i++) {
            String linea = lineas.get(i);
            for (int j = 0; j < columnas; j++) {
                char c = linea.charAt(j);

                if (c != '+' && c != '-' && c != '|'
                        && c != ' ' && c != '#'
                        && c != 'E' && c != 'S') {
                    System.out.println("Error: caracter invalido en (" + i + ", " + j + ")");
                    return null;
                }
                matriz[i][j] = c;
                if (c == 'E') {
                    cantidadE++;
                    entradaFila = i;
                    entradaColumna = j;
                }
                if (c == 'S') {
                    cantidadS++;
                    salidaFila = i;
                    salidaColumna = j;
                }

                boolean esBorde = i == 0 || i == renglones - 1
                        || j == 0 || j == columnas - 1;
                if (esBorde && (c == ' ' || c == '#')) {
                    aberturas++;
                    if (aberturas == 1) {
                        primeraFila = i;
                        primeraColumna = j;
                    } else if (aberturas == 2) {
                        segundaFila = i;
                        segundaColumna = j;
                    }
                }
            }
        }

        if (cantidadE > 0 || cantidadS > 0) {
            if (cantidadE != 1 || cantidadS != 1) {
                System.out.println("Error: debe existir una E y una S.");
                return null;
            }
        } else {
            if (aberturas != 2) {
                System.out.println("Error: debe haber dos posiciones transitables en el borde.");
                return null;
            }
            entradaFila = primeraFila;
            entradaColumna = primeraColumna;
            salidaFila = segundaFila;
            salidaColumna = segundaColumna;
        }

        filaEntrada = entradaFila;
        columnaEntrada = entradaColumna;
        filaSalida = salidaFila;
        columnaSalida = salidaColumna;
        return matriz;
    }


    public static void despliegaLaberinto(List<Vertice> ordenVisita) {
        List<Vertice> visitados = new ArrayList<>();

        for (int i = 0; i < ordenVisita.size(); i++) {
            Vertice actual = ordenVisita.get(i);
            visitados.add(actual);

            System.out.println("\n" + algoritmo + " - Paso " + (i + 1) + "/" + ordenVisita.size());
            System.out.println("Leyenda: E=Entrada | S=Salida | @=Actual | .=Visitado | +-|=Pared");
            imprimirTablero(visitados, null, actual);

            try {
                Thread.sleep(pausaMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        List<Vertice> caminoFinal = grafo.getCaminoFinal();
        System.out.println("\n===== RESULTADO FINAL: " + algoritmo + " =====");
        System.out.println("Leyenda: E=Entrada | S=Salida | *=Camino | .=Explorado | +-|=Pared");
        imprimirTablero(visitados, caminoFinal, null);
        System.out.println("Vertices visitados: " + ordenVisita.size());
        System.out.println(!caminoFinal.isEmpty()
                ? "Longitud del camino (movimientos): " + (caminoFinal.size() - 1)
                : "No existe camino entre la entrada y la salida.");
    }


    public static void mostrarTresSoluciones() {
        if (visitasDFS == null) {
            visitasDFS = grafo.DFS();
            caminoDFS = grafo.getCaminoFinal();
        }
        if (visitasBFS == null) {
            visitasBFS = grafo.BFS();
            caminoBFS = grafo.getCaminoFinal();
        }
        if (visitasDijkstra == null) {
            visitasDijkstra = grafo.caminoMasCorto();
            caminoDijkstra = grafo.getCaminoFinal();
        }

        String[] nombres = {"DFS", "BFS", "Dijkstra (camino mas corto)"};
        List[] visitas = {visitasDFS, visitasBFS, visitasDijkstra};
        List[] caminos = {caminoDFS, caminoBFS, caminoDijkstra};

        System.out.println("\n===== LAS TRES SOLUCIONES =====");
        System.out.println("Leyenda: E=Entrada | S=Salida | *=Camino | .=Explorado");

        for (int k = 0; k < 3; k++) {
            System.out.println("\n--- " + nombres[k] + " ---");
            imprimirTablero(visitas[k], caminos[k], null);
            System.out.println("Vertices visitados: " + visitas[k].size());
            System.out.println(!caminos[k].isEmpty()
                    ? "Longitud del camino (movimientos): " + (caminos[k].size() - 1)
                    : "No existe camino.");
        }
    }

    private static void imprimirTablero(List<Vertice> visitados, List<Vertice> camino, Vertice actual) {
        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[0].length; j++) {
                if (actual != null && actual.getFila() == i && actual.getColumna() == j) {
                    System.out.print('@');
                } else if (i == filaEntrada && j == columnaEntrada) {
                    System.out.print('E');
                } else if (i == filaSalida && j == columnaSalida) {
                    System.out.print('S');
                } else if (contiene(camino, i, j)) {
                    System.out.print('*');
                } else if (contiene(visitados, i, j)) {
                    System.out.print('.');
                } else if (laberinto[i][j] == '#') {
                    System.out.print(' ');
                } else {
                    System.out.print(laberinto[i][j]);
                }
            }
            System.out.println();
        }
    }

    private static boolean contiene(List<Vertice> lista, int fila, int col) {
        if (lista == null) return false;
        for (Vertice v : lista) {
            if (v.getFila() == fila && v.getColumna() == col) return true;
        }
        return false;
    }
}