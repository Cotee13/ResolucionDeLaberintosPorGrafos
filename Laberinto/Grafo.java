package Laberinto;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.HashSet;
import java.util.Queue;

public class Grafo {
    private Vertices vertices;
    private ArrayList<Arcos> arcos;
    private Hashtable<Integer, LinkedList<Vertice>> adyacencia;

    private Vertice entrada;
    private Vertice salida;

    private List<Vertice> caminoFinal;
    private boolean solucionEncontrada;

    public Grafo(char[][] laberinto, int filaEntrada, int columnaEntrada,
                 int filaSalida, int columnaSalida) {

        vertices = new Vertices();
        arcos = new ArrayList<>();
        adyacencia = new Hashtable<>();

        caminoFinal = new ArrayList<>();
        solucionEncontrada = false;

        int renglones = laberinto.length;
        int columnas = laberinto[0].length;

        // se crean los vertices
        for (int i = 0; i < renglones; i++) {
            for (int j = 0; j < columnas; j++) {

                if (esTransitable(laberinto[i][j])) {
                    int id = i * columnas + j;

                    Vertice vertice = new Vertice(id, i, j);
                    vertices.agregarVertice(vertice);

                    adyacencia.put(id, new LinkedList<>());
                }
            }
        }

        entrada = obtenerVertice(filaEntrada, columnaEntrada, columnas);
        salida = obtenerVertice(filaSalida, columnaSalida, columnas);


        int[] cambioFila = {-1, 0, 1, 0};
        int[] cambioColumna = {0, 1, 0, -1};

        // se crean las conexiones
        for (int i = 0; i < renglones; i++) {
            for (int j = 0; j < columnas; j++) {

                if (!esTransitable(laberinto[i][j])) {
                    continue;
                }

                Vertice actual = obtenerVertice(i, j, columnas);

                for (int k = 0; k < 4; k++) {
                    int nuevaFila = i + cambioFila[k];
                    int nuevaColumna = j + cambioColumna[k];

                    if (nuevaFila >= 0 && nuevaFila < renglones
                            && nuevaColumna >= 0 && nuevaColumna < columnas
                            && esTransitable(laberinto[nuevaFila][nuevaColumna])) {

                        Vertice vecino = obtenerVertice(
                                nuevaFila, nuevaColumna, columnas);

                        adyacencia.get(actual.getId()).add(vecino);

                        // con el if se evitan registrar 2 arcos
                        if (actual.getId() < vecino.getId()) {
                            arcos.add(new Arcos(actual, vecino, 1));
                        }
                    }
                }
            }
        }
    }

    private boolean esTransitable(char c) {
        return c == ' ' || c == '#' || c == 'E' || c == 'S';
    }

    private Vertice obtenerVertice(int fila, int columna, int columnas) {
        return vertices.obtenerVertice(fila * columnas + columna);
    }

    public Vertice getEntrada() {
        return entrada;
    }

    public Vertice getSalida() {
        return salida;
    }

    public int getCantidadVertices() {
        return vertices.cantidad();
    }

    public int getCantidadArcos() {
        return arcos.size();
    }

    public ArrayList<Vertice> obtenerVecinos(Vertice actual) {
        return new ArrayList<>(adyacencia.get(actual.getId()));
    }

    public List<Vertice> getCaminoFinal() {
        return new ArrayList<>(caminoFinal);
    }

    public boolean isSolucionEncontrada() {
        return solucionEncontrada;
    }

    public void guardarResultado(List<Vertice> camino, boolean encontrada) {
        caminoFinal = new ArrayList<>(camino);
        solucionEncontrada = encontrada;
    }

    public void limpiarResultado() {
        caminoFinal.clear();
        solucionEncontrada = false;
    }

    // se implementan los algoritmos

    public List<Vertice> DFS() {
        limpiarResultado();

        List<Vertice> ordenVisita = new ArrayList<>();
        HashSet<Integer> visitados = new HashSet<>();
        Hashtable<Integer, Vertice> predecesores = new Hashtable<>();

        Stack<Vertice> pila = new Stack<>();
        Stack<Vertice> padres = new Stack<>();

        pila.push(entrada);
        padres.push(entrada);

        while (!pila.isEmpty()) {
            
            Vertice actual = pila.pop();
            Vertice padre = padres.pop();

            if (visitados.contains(actual.getId())) {
                continue;
                
            }
            visitados.add(actual.getId());
            ordenVisita.add(actual);

            if (actual.getId() != entrada.getId()) {
                predecesores.put(actual.getId(), padre);
                
            }

            //Metodo si encuentra la salida
            if (actual.getId() == salida.getId()) {
                List<Vertice> camino = new ArrayList<>();
                Vertice paso = salida;

                while (paso.getId() != entrada.getId()) {
                    camino.add(0, paso);
                    paso = predecesores.get(paso.getId());
                    
                }
                camino.add(0, entrada);

                guardarResultado(camino, true);
                return ordenVisita;
                
            }

            ArrayList<Vertice> vecinos = obtenerVecinos(actual);
            for(int i = vecinos.size() - 1; i >= 0; i--) {
                Vertice vecino = vecinos.get(i);

                if (!visitados.contains(vecino.getId())) {
                    pila.push(vecino);
                    padres.push(actual);
                }
            }
            
        }

        //Para cuando no encuentre ni un camino
        guardarResultado(new ArrayList<>(), false);
        return ordenVisita;
    }

    public List<Vertice> BFS() {
        
        limpiarResultado();

        List<Vertice> ordenVisita = new ArrayList<>();
        HashSet<Integer> visitados = new HashSet<>();
        Hashtable<Integer, Vertice> predecesores = new Hashtable<>();

        Queue<Vertice> cola = new LinkedList<>();

        cola.add(entrada);
        visitados.add(entrada.getId());

        while (!cola.isEmpty()) {
            Vertice actual = cola.poll();
            ordenVisita.add(actual);

            //metodo si encuentra la salida
            if (actual.getId() == salida.getId()) {
                List<Vertice> camino = new ArrayList<>();
                Vertice paso = salida;

                while (paso.getId() != entrada.getId()) {
                    camino.add(0, paso);
                    paso = predecesores.get(paso.getId());
                }
                camino.add(0, entrada);

                guardarResultado(camino, true);
                return ordenVisita;
            }
            
            ArrayList<Vertice> vecinos = obtenerVecinos(actual);
            for (Vertice vecino : vecinos) {
                if (!visitados.contains(vecino.getId())) {
                    visitados.add(vecino.getId());
                    predecesores.put(vecino.getId(), actual);
                    cola.add(vecino);
                }
            }
        }

        //Para cuando no encuentre el camino
        guardarResultado(new ArrayList<>(), false);
        return ordenVisita;
    }

    public List<Vertice> caminoMasCorto() {
        throw new UnsupportedOperationException(
                "Dijkstra pendiente de implementar");
    }
}

