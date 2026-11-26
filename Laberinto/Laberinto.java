package Laberinto;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Laberinto {

    static int filaEntrada, columnaEntrada;
    static int filaSalida, columnaSalida;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //se creo un pequeño menu para tener mas orden y que se vea mas limpio
        System.out.println("===== CARGAR LABERINTO =====");
        System.out.println("1. laberinto1.txt");
        System.out.println("2. laberinto2.txt");
        System.out.println("3. laberinto3.txt");
        System.out.println("4. Cargar otro archivo");

        System.out.print("Selecciona una opcion: ");
        String opcion = sc.nextLine();
        String ruta;

        //aqui van las URL con los grafos dentro
        switch (opcion) {
            case "1":
                ruta = "C:\\Users\\juanp\\OneDrive\\Documentos\\GitHub\\ResolucionDeLaberintosPorGrafos\\laberinto1.txt";
                break;
            case "2":
                ruta = "laberintos/laberinto2.txt";
                break;
            case "3":
                ruta = "laberintos/laberinto3.txt";
                break;
            case "4":
                System.out.print("Ruta del archivo: ");
                ruta = sc.nextLine();
                break;
            default:
                System.out.println("Opcion no valida.");
                sc.close();
                return;
        }

        char[][] laberinto = cargarLaberinto(ruta);

        if (laberinto != null) {
            System.out.println("\nLaberinto cargado correctamente.");
            System.out.println("Renglones: " + laberinto.length);
            System.out.println("Columnas: " + laberinto[0].length);
            System.out.println("Entrada: (" + filaEntrada + ", " + columnaEntrada + ")");
            System.out.println("Salida: (" + filaSalida + ", " + columnaSalida + ")");
            System.out.println();

            for (int i = 0; i < laberinto.length; i++) {
                for (int j = 0; j < laberinto[i].length; j++) {
                    System.out.print(laberinto[i][j]);
                }
                System.out.println();
            }
        }
        sc.close();
    }

    //se implementa el cargar laberinto y revisar si es valido o no
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

        // Verificar si el archivo esta vacio
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
        int cantidadE = 0;
        int cantidadS = 0;
        int entradaFila = -1;
        int entradaColumna = -1;
        int salidaFila = -1;
        int salidaColumna = -1;
        int aberturas = 0;
        int primeraFila = -1;
        int primeraColumna = -1;
        int segundaFila = -1;
        int segundaColumna = -1;

        //se validan los caracteres del laberinto
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
}
