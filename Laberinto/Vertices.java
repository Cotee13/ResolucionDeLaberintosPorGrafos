package Laberinto;

import java.util.Hashtable;

public class Vertices {
    private Hashtable<Integer, Vertice> tabla;

    public Vertices() {
        tabla = new Hashtable<>();
    }

    public void agregarVertice(Vertice vertice) {
        tabla.put(vertice.getId(), vertice);
    }

    public Vertice obtenerVertice(int id) {
        return tabla.get(id);
    }

    public int cantidad() {
        return tabla.size();
    }
}

